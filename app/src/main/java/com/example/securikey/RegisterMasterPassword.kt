package com.example.securikey

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.example.securikey.crypto.EncryptDecrypt
import com.example.securikey.databinding.ActivityRegisterMasterPasswordBinding
import com.example.securikey.repository.masterkey.MasterKeyRepository
import com.example.securikey.repository.masterkey.MasterKeyViewModelFactory
import com.example.securikey.room.masterkey.MasterKey
import com.example.securikey.room.masterkey.MasterKeyDatabase
import com.example.securikey.viewmodel.MasterKeyViewModel

class RegisterMasterPassword : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterMasterPasswordBinding
    private lateinit var masterKeyViewmodel : MasterKeyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityRegisterMasterPasswordBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

//        masterKeyViewmodel = MasterPassword().masterKeyViewModel
        val masterKeyRepository = MasterKeyRepository(MasterKeyDatabase.getDatabase(this))
        val viewModelFactory = MasterKeyViewModelFactory(application, masterKeyRepository)
        masterKeyViewmodel = ViewModelProvider(this, viewModelFactory)[MasterKeyViewModel::class
            .java]

        binding.masterPwEt.setOnFocusChangeListener { _, focused ->
            if (!focused){
                binding.masterPwEtContainer.helperText = validPassword(binding.masterPwEt.text.toString())
            }
        }

        binding.remasterPwEt.addTextChangedListener {
            if(it.toString() != binding.masterPwEt.text.toString()){
                binding.remasterPwEtContainer.helperText = "Password does not match"
            }else{
                binding.remasterPwEtContainer.helperText = null
            }
        }

        binding.masterRegisterBtn.setOnClickListener {
            if(binding.masterPwEt.text.toString() != binding.remasterPwEt.text.toString()){
                binding.remasterPwEtContainer.helperText = "Password does not match"
            } else {
                val mpw = EncryptDecrypt().encrypt(binding.masterPwEt.text.toString())
                try {
                    val masterPassword = MasterKey(0, mpw)
                    masterKeyViewmodel.insertmKey(masterPassword)
                    Toast.makeText(this, "Master Password Registered", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this, MasterPassword::class.java)
                    startActivity(intent)
                    finish()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

    }

    private fun validPassword(pwText : String) : String?{
        if(pwText.length < 8){
            return "Minimum 8 character password"
        }
        if(!pwText.matches(".*[A-Z].*".toRegex())){
            return "Must contain 1 uppercase character"
        }
        if(!pwText.matches(".*[a-z].*".toRegex())){
            return "Must contain 1 lowercase character"
        }
        if(!pwText.matches(".*[0-9].*".toRegex())){
            return "Must contain 1 digit"
        }
        if(!pwText.matches(".*[!@#\$%^&+=\\-_():;'\"<>?*,./|].*".toRegex())){
            return "Must contain 1 special character"
        }

        return null
    }
}
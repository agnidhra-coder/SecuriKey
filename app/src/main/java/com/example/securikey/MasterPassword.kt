package com.example.securikey

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.securikey.crypto.EncryptDecrypt
import com.example.securikey.databinding.ActivityMasterPasswordBinding
import com.example.securikey.repository.masterkey.MasterKeyRepository
import com.example.securikey.repository.masterkey.MasterKeyViewModelFactory
import com.example.securikey.room.masterkey.MasterKey
import com.example.securikey.room.masterkey.MasterKeyDatabase
import com.example.securikey.viewmodel.MasterKeyViewModel

class MasterPassword : AppCompatActivity() {
    private lateinit var binding: ActivityMasterPasswordBinding
    lateinit var masterKeyViewModel: MasterKeyViewModel
    private var mPwList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMasterPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        setupMKeyViewmodel()
        val masterKeyRepository = MasterKeyRepository(MasterKeyDatabase.getDatabase(this))
        val viewModelFactory = MasterKeyViewModelFactory(application, masterKeyRepository)
        masterKeyViewModel = ViewModelProvider(this, viewModelFactory)[MasterKeyViewModel::class.java]

        binding.masterPwEt.setOnFocusChangeListener { _, focused ->
            if(!focused){
                if(binding.masterPwEt.text.toString().isEmpty()){
                    binding.masterPwEtContainer.helperText = "Required"
                    return@setOnFocusChangeListener
                }
            }
        }

        binding.masterLoginBtn.setOnClickListener {
            masterKeyViewModel.getAllmKey().observe(this) { mKey ->
                if (mKey != null){
                    for(i in mKey){
                        val decryptedKey = EncryptDecrypt().decrypt(i.mKey)
                        mPwList.add(decryptedKey)
                    }
                } else{
                    Toast.makeText(this, "No Master Password Registered, create one!", Toast
                        .LENGTH_SHORT).show()
                }
                if(mPwList.isNotEmpty()){
                    val match = mPwList.filter {it in binding.masterPwEt.text.toString()}
                    if(match.isNotEmpty()){
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else{
                        binding.masterPwEtContainer.helperText = "Wrong Master Password"
                    }
                }
            }
        }


        binding.createMKeyTV.setOnClickListener {
            val intent = Intent(this, RegisterMasterPassword::class.java)
            startActivity(intent)
            finish()
        }

    }

    private fun setupMKeyViewmodel() {
        val masterKeyRepository = MasterKeyRepository(MasterKeyDatabase.getDatabase(this))
        val viewModelFactory = MasterKeyViewModelFactory(application, masterKeyRepository)
        masterKeyViewModel = ViewModelProvider(this, viewModelFactory)[MasterKeyViewModel::class.java]
    }
}
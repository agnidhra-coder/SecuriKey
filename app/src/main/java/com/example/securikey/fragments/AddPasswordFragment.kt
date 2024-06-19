package com.example.securikey.fragments

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.securikey.MainActivity
import com.example.securikey.R
import com.example.securikey.crypto.EncryptDecrypt
import com.example.securikey.databinding.FragmentAddPasswordBinding
import com.example.securikey.room.Password
import com.example.securikey.viewmodel.PasswordViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.Date

class AddPasswordFragment : BottomSheetDialogFragment(R.layout.fragment_add_password) {
    private var addPasswordBinding: FragmentAddPasswordBinding? = null
    private val binding get() = addPasswordBinding!!
//    private lateinit var bottomSheetBehavior: BottomSheetBehavior<*>

    private lateinit var passwordViewModel: PasswordViewModel
    private lateinit var combinedData: ByteArray
    lateinit var iv: ByteArray


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        addPasswordBinding = FragmentAddPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        passwordViewModel = (activity as MainActivity).passwordViewModel

        /*bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheetAddPw)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED

        binding.bottomSheetAddPw.minimumHeight = Resources.getSystem().displayMetrics.heightPixels*/

        binding.websiteNameEt.setOnFocusChangeListener { _, focused ->
            if(!focused){
                binding.websiteNameContainer.helperText = emptyField(binding.websiteNameEt.text.toString())
            }
        }
        binding.websiteUrlEt.setOnFocusChangeListener { _, focused ->
            if(!focused){
                binding.websiteUrlContainer.helperText = emptyField(binding.websiteUrlEt.text.toString())
            }
        }
        binding.usernameEt.setOnFocusChangeListener { _, focused ->
            if(!focused){
                binding.usernameContainer.helperText = emptyField(binding.usernameEt.text.toString())
            }
        }
        binding.emailEt.setOnFocusChangeListener { _, focused ->
            if(!focused){
                binding.emailContainer.helperText = validEmail(binding.emailEt.text.toString())
            }
        }
        binding.pwEt.setOnFocusChangeListener { _, focused ->
            if(!focused){
                binding.pwContainer.helperText = validPassword(binding.pwEt.text.toString())
            }
        }

        binding.saveBtn.setOnClickListener {
            if(binding.websiteNameEt.text.toString().isNotEmpty() && binding.websiteUrlEt.text
                .toString().isNotEmpty() && binding.usernameEt.text.toString().isNotEmpty() &&
                binding.emailEt.text.toString().isNotEmpty() && binding.pwEt.text.toString().isNotEmpty())
            {
                savePassword()
            } else{
                if (binding.websiteNameEt.text.toString().isEmpty()){
                    binding.websiteNameContainer.helperText = "Required"
                }
                if (binding.websiteUrlEt.text.toString().isEmpty()){
                    binding.websiteUrlContainer.helperText = "Required"
                }
                if (binding.usernameEt.text.toString().isEmpty()){
                    binding.usernameContainer.helperText = "Required"
                }
                if (binding.emailEt.text.toString().isEmpty()){
                    binding.emailContainer.helperText = "Required"
                }
                if (binding.pwEt.text.toString().isEmpty()){
                    binding.pwContainer.helperText = "Required"
                }
            }

        }

        binding.closeBtn.setOnClickListener {
            it.findNavController().popBackStack()
        }

    }

    private fun validEmail(emailText: String): String? {
        if(emailText.isNotEmpty()){
            if(!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()){
                return "Invalid Email Address"
            }
        }
        else{
            return "Required"
        }

        return null
    }

    private fun emptyField(fieldName : String) : String?{

        if(fieldName.isEmpty()){
            return "Required"
        }
        return null
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
        if(!pwText.matches(".*[!@#\$%^&+=\\-_()].*".toRegex())){
            return "Must contain 1 special character"
        }

        binding.saveBtn.setOnClickListener{
            savePassword()
        }

        return null
    }

    private fun savePassword(){
        val websiteName = binding.websiteNameEt.text.toString().trim()
        val websiteUrl = binding.websiteUrlEt.text.toString().trim()
        val username = binding.usernameEt.text.toString().trim()
        val email = binding.emailEt.text.toString().trim()
        val pw = binding.pwEt.text.toString()


        val cipherText = EncryptDecrypt().encrypt(pw)
//        val combinedData = iv.toString() + cipherText

//        val ivStored = iv
//        Log.i("iv", "$iv")
//        Log.i("iv", "$ivStored")

        try {
            val password = Password(0, websiteName, websiteUrl, email, username, cipherText, Date())
            passwordViewModel.insertPW(password)
            dismiss()
        } catch (e: Exception){
            Log.i("AddPasswordFragment", "$e")
        }


    }

//    private fun encryptedPassword(pw: String, iv: ByteArray) : String{
//        val cipherText = CryptoManager().encrypt(pw, iv)
//
//        return cipherText
//    }

    override fun onDestroy() {
        super.onDestroy()
        addPasswordBinding = null
    }

}
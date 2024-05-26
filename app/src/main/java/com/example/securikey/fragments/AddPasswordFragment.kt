package com.example.securikey.fragments

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.securikey.R
import com.example.securikey.databinding.FragmentAddPasswordBinding
import com.example.securikey.viewmodel.PasswordViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AddPasswordFragment : BottomSheetDialogFragment(R.layout.fragment_add_password) {
    private var addPasswordBinding: FragmentAddPasswordBinding? = null
    private val binding get() = addPasswordBinding!!
//    private lateinit var bottomSheetBehavior: BottomSheetBehavior<*>

    private lateinit var passwordViewModel: PasswordViewModel
    private lateinit var addPasswordView: View


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


        return null
    }
}
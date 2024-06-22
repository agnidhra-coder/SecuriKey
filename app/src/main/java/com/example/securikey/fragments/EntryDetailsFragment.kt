package com.example.securikey.fragments

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.navigation.findNavController
import com.example.securikey.BiometricPromptManager
import com.example.securikey.MainActivity
import com.example.securikey.R
import com.example.securikey.crypto.EncryptDecrypt
import com.example.securikey.databinding.FragmentEntryDetailsBinding
import com.example.securikey.viewmodel.PasswordViewModel

class EntryDetailsFragment : Fragment(R.layout.fragment_entry_details) {
    private var entryDetailsBinding: FragmentEntryDetailsBinding? = null
    private val binding get() = entryDetailsBinding!!

    private lateinit var passwordViewModel: PasswordViewModel
    private lateinit var clipboardManager: ClipboardManager
    private lateinit var password: String
    private var errorMessage: String = ""

    private val promptManager = MainActivity().promptManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        entryDetailsBinding = FragmentEntryDetailsBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.siteNameTv.text = requireArguments().getString("siteName")
        binding.siteUrlTv.text = requireArguments().getString("siteUrl")
        binding.usernameTv.text = requireArguments().getString("username")
        binding.emailTv.text = requireArguments().getString("email")
        val encryptedPassword = requireArguments().getString("password")
        password = EncryptDecrypt().decrypt(encryptedPassword!!)

        clipboardManager = context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        binding.backBtn.setOnClickListener {
            it.findNavController().popBackStack()
        }



        binding.showPasswordBtn.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                showBiometricPrompt("Show Password", "Use your biometric to show password")

            } else{
                binding.passwordTv.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
                binding.passwordTv.transformationMethod = PasswordTransformationMethod()
                binding.passwordTv.text = "password"
            }
        }


        binding.editBtn.setOnClickListener {
            it.findNavController().navigate(R.id.action_entryDetailsFragment_to_editEntryFragment,
                Bundle().apply {
                    putLong("id", requireArguments().getLong("id"))
                    putString("siteName", binding.siteNameTv.text.toString())
                    putString("siteUrl", binding.siteUrlTv.text.toString())
                    putString("username", binding.usernameTv.text.toString())
                    putString("email", binding.emailTv.text.toString())
                    putString("password", password)
                })
        }

        binding.copyUrlBtn.setOnClickListener {
            clipboardManager.setPrimaryClip(ClipData.newPlainText("LINK", binding.siteUrlTv.text
                .toString()))
            Toast.makeText(requireContext(), "URL Copied!", Toast.LENGTH_SHORT).show()
        }

        binding.copyUsernameBtn.setOnClickListener {
            clipboardManager.setPrimaryClip(ClipData.newPlainText("USERNAME", binding.usernameTv
                .text
                .toString()))
            Toast.makeText(requireContext(), "Username Copied!", Toast.LENGTH_SHORT).show()
        }

        binding.copyEmailBtn.setOnClickListener {
            clipboardManager.setPrimaryClip(ClipData.newPlainText("EMAIL", binding.emailTv.text
                .toString()))
            Toast.makeText(requireContext(), "Email Copied!", Toast.LENGTH_SHORT).show()
        }

        binding.copyPwBtn.setOnClickListener {
            clipboardManager.setPrimaryClip(ClipData.newPlainText("EMAIL", password))
            Toast.makeText(requireContext(), "Password Copied!", Toast.LENGTH_SHORT).show()
        }

    }

    fun showBiometricPrompt(
        title: String,
        description: String
    ){
        val manager = BiometricManager.from(requireContext())
        val authenticators = if(Build.VERSION.SDK_INT >= 30) {
            BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.BIOMETRIC_WEAK or BiometricManager.Authenticators.DEVICE_CREDENTIAL
        } else BiometricManager.Authenticators.BIOMETRIC_STRONG

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(title)
            .setDescription(description)
            .setAllowedAuthenticators(authenticators)
            .setConfirmationRequired(false)

        if(Build.VERSION.SDK_INT < 30){
            promptInfo.setNegativeButtonText("Cancel")
        }

        when(manager.canAuthenticate(authenticators)) {
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                errorMessage = "Biometric hardware is not working"
                return
            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                errorMessage = "Biometric feature is not available"
                return
            }
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                if(Build.VERSION.SDK_INT >= 30){

                    val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                        putExtra(
                            Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                            BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL
                        )
                    }
                    activity?.startActivity(enrollIntent)
                }

                return
            }
            else -> Unit
        }

        val prompt = BiometricPrompt(
            requireActivity(), object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    errorMessage = errString.toString()
                    Toast.makeText(activity, "Authentication Error", Toast.LENGTH_SHORT).show()
                    Log.d("TAG", "onAuthenticationError: $errString")
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    binding.passwordTv.transformationMethod = null
                    binding.passwordTv.inputType = InputType.TYPE_CLASS_TEXT
                    binding.passwordTv.text = password
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(activity, "Authentication Failed", Toast.LENGTH_SHORT).show()
                }
            }
        )

        prompt.authenticate(promptInfo.build())

    }

}
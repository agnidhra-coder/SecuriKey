package com.example.securikey.fragments

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
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
                binding.passwordTv.transformationMethod = null
                binding.passwordTv.inputType = InputType.TYPE_CLASS_TEXT
                binding.passwordTv.text = password
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

}
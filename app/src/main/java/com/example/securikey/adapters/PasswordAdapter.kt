package com.example.securikey.adapters

import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.securikey.DiffUtil_Callback
import com.example.securikey.databinding.EachEntryBinding
import com.example.securikey.room.Password
import com.example.securikey.R
import com.example.securikey.crypto.EncryptDecrypt
import com.example.securikey.viewmodel.PasswordViewModel

class PasswordAdapter() : RecyclerView
    .Adapter<PasswordAdapter.PasswordViewHolder>() {
    private lateinit var clipboardManager: ClipboardManager
    lateinit var currentPW : Password
    lateinit var context: Context
    private lateinit var passwordViewModel: PasswordViewModel
    private var password = listOf<Password>()
    private lateinit var activity: AppCompatActivity
    private lateinit var owner: LifecycleOwner
    private var mList = ArrayList<Password>()

    private var errorMessage = ""

    constructor(context: Context, passwordViewModel: PasswordViewModel, activity:
    AppCompatActivity, owner: LifecycleOwner) : this() {
        this.context = context
        this.passwordViewModel = passwordViewModel
        this.activity = activity
        this.owner = owner
    }

    class PasswordViewHolder(val itemBinding: EachEntryBinding) : RecyclerView.ViewHolder
        (itemBinding.root){
        }


    private val differCallback = object : DiffUtil.ItemCallback<Password>(){
        override fun areItemsTheSame(oldItem: Password, newItem: Password): Boolean {
            return oldItem.id == newItem.id &&
                    oldItem.siteUrl == newItem.siteUrl
        }

        override fun areContentsTheSame(oldItem: Password, newItem: Password): Boolean {
            return oldItem == newItem
        }
    }

//    val diffResult = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
//        override fun getOldListSize(): Int = oldData.size
//        override fun getNewListSize(): Int = newData.size
//
//        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
//            return oldData[oldItemPosition].key == newData[newItemPosition].key             }
//
//        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
//            return oldData[oldItemPosition] == newData[newItemPosition]
//        }
//    })
//
//    diffResult.dispatchUpdatesTo(this)

    val differ = AsyncListDiffer(this, differCallback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PasswordViewHolder {
        return PasswordViewHolder(EachEntryBinding.inflate(LayoutInflater.from(parent.context),
            parent, false))
    }

    override fun getItemCount(): Int {
        return password.size
    }


    override fun onBindViewHolder(holder: PasswordViewHolder, position: Int) {
        currentPW = password[position]

        holder.itemBinding.websiteName.text = currentPW.siteName
        holder.itemBinding.email.text = currentPW.email
        holder.itemBinding.createdDate.text = currentPW.createdDate.toString()

        holder.itemView.setOnClickListener {
            val element = password[position]
            Log.i("element", "$element")
            Log.i("index", "$position")
            Bundle().clear()
            it.findNavController().navigate(R.id
                .action_homeFragment_to_entryDetailsFragment,
                Bundle().apply {
                    putLong("id", element.id)
                    putString("siteName", element.siteName)
                    putString("siteUrl", element.siteUrl)
                    putString("username", element.username)
                    putString("email", element.email)
                    putString("password", element.password)
                })
        }

        holder.itemBinding.moreOptions.setOnClickListener {
            val element = password[position]
            showPopupMenu(it, element, owner)
        }

    }

    internal fun setPasswords(newPasswords: List<Password>) {
        val diffCallback = DiffUtil_Callback(password, newPasswords)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        password = newPasswords
        diffResult.dispatchUpdatesTo(this)
    }

    private fun showPopupMenu(view: View?, currentPw : Password, owner: LifecycleOwner){
        val popupMenu = PopupMenu(context, view)
        val inflater = popupMenu.menuInflater
        inflater.inflate(R.menu.popup_menu, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { item -> onPopupMenuClick(item, currentPw, view,
            owner) }
        popupMenu.show()
    }

    private fun onPopupMenuClick(item: MenuItem?, currentPw: Password, view: View?, owner: LifecycleOwner):
            Boolean {
        when(item?.itemId){
            R.id.copyUsername -> copyUsername(currentPw.username)
            R.id.copyEmail ->copyEmail(currentPw.email)
            R.id.copyPw -> copyPassword(currentPw.password)
            R.id.deleteEntry ->deleteEntry(currentPw, view)

        }
        return true
    }

    private fun copyUsername(username : String) {
        clipboardManager.setPrimaryClip(ClipData.newPlainText("USERNAME", username))
        Toast.makeText(context, "Username Copied", Toast.LENGTH_SHORT).show()
    }

    private fun copyPassword(pw : String) {
        showBiometricPrompt("Copy Password", "Use biometric to copy password", pw)
    }

    private fun copyEmail(email : String) {
        clipboardManager.setPrimaryClip(ClipData.newPlainText("EMAIL", email))
        Toast.makeText(context, "Email Copied", Toast.LENGTH_SHORT).show()
    }

    private fun deleteEntry(currentPw: Password, view: View?) {
        val dialog = Dialog(context).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(R.layout.delete_dialog)
            window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

            setCancelable(true)
            setCanceledOnTouchOutside(true)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        dialog.show()
        dialog.findViewById<Button>(R.id.deleteBtn).setOnClickListener {
            passwordViewModel.deletePW(currentPw)
//            passwordViewModel.getAllPW().observe(owner) {
//                for (i in it) {
//                    mList.add(i)
//                }
//            }
//
//            differ.submitList(mList)

            Toast.makeText(context, "Entry Deleted", Toast.LENGTH_SHORT).show()
            view?.findNavController()?.popBackStack(R.id.homeFragment, false)
            dialog.dismiss()
        }

        dialog.findViewById<Button>(R.id.cancelBtn).setOnClickListener {
            dialog.dismiss()
        }

//        AlertDialog.Builder(context).apply {
//            setTitle("Delete Entry")
//            setMessage("Are you sure you want to delete this entry?")
//            setPositiveButton("Delete"){_, _ ->
//                passwordViewModel.deletePW(currentPw)
//                Toast.makeText(context, "Entry Deleted", Toast.LENGTH_SHORT).show()
//                view?.findNavController()?.popBackStack(R.id.homeFragment, false)
//            }
//            setNegativeButton("Cancel", null)
//        }.create().show()
    }

    fun showBiometricPrompt(
        title: String,
        description: String,
        pw: String
    ){
        val manager = BiometricManager.from(activity)
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
                Log.i("Biometric", errorMessage)
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
                    activity.startActivity(enrollIntent)
                }

                return
            }
            else -> Unit
        }

        val prompt = BiometricPrompt(
            activity, object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    errorMessage = errString.toString()
                    Toast.makeText(context, "Authentication Error", Toast.LENGTH_SHORT).show()
                    Log.d("TAG", "onAuthenticationError: $errString")
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    val decryptedPw = EncryptDecrypt().decrypt(pw)
                    clipboardManager.setPrimaryClip(ClipData.newPlainText("PASSWORD", decryptedPw))
                    Toast.makeText(context, "Password Copied", Toast.LENGTH_SHORT).show()
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
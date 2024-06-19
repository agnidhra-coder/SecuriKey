package com.example.securikey.adapters

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.securikey.databinding.EachEntryBinding
import com.example.securikey.room.Password
import com.example.securikey.R
import com.example.securikey.viewmodel.PasswordViewModel

class PasswordAdapter() : RecyclerView
    .Adapter<PasswordAdapter.PasswordViewHolder>() {
        private lateinit var clipboardManager: ClipboardManager
        lateinit var currentPW : Password
        lateinit var context: Context
        private lateinit var passwordViewModel: PasswordViewModel

        constructor(context: Context, passwordViewModel: PasswordViewModel) : this() {
            this.context = context
            this.passwordViewModel = passwordViewModel
        }

    class PasswordViewHolder(val itemBinding: EachEntryBinding) : RecyclerView.ViewHolder
        (itemBinding.root){}


    private val differCallback = object : DiffUtil.ItemCallback<Password>(){
        override fun areItemsTheSame(oldItem: Password, newItem: Password): Boolean {
            return oldItem.id == newItem.id &&
                    oldItem.siteUrl == newItem.siteUrl
        }

        override fun areContentsTheSame(oldItem: Password, newItem: Password): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PasswordViewHolder {
        return PasswordViewHolder(EachEntryBinding.inflate(LayoutInflater.from(parent.context),
            parent, false))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


    override fun onBindViewHolder(holder: PasswordViewHolder, position: Int) {
        currentPW = differ.currentList[position]

        holder.itemBinding.websiteName.text = currentPW.siteName
        holder.itemBinding.email.text = currentPW.email
        holder.itemBinding.createdDate.text = currentPW.createdDate.toString()

        holder.itemView.setOnClickListener {
            val element = differ.currentList[position]
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
            val index = position
            val element = differ.currentList[index]
            showPopupMenu(it, element, position)
        }

    }

    private fun showPopupMenu(view: View?, currentPw : Password, position: Int){
        val popupMenu = PopupMenu(context, view)
        val inflater = popupMenu.menuInflater
        inflater.inflate(R.menu.popup_menu, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { item -> onPopupMenuClick(item, currentPw, view, position) }
        popupMenu.show()
    }

    private fun onPopupMenuClick(item: MenuItem?, currentPw: Password, view: View?, position: Int): Boolean {
        when(item?.itemId){
            R.id.copyUsername -> copyUsername(currentPw.username)
            R.id.copyEmail ->copyEmail(currentPw.email)
            R.id.copyPw -> copyPassword(currentPw.password)
            R.id.deleteEntry ->deleteEntry(currentPw, view, position)

        }
        return true
    }

    private fun copyUsername(username : String) {
        clipboardManager.setPrimaryClip(ClipData.newPlainText("USERNAME", username))
    }

    private fun copyPassword(pw : String) {
        TODO("Not yet implemented")
    }

    private fun copyEmail(email : String) {
        clipboardManager.setPrimaryClip(ClipData.newPlainText("EMAIL", email))
    }

    private fun deleteEntry(currentPw: Password, view: View?, position: Int) {
        AlertDialog.Builder(context).apply {
            setTitle("Delete Entry")
            setMessage("Are you sure you want to delete this entry?")
            setPositiveButton("Delete"){_, _ ->
                passwordViewModel.deletePW(currentPw)
                Toast.makeText(context, "Entry Deleted", Toast.LENGTH_SHORT).show()
                view?.findNavController()?.popBackStack(R.id.homeFragment, false)
            }
            setNegativeButton("Cancel", null)
        }.create().show()
//        notifyItemRemoved(position)
    }



}
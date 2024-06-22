package com.example.securikey.adapters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.securikey.R
import com.example.securikey.crypto.EncryptDecrypt
import com.example.securikey.databinding.ShareEachEntryBinding
import com.example.securikey.room.Password

class SharePasswordAdapter : RecyclerView.Adapter<SharePasswordAdapter.SharePasswordViewHolder>() {
    class SharePasswordViewHolder(val itemBinding: ShareEachEntryBinding) : RecyclerView.ViewHolder
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
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SharePasswordViewHolder {
        return SharePasswordViewHolder(ShareEachEntryBinding.inflate(LayoutInflater.from(parent
            .context), parent, false))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: SharePasswordViewHolder, position: Int) {
        val currentPW = differ.currentList[position]

        holder.itemBinding.websiteName.text = currentPW.siteName
        holder.itemBinding.email.text = currentPW.email

        holder.itemView.setOnClickListener{
            val current = differ.currentList[position]
            val decryptedPassword = EncryptDecrypt().decrypt(current.password)
            it.findNavController().navigate(R.id.action_shareFragment_to_passwordQRFragment, Bundle().apply {
                putString("password", decryptedPassword)
                putString("siteName", current.siteName)
            })
        }
    }
}
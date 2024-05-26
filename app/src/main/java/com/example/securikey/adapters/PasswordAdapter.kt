package com.example.securikey.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.securikey.databinding.EachEntryBinding
//import com.example.securikey.fragments.HomeFragmentDirections
import com.example.securikey.room.Password

class PasswordAdapter : RecyclerView.Adapter<PasswordAdapter.PasswordViewHolder>() {
    class PasswordViewHolder(val itemBinding: EachEntryBinding) : RecyclerView.ViewHolder
        (itemBinding.root){

    }

    private val differCallback = object : DiffUtil.ItemCallback<Password>(){
        override fun areItemsTheSame(oldItem: Password, newItem: Password): Boolean {
            return oldItem.id == newItem.id &&
                    oldItem.siteName == newItem.siteName &&
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
        val currentPW = differ.currentList[position]

        holder.itemBinding.websiteName.text = currentPW.siteName
        holder.itemBinding.email.text = currentPW.email

//        holder.itemView.setOnClickListener {
//            val direction = .actionHomeFragmentToEditEntryFragment(currentPW)
//            it.findNavController().navigate(direction)
//        }

    }

}
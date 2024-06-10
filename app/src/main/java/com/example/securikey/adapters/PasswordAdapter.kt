package com.example.securikey.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.securikey.databinding.EachEntryBinding
//import com.example.securikey.fragments.HomeFragmentDirections
import com.example.securikey.room.Password

class PasswordAdapter(val context: Context) : RecyclerView.Adapter<PasswordAdapter
    .PasswordViewHolder>() {
    class PasswordViewHolder(val itemBinding: EachEntryBinding) : RecyclerView.ViewHolder
        (itemBinding.root){}

    private val passwords = ArrayList<Password>()

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

    fun submitList(courses: MutableList<Password>) {
        courses.clear()
        courses.addAll(courses)
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
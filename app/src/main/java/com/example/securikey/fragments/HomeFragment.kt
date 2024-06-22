package com.example.securikey.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.securikey.MainActivity
import com.example.securikey.R
import com.example.securikey.adapters.PasswordAdapter
import com.example.securikey.databinding.FragmentHomeBinding
import com.example.securikey.room.Password
import com.example.securikey.viewmodel.PasswordViewModel
import java.util.Locale

class HomeFragment : Fragment(R.layout.fragment_home) {
    private var homeBinding: FragmentHomeBinding? = null
    private val binding get() = homeBinding!!

    private lateinit var passwordsViewModel: PasswordViewModel
    private lateinit var passwordAdapter: PasswordAdapter

    private var mList = ArrayList<Password>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeBinding = FragmentHomeBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        passwordsViewModel = (activity as MainActivity).passwordViewModel
        setupRecyclerView()
        setupSearchView()
    }

    private fun setupRecyclerView() {
        passwordAdapter = PasswordAdapter(requireContext(), passwordsViewModel, MainActivity(),
            viewLifecycleOwner)
        binding.rvPasswords.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = passwordAdapter
        }

        activity?.let {
            passwordsViewModel.getAllPW().observe(viewLifecycleOwner) { passwords ->
                passwords?.let { passwordAdapter.setPasswords(it) }
                for(i in passwords){
                    mList.add(i)
                }
            }
        }
    }

    private fun setupSearchView(){
        binding.entrySearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    filterPasswords(newText)
                }
                return true
            }
        })
    }

    private fun filterPasswords(query: String?) {

        if(query != null){
            val filteredList = ArrayList<Password>()
            for(i in mList){
                if(i.siteName.lowercase(Locale.ROOT).contains(query) ||
                    i.siteUrl.lowercase(Locale.ROOT).contains(query)){
                    filteredList.add(i)
                }
            }
            if(filteredList.isEmpty()){
                Toast.makeText(requireContext(), "No Data Found", Toast.LENGTH_SHORT).show()
            }else{
                passwordAdapter.setPasswords(filteredList)
            }
        }


    }
}
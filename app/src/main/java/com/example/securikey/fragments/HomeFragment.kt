package com.example.securikey.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.securikey.MainActivity
import com.example.securikey.R
import com.example.securikey.adapters.PasswordAdapter
import com.example.securikey.databinding.FragmentHomeBinding
import com.example.securikey.viewmodel.PasswordViewModel

class HomeFragment : Fragment(R.layout.fragment_home) {
    private var homeBinding: FragmentHomeBinding? = null
    private val binding get() = homeBinding!!

    private lateinit var passwordsViewModel: PasswordViewModel
    private lateinit var passwordAdapter: PasswordAdapter


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
    }

    private fun setupRecyclerView() {
        passwordAdapter = PasswordAdapter(requireContext())
        binding.rvPasswords.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
            adapter = passwordAdapter
        }

        activity?.let {
            passwordsViewModel.getAllPW().observe(viewLifecycleOwner) { passwords ->
                Log.i("HomeRecyclerView", "$passwords")
                passwordAdapter.differ.submitList(passwords)
//                passwordAdapter.submitList(passwords)
                passwordAdapter.notifyDataSetChanged()
            }
        }

    }
}
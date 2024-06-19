package com.example.securikey.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.securikey.MainActivity
import com.example.securikey.R
import com.example.securikey.adapters.SharePasswordAdapter
import com.example.securikey.databinding.FragmentShareBinding
import com.example.securikey.viewmodel.PasswordViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ShareFragment : Fragment() {
    private var shareBinding: FragmentShareBinding? = null
    private val binding get() = shareBinding!!
    private lateinit var sharePasswordAdapter: SharePasswordAdapter
    private lateinit var passwordsViewModel: PasswordViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        shareBinding = FragmentShareBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        passwordsViewModel = (activity as MainActivity).passwordViewModel
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        sharePasswordAdapter = SharePasswordAdapter()
        binding.rvShare.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = sharePasswordAdapter
        }

        activity?.let {
            passwordsViewModel.getAllPW().observe(viewLifecycleOwner) { passwords ->
                sharePasswordAdapter.differ.submitList(passwords)
            }
        }
    }

}
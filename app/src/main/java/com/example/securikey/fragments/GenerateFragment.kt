package com.example.securikey.fragments

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import com.example.securikey.R
import com.example.securikey.databinding.FragmentGenerateBinding
import java.util.concurrent.ThreadLocalRandom


class GenerateFragment : Fragment() {
    private var generateFragmentBinding: FragmentGenerateBinding? = null
    private val binding get() = generateFragmentBinding!!
    private lateinit var clipboardManager: ClipboardManager
    private lateinit var password : String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        generateFragmentBinding = FragmentGenerateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        clipboardManager = context?.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager


        setPasswordEt()

        binding.reloadBtn.setOnClickListener {
            setPasswordEt()
        }

        binding.lengthSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                binding.counterTxt.setText(progress.toString())

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                setPasswordEt()
            }
        })

        binding.letterCB.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                setPasswordEt()
                binding.digitCB.isEnabled = true
            } else{
                binding.digitCB.isChecked = true
                binding.digitCB.isEnabled = false
                setPasswordEt()
            }
        }

        binding.digitCB.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                setPasswordEt()
                binding.letterCB.isEnabled = true
            } else{

                binding.letterCB.isChecked = true
                binding.letterCB.isEnabled = false
                setPasswordEt()
            }
        }

        binding.symbolCB.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                setPasswordEt()
            } else{
                setPasswordEt()
            }
        }

        binding.reloadBtn.setOnClickListener {
            setPasswordEt()
        }

        binding.copyBtn.setOnClickListener {
            clipboardManager.setPrimaryClip(ClipData.newPlainText("PASSWORD", binding.passwordEt.text.toString()))
            Toast.makeText(requireContext(), "Copied!", Toast.LENGTH_SHORT).show()
        }

        password = binding.passwordEt.text.toString()
    }

    private fun generate(length: Int, letters: Boolean, digits: Boolean, symbols: Boolean) : String{
        val charset = (('a'..'z') + ('A'..'Z') + ('0'..'9') + listOf(
            '!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '-', '_', '=', '+',
            '[', ']', '{', '}', ';', ':', '\'', '\"', ',', '.', '<', '>', '/', '?'
        )).toMutableList()

        if(!symbols){
            charset -= listOf(
                '!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '-', '_', '=', '+',
                '[', ']', '{', '}', ';', ':', '\'', '\"', ',', '.', '<', '>', '/', '?'
            )
        }

        if(!letters){
            charset -=  ('a'..'z')
            charset -=  ('A'..'Z')
        }

        else if(!digits){
            charset -= ('0'..'9')
        }


        return (1..length)
            .map { charset.random() }
            .joinToString("")
    }

    fun setPasswordEt(){
        binding.passwordEt.setText(generate(binding.lengthSeekBar.progress, binding
            .letterCB.isChecked, binding.digitCB.isChecked, binding.symbolCB.isChecked))
    }

    override fun onStart() {
        super.onStart()
        Log.i("onStart", "Called")
    }

    override fun onResume() {
        super.onResume()
        Log.i("onResume", "Called")
    }
    override fun onPause() {
        super.onPause()
        Log.i("onPause", "Called")
    }
    override fun onStop() {
        super.onStop()
        Log.i("onStop", "Called")
    }
    override fun onDestroy() {
        super.onDestroy()
        Log.i("onDestroy", "Called")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.i("onDestroyView", "Called")
    }

    override fun onDetach() {
        super.onDetach()
        Log.i("onDetach", "Called")
    }


}
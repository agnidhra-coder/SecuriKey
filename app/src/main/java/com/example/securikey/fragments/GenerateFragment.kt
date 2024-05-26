package com.example.securikey.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import com.example.securikey.R
import com.example.securikey.databinding.FragmentGenerateBinding
import java.util.concurrent.ThreadLocalRandom


class GenerateFragment : Fragment() {
    private var generateFragmentBinding: FragmentGenerateBinding? = null
    private val binding get() = generateFragmentBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        generateFragmentBinding = FragmentGenerateBinding.inflate(inflater, container, false)
        return inflater.inflate(R.layout.fragment_generate, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setPasswordEt()

        binding.reloadBtn.setOnClickListener {
            setPasswordEt()
        }

        binding.lengthSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                binding.counterTxt.text = progress.toString()
                setPasswordEt()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                TODO("Not yet implemented")
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                TODO("Not yet implemented")
            }
        })
    }

    fun generate(length: Int, letters: Boolean, digits: Boolean, symbols: Boolean) : String{
        var MIN_CODE = 33;
        var MAX_CODE = 126;
        val builder = StringBuilder()

        if(!letters){
            MAX_CODE = 64
            binding.digitCB.isChecked = true
            binding.digitCB.isEnabled = false
        }

        if(!digits){
            MIN_CODE = 58
            binding.letterCB.isChecked = true
            binding.letterCB.isEnabled = false
        }
        

        for (i in 0..<length){
            builder.append(ThreadLocalRandom.current().nextInt(MIN_CODE, MAX_CODE+1).toString())
        }

        return builder.toString()
    }

    fun setPasswordEt(){
        binding.passwordEt.setText(generate(binding.lengthSeekBar.progress, binding
            .letterCB.isChecked, binding.digitCB.isChecked, binding.symbolCB.isChecked))
    }

}
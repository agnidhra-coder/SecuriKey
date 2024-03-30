package com.example.securikey

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.securikey.databinding.ActivityMasterPasswordBinding

class MasterPassword : AppCompatActivity() {
    private lateinit var binding: ActivityMasterPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMasterPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}
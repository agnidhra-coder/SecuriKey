package com.example.securikey

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.fragment.app.Fragment
import com.example.securikey.databinding.ActivityMainBinding
import com.example.securikey.fragments.GenerateFragment
import com.example.securikey.fragments.HomeFragment
import com.example.securikey.fragments.ProfileFragment
import com.example.securikey.fragments.ShareFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var cryptoManager: CryptoManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        binding.bottomNavView.background = null
        binding.bottomNavView.menu.getItem(2).isEnabled = false

        cryptoManager = CryptoManager()

        replaceWithFragment(HomeFragment())

        binding.bottomNavView.setOnItemSelectedListener{
            when(it.itemId){
                R.id.home -> replaceWithFragment(HomeFragment())
                R.id.generate -> replaceWithFragment(GenerateFragment())
                R.id.share -> replaceWithFragment(ShareFragment())
                R.id.profile -> replaceWithFragment(ProfileFragment())
                else -> {}
            }
            true
        }


    }

    private fun replaceWithFragment(fragment: Fragment){
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, fragment)
        fragmentTransaction.commit()
    }
}
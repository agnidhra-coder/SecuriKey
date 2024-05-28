package com.example.securikey

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.securikey.databinding.ActivityMainBinding
import com.example.securikey.fragments.AddPasswordFragment
import com.example.securikey.fragments.GenerateFragment
import com.example.securikey.fragments.HomeFragment
import com.example.securikey.fragments.ProfileFragment
import com.example.securikey.fragments.ShareFragment
import com.example.securikey.repository.PasswordRepository
import com.example.securikey.repository.PasswordViewModelFactory
import com.example.securikey.room.PasswordDatabase
import com.example.securikey.viewmodel.PasswordViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var cryptoManager: CryptoManager
    lateinit var passwordViewModel: PasswordViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        setupViewModel()

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

        binding.addPwFAB.setOnClickListener{
            val addPasswordFragment = AddPasswordFragment()
            addPasswordFragment.show(supportFragmentManager, "AddPasswordFragment")
        }


    }

    private fun replaceWithFragment(fragment: Fragment){
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, fragment)
        fragmentTransaction.commit()
    }

    private fun setupViewModel(){
        val passwordRepository = PasswordRepository(PasswordDatabase.getDatabase(this))
        val viewModelFactory = PasswordViewModelFactory(application, passwordRepository)
        passwordViewModel = ViewModelProvider(this, viewModelFactory)[PasswordViewModel::class.java]
    }
}
package com.example.securikey

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.navigation.ui.setupWithNavController
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
    lateinit var passwordViewModel: PasswordViewModel
    val promptManager by lazy {
        BiometricPromptManager(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        setupViewModel()

//        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
//        val navController = navHostFragment.navController
        binding.bottomNavView.background = null
        binding.bottomNavView.menu.getItem(2).isEnabled = false

        replaceWithFragment(HomeFragment(), "home")

        binding.bottomNavView.setOnItemSelectedListener{
            when(it.itemId){
                R.id.homeFragment -> replaceWithFragment(HomeFragment(), "home")
                R.id.generateFragment -> replaceWithFragment(GenerateFragment(), "generate")
                R.id.shareFragment -> replaceWithFragment(ShareFragment(), "share")
                R.id.profileFragment -> replaceWithFragment(ProfileFragment(), "profile")
                else -> {}
            }
            true
        }


//        setupWithNavController(binding.bottomNavView, navController)
//        binding.bottomNavView.setupWithNavController(navController)

        binding.addPwFAB.setOnClickListener{
            val addPasswordFragment = AddPasswordFragment()
            addPasswordFragment.show(supportFragmentManager, "AddPasswordFragment")
        }


    }

    private fun replaceWithFragment(fragment: Fragment, tag: String){
//        val fragmentTransaction = supportFragmentManager.beginTransaction()
//        fragmentTransaction.replace(R.id.fragment_container, fragment)
//        fragmentTransaction.commit()

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavView.setupWithNavController(navController)

//        val fragmentTransaction = supportFragmentManager.beginTransaction()
//
//        // Hide all fragments
//        supportFragmentManager.fragments.forEach { fragmentTransaction.hide(it) }
//
//        // Check if the fragment is already added to the fragment manager
//        if (supportFragmentManager.findFragmentByTag(tag) == null) {
//            fragmentTransaction.add(R.id.fragment_container, fragment, tag)
//        } else {
//            fragmentTransaction.show(fragment)
//        }
//
//        fragmentTransaction.commit()

    }

    private fun setupViewModel(){
        val passwordRepository = PasswordRepository(PasswordDatabase.getDatabase(this))
        val viewModelFactory = PasswordViewModelFactory(application, passwordRepository)
        passwordViewModel = ViewModelProvider(this, viewModelFactory)[PasswordViewModel::class.java]
    }
}
package com.example.securikey.repository

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.securikey.viewmodel.PasswordViewModel

class PasswordViewModelFactory(val app: Application, private val passwordRepository:
PasswordRepository) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PasswordViewModel(app, passwordRepository) as T
    }
}
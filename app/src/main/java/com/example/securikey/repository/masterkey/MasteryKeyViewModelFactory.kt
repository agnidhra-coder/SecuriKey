package com.example.securikey.repository.masterkey

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.securikey.viewmodel.MasterKeyViewModel

class MasterKeyViewModelFactory(val app: Application, private val masterKeyRepository:
MasterKeyRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MasterKeyViewModel(app, masterKeyRepository) as T
    }
}
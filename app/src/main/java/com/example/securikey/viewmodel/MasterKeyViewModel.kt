package com.example.securikey.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.securikey.repository.masterkey.MasterKeyRepository
import com.example.securikey.room.masterkey.MasterKey
import kotlinx.coroutines.launch

class MasterKeyViewModel(val app: Application, private val masterKeyRepository:
MasterKeyRepository) : AndroidViewModel(app) {

    fun insertmKey(mKey: MasterKey) = viewModelScope.launch {
        masterKeyRepository.insertmKey(mKey)
    }

    fun updatemKey(mKey: MasterKey) = viewModelScope.launch {
        masterKeyRepository.updatemKey(mKey)
    }

    fun getAllmKey() = masterKeyRepository.getAllmKey()
}
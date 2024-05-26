package com.example.securikey.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.securikey.repository.PasswordRepository
import com.example.securikey.room.Password
import kotlinx.coroutines.launch

class PasswordViewModel(val app: Application, private val passwordRepository: PasswordRepository) :
    AndroidViewModel(app) {

    fun insertPW(pw: Password) = viewModelScope.launch {
        passwordRepository.insertPW(pw)
    }
    fun updatePW(pw: Password) = viewModelScope.launch {
        passwordRepository.updatePW(pw)
    }
    fun deletePW(pw: Password) = viewModelScope.launch {
        passwordRepository.deletePW(pw)
    }

    fun getAllPW() = passwordRepository.getAllPW()

    fun searchPW(query: String?) = passwordRepository.searchPW(query)
}
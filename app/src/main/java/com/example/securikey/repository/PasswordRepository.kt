package com.example.securikey.repository

import com.example.securikey.room.Password
import com.example.securikey.room.PasswordDatabase

class PasswordRepository(private val db: PasswordDatabase) {
    suspend fun insertPW(pw: Password) = db.passwordDao().insertPW(pw)
    suspend fun updatePW(pw: Password) = db.passwordDao().updatePW(pw)
    suspend fun deletePW(pw: Password) = db.passwordDao().deletePW(pw)

    fun getAllPW() = db.passwordDao().getAllPW()

    fun searchPW(query: String?) = db.passwordDao().searchNote(query)
}
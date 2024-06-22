package com.example.securikey.repository.masterkey

import com.example.securikey.room.masterkey.MasterKey
import com.example.securikey.room.masterkey.MasterKeyDatabase

class MasterKeyRepository(private val db: MasterKeyDatabase) {
    suspend fun insertmKey(mKey: MasterKey) = db.masterKeyDao().insertmKey(mKey)

    suspend fun updatemKey(mKey: MasterKey) = db.masterKeyDao().updatemKey(mKey)

    fun getAllmKey() = db.masterKeyDao().getAllmKeys()

}
package com.example.securikey.room.masterkey

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface MasterKeyDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertmKey(mKey: MasterKey)

    @Update
    suspend fun updatemKey(mKey: MasterKey)

    @Query("SELECT * FROM MasterKeys")
    fun getAllmKeys() : LiveData<List<MasterKey>>
}
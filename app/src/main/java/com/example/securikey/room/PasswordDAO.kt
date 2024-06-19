package com.example.securikey.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert

@Dao
interface PasswordDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPW(password: Password)
    @Update
    suspend fun updatePW(password: Password)
    @Delete
    suspend fun deletePW(password: Password)

    @Query("SELECT * FROM Entries")
    fun getAllPW() : LiveData<List<Password>>

    @Query("SELECT * FROM Entries WHERE siteName LIKE :query OR siteUrl LIKE :query")
    fun searchPW(query: String?) : LiveData<List<Password>>
}
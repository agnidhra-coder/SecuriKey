package com.example.securikey.room.masterkey

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "MasterKeys")
@Parcelize
data class MasterKey(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val mKey: String
) : Parcelable
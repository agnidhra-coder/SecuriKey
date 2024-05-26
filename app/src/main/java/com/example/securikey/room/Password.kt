package com.example.securikey.room

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.Date


@Entity(tableName = "Entries")
@Parcelize
data class Password(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val siteName: String,
    val siteUrl: String,
    val email: String,
    val password: String,
    val createdDate: Date
): Parcelable

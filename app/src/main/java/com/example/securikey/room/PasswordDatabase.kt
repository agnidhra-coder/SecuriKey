package com.example.securikey.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Password::class], version = 1)
@TypeConverters(Convertors::class)
abstract class PasswordDatabase : RoomDatabase() {

    abstract fun passwordDao() : PasswordDAO

    companion object{
        @Volatile
        private var INSTANCE : PasswordDatabase? = null

        fun getDatabase(context: Context): PasswordDatabase{
            if(INSTANCE == null){
                synchronized(this){
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        PasswordDatabase::class.java,
                        "passwordDB").build()
                }
            }
            return INSTANCE!!
        }
    }
}
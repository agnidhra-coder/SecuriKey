package com.example.securikey.room.masterkey

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [MasterKey::class], version = 1)
abstract class MasterKeyDatabase : RoomDatabase(){

    abstract fun masterKeyDao(): MasterKeyDAO

    companion object{
        @Volatile
        private var INSTANCE: MasterKeyDatabase? = null

        fun getDatabase(context: Context): MasterKeyDatabase{
            if(INSTANCE == null){
                synchronized(this){
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        MasterKeyDatabase::class.java,
                        "master_key_database").build()
                }
            }
                return INSTANCE!!
        }
    }
}
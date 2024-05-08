package com.example.passwordmanagerapp

import android.app.Application
import androidx.room.Room
import com.example.passwordmanagerapp.room.AppDatabase


class ApplicationClass : Application() {

    companion object {
        lateinit var database: AppDatabase
    }

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "my_database"
        ).build()
    }
}
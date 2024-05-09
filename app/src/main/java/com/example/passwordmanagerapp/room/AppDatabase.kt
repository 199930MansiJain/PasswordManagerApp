package com.example.passwordmanagerapp.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.passwordmanagerapp.room.dao.PasswordDao
import com.example.passwordmanagerapp.room.entities.UserPasswordEntities

@Database(entities = [UserPasswordEntities::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): PasswordDao
}

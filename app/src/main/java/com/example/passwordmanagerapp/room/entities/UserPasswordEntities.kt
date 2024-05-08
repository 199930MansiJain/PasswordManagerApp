package com.example.passwordmanagerapp.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserPasswordEntities(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val accountName: String,
    val userName: String,
    val password: String,
    val emailId: String
)

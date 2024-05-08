package com.example.passwordmanagerapp.room.dao

import androidx.room.*
import com.example.passwordmanagerapp.room.entities.UserPasswordEntities

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserPasswordEntities)

    @Query("SELECT * FROM user")
    suspend fun getAllUsers(): List<UserPasswordEntities>

    @Update
    suspend fun updateUser(user: UserPasswordEntities)

    @Query("DELETE FROM user WHERE id = :userId")
    suspend fun deleteUserById(userId: Long)
}

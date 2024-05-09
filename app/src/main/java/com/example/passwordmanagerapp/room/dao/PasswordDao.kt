package com.example.passwordmanagerapp.room.dao

import androidx.room.*
import com.example.passwordmanagerapp.room.entities.UserPasswordEntities

@Dao
interface PasswordDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPassword(userPasswordEntities: UserPasswordEntities)

    @Query("SELECT * FROM passwordsTable")
    suspend fun getAllPasswords(): List<UserPasswordEntities>

    @Update
    suspend fun updatePassword(userPasswordEntities: UserPasswordEntities)

    @Query("DELETE FROM passwordsTable WHERE id = :passwordId")
    suspend fun deletePasswordById(passwordId: Long)
}

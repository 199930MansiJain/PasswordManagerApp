package com.example.passwordmanagerapp.room.entities

import android.util.Base64
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

@Entity(tableName = "passwordsTable")
data class UserPasswordEntities(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val accountName: String,
    val userNameOrEmail: String,
    val password: String,
)
{
    companion object {
        private const val SECRET_KEY = "YOUR_SECRET_KEY" // Replace with your own secret key
        private const val INITIALIZATION_VECTOR = "YOUR_INIT_VECTOR" // Replace with your own initialization vector
    }

    private val encryptedPassword: String
        get() = encrypt(password)

    fun getDecryptedPassword(): String {
        return decrypt(encryptedPassword)
    }

    private fun encrypt(value: String): String {
        val key = SecretKeySpec(MessageDigest.getInstance("SHA-256").digest(SECRET_KEY.toByteArray()), "AES")
        val iv = IvParameterSpec(INITIALIZATION_VECTOR.toByteArray())
        val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
        cipher.init(Cipher.ENCRYPT_MODE, key, iv)
        return Base64.encodeToString(cipher.doFinal(value.toByteArray()), Base64.DEFAULT)
    }

    private fun decrypt(value: String): String {
        val key = SecretKeySpec(MessageDigest.getInstance("SHA-256").digest(SECRET_KEY.toByteArray()), "AES")
        val iv = IvParameterSpec(INITIALIZATION_VECTOR.toByteArray())
        val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
        cipher.init(Cipher.DECRYPT_MODE, key, iv)
        return String(cipher.doFinal(Base64.decode(value, Base64.DEFAULT)))
    }
}

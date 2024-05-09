package com.example.passwordmanagerapp.utils


import android.util.Base64
import java.security.MessageDigest

import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec





import javax.crypto.spec.IvParameterSpec

object EncryptionUtils {
    private const val SECRET_KEY = "pass123" // Replace with your own secret key
    private const val INITIALIZATION_VECTOR = "0123456789abcdef"

    fun encrypt(value: String): String {
        val key = SecretKeySpec(MessageDigest.getInstance("SHA-256").digest(SECRET_KEY.toByteArray()), "AES")
        val iv = IvParameterSpec(INITIALIZATION_VECTOR.toByteArray())
        val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
        cipher.init(Cipher.ENCRYPT_MODE, key, iv)
        return Base64.encodeToString(cipher.doFinal(value.toByteArray()), Base64.DEFAULT)
    }

    fun decrypt(value: String): String {
        val key = SecretKeySpec(MessageDigest.getInstance("SHA-256").digest(SECRET_KEY.toByteArray()), "AES")
        val iv = IvParameterSpec(INITIALIZATION_VECTOR.toByteArray())
        val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
        cipher.init(Cipher.DECRYPT_MODE, key, iv)
        return String(cipher.doFinal(Base64.decode(value, Base64.DEFAULT)))
    }
}

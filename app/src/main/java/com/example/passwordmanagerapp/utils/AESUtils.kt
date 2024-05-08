package com.example.passwordmanagerapp.utils

import java.nio.charset.StandardCharsets
import java.security.Key
import java.security.KeyPair
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec


import java.security.KeyPairGenerator

object RSAUtil {
    private const val RSA_ALGORITHM = "RSA"

    fun generateKeyPair(): KeyPair {
        val keyPairGenerator = KeyPairGenerator.getInstance(RSA_ALGORITHM)
        keyPairGenerator.initialize(2048) // You can choose your key size here
        return keyPairGenerator.generateKeyPair()
    }

    fun encrypt(plainText: String, publicKey: java.security.PublicKey): String {
        val cipher = Cipher.getInstance(RSA_ALGORITHM)
        cipher.init(Cipher.ENCRYPT_MODE, publicKey)
        val encryptedBytes = cipher.doFinal(plainText.toByteArray())
        return android.util.Base64.encodeToString(encryptedBytes, android.util.Base64.DEFAULT)
    }

    fun decrypt(encryptedText: String, privateKey: java.security.PrivateKey): String {
        val cipher = Cipher.getInstance(RSA_ALGORITHM)
        cipher.init(Cipher.DECRYPT_MODE, privateKey)
        val decryptedBytes =
            cipher.doFinal(android.util.Base64.decode(encryptedText, android.util.Base64.DEFAULT))
        return String(decryptedBytes)
    }
}


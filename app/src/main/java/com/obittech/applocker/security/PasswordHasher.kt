package com.obittech.applocker.security

import android.util.Base64
import java.security.SecureRandom
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

object PasswordHasher {

    private const val iterations = 1000
    private const val keyLength = 256

    fun hashPin(pin: String, salt: ByteArray = generateSalt()): String {
        val spec = PBEKeySpec(pin.toCharArray(), salt, iterations, keyLength)
        val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
        val hash = factory.generateSecret(spec).encoded
        return Base64.encodeToString(salt + hash, Base64.NO_WRAP)
    }

    fun verifyPin(pin: String, stored: String): Boolean {
        val decoded = Base64.decode(stored, Base64.NO_WRAP)
        val salt = decoded.copyOfRange(0, 16)
        val hashOfInput = hashPin(pin, salt)
        return stored == hashOfInput
    }

    private fun generateSalt(): ByteArray {
        val salt = ByteArray(16)
        SecureRandom().nextBytes(salt)
        return salt
    }
}
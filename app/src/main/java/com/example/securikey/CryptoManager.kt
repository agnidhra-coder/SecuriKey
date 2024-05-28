package com.example.securikey

import android.security.keystore.KeyProperties
import android.util.Base64
import java.io.InputStream
import java.security.KeyStore
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec


class CryptoManager {
    private val keyStore = KeyStore.getInstance("AndroidKeyStore").apply {
        load(null)
    }
    private fun getKey() : SecretKey{
        val existingKey = keyStore.getKey("secret", null) as? KeyStore.SecretKeyEntry
        return existingKey?.secretKey ?: createKey()
    }

    private val key = "Hello7809hh"
    private val secretKeySpec = SecretKeySpec(key.toByteArray(), "AES")

    private val encryptCipher = Cipher.getInstance("AES/CBC/PKCS5Padding").apply {
        init(Cipher.ENCRYPT_MODE, getKey(), generateIv())
    }

    private fun getDecryptCipherForIv(iv: ByteArray): Cipher{
        return Cipher.getInstance(TRANSFORMATION).apply {
            init(Cipher.DECRYPT_MODE, getKey(), IvParameterSpec(iv))
        }
    }


    private fun createKey() : SecretKey{
//        return KeyGenerator.getInstance(ALGORITHM).apply {
//            init(
//                KeyGenParameterSpec.Builder(
//                    "secret",
//                    KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
//                )
//                    .setBlockModes(BLOCK_MODE)
//                    .setEncryptionPaddings(PADDING)
//                    .setUserAuthenticationRequired(true)
//                    .setRandomizedEncryptionRequired(true)
//                    .build()
//            )
//        }.generateKey()
        val keyGenerator = KeyGenerator.getInstance("AES")
        keyGenerator.init(128)
        val key = keyGenerator.generateKey()
        return key
    }

    fun generateIv(): IvParameterSpec {
        val iv = ByteArray(16)
        SecureRandom().nextBytes(iv)
        return IvParameterSpec(iv)
    }

    fun encrypt(string: String): String{
        val encryptedBytes = encryptCipher.doFinal(string.toByteArray())
        return Base64.encodeToString(encryptedBytes, Base64.DEFAULT)
    }

    fun decrypt(inputStream: InputStream): ByteArray{
        return inputStream.use {
            val ivSize = it.read()
            val iv = ByteArray(ivSize)
            it.read(iv)

            val encryptedBytesSize = it.read()
            val encryptedBytes = ByteArray(encryptedBytesSize)
            it.read(encryptedBytes)

            getDecryptCipherForIv(iv).doFinal(encryptedBytes)
        }
    }

    companion object{
        private const val ALGORITHM = KeyProperties.KEY_ALGORITHM_AES
        private const val BLOCK_MODE = KeyProperties.BLOCK_MODE_CBC
        private const val PADDING = KeyProperties.ENCRYPTION_PADDING_PKCS7
        private const val TRANSFORMATION = "AES/CBC/PKCS5Padding"

    }
}
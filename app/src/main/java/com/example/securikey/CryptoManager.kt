package com.example.securikey

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import android.util.Log
import java.io.InputStream
import java.security.KeyStore
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec


class CryptoManager {
    val keyAlias = "secret"

    private val keyStore = KeyStore.getInstance("AndroidKeyStore")

    private fun getKey() : SecretKey{
        keyStore.load(null)
        val secretKey : SecretKey = if (keyStore.containsAlias(keyAlias)) {
            val secretKeyEntry = keyStore.getEntry(keyAlias, null) as KeyStore.SecretKeyEntry
            secretKeyEntry.secretKey
        } else {
            createKey()
        }
        return secretKey
    }

    private val key = getKey()

    private val encryptCipher = Cipher.getInstance(TRANSFORMATION).apply {
        init(Cipher.ENCRYPT_MODE, key, IvParameterSpec(generateIv()))
    }

    private fun getEncryptCipherForIv(iv: IvParameterSpec): Cipher{
        return Cipher.getInstance(TRANSFORMATION).apply {
            init(Cipher.ENCRYPT_MODE, key, iv)
        }
    }


    private fun getDecryptCipherForIv(iv: ByteArray): Cipher{
        return Cipher.getInstance(TRANSFORMATION).apply {
            init(Cipher.DECRYPT_MODE, key, IvParameterSpec(iv))
        }
    }

    private val decryptCipher = Cipher.getInstance(TRANSFORMATION).apply {
        init(Cipher.DECRYPT_MODE, key, IvParameterSpec(generateIv()))
    }


    private fun createKey() : SecretKey{
        return KeyGenerator.getInstance(ALGORITHM, "AndroidKeyStore").apply {
            init(
                KeyGenParameterSpec.Builder(
                    keyAlias,
                    KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                )
                    .setBlockModes(BLOCK_MODE)
                    .setEncryptionPaddings(PADDING)
                    .setRandomizedEncryptionRequired(true)
                    .setUserAuthenticationRequired(false)
                    .build()
            )
        }.generateKey()
//        val keyGenerator = KeyGenerator.getInstance("AES")
//        keyGenerator.init(128)
//        val key = keyGenerator.generateKey()
//        return key
    }

    fun generateIv(): ByteArray {
        val iv = ByteArray(16)
        SecureRandom().nextBytes(iv)
//        Log.i("iv", ivParameterSpec.toString())
        return iv
    }

    fun encrypt(string: String, iv: ByteArray): String{
        val encryptedBytes = getEncryptCipherForIv(IvParameterSpec(iv)).doFinal(string
            .toByteArray())
        return Base64.encodeToString(encryptedBytes, Base64.DEFAULT)
    }

    fun decrypt(encryptedString: String, iv: ByteArray): String {
//        return inputStream.use {
//            val ivSize = it.read()
//            val iv = ByteArray(ivSize)
//            it.read(iv)
//
//            val encryptedBytesSize = it.read()
//            val encryptedBytes = ByteArray(encryptedBytesSize)
//            it.read(encryptedBytes)
//
//            getDecryptCipherForIv(iv).doFinal(encryptedBytes)
//        }
        val encryptedBytes = Base64.decode(encryptedString, Base64.DEFAULT)
        val decryptedBytes = getDecryptCipherForIv(iv).doFinal(encryptedBytes)
        return String(decryptedBytes)
    }

    companion object{
        private const val ALGORITHM = KeyProperties.KEY_ALGORITHM_AES
        private const val BLOCK_MODE = KeyProperties.BLOCK_MODE_CBC
        private const val PADDING = KeyProperties.ENCRYPTION_PADDING_PKCS7
        private const val TRANSFORMATION = "$ALGORITHM/$BLOCK_MODE/$PADDING"

    }
}
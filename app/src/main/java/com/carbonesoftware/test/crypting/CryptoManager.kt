package com.carbonesoftware.test.crypting

import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.annotation.RequiresApi
import java.io.InputStream
import java.io.OutputStream
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

@RequiresApi(Build.VERSION_CODES.M)
class CryptoManager {

    //Constantes
    companion object{
        private const val ALGORITHM = KeyProperties.KEY_ALGORITHM_AES
        private const val BLOCK_MODE = KeyProperties.BLOCK_MODE_CBC
        private const val PADDING = KeyProperties.ENCRYPTION_PADDING_PKCS7
        private const val TRANSFORMATION = "$ALGORITHM/$BLOCK_MODE/$PADDING"
    }

    //Obtener instancia de Keystore
    private val keyStore = KeyStore.getInstance("AndroidKeyStore").apply {
        load(null)
    }

    //Generador de keys
    private fun createKey(): SecretKey{
        //Especificamos el algoritmo que utilizará
        return KeyGenerator.getInstance(ALGORITHM).apply {
            init(
                KeyGenParameterSpec.Builder(
                    //Alias de la keystore
                    "my-keystore-alias",
                    //Los propositos de esta keystore
                    KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                )
                    .setBlockModes(BLOCK_MODE)//Seteamos el BLOCK_MODE
                    .setEncryptionPaddings(PADDING)//Seteamos el PADDING
                    .setUserAuthenticationRequired(false)
                    .setRandomizedEncryptionRequired(true)
                    .build()
            )
        }.generateKey()
    }

    //Obtener una key existente, sino existe, crear una nueva
    private fun getKey():SecretKey {
        val existingKey = this.keyStore
            .getEntry("my-keystore-alias", null) as? KeyStore.SecretKeyEntry

        return existingKey?.secretKey ?: createKey()
    }

    //Los ciphers definen como la app va a encriptar/desencriptar

    private val encryptCipher = Cipher.getInstance(TRANSFORMATION).apply {
        //Obtener incancia de cipher en modo ecriptacion
        //y que usará una key especifica (my-keystore-alias)
        init(Cipher.ENCRYPT_MODE, getKey())
    }

    //IV es initialization vector y se utiliza para crear el inicio
    //del texto encriptado
    private fun getDecryptCipherForIv(iv: ByteArray): Cipher {
        return Cipher.getInstance(TRANSFORMATION).apply {
            init(
                Cipher.DECRYPT_MODE,
                getKey(),
                IvParameterSpec(iv)
            )
        }
    }

    fun encrypt(bytes: ByteArray, outputStream: OutputStream): ByteArray{
        val encryptedBytes = encryptCipher.doFinal(bytes)
        outputStream.use {
            //Especificamos tamaño de array y luego el array porque
            //cuando estemos leyendo tenemos que saber el tamaño del array
            //para saber hasta donde leer (recordar que los arrays tienen tamaño fijo)
            it.write(encryptCipher.iv.size)
            it.write(encryptCipher.iv)
            it.write(encryptedBytes.size)
            it.write(encryptedBytes)
        }
        return encryptedBytes
    }

    fun decrypt(inputStream: InputStream): ByteArray{
        return inputStream.use {
            val ivSize = it.read()
            val iv = ByteArray(ivSize)
            it.read(iv)

            val encryptedByteSize = it.read()
            val encryptedBytes = ByteArray(encryptedByteSize)
            it.read(encryptedBytes)

            getDecryptCipherForIv(iv).doFinal(encryptedBytes)
        }
    }
}
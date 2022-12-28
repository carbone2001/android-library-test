package com.carbonesoftware.test.encryptDataStore

import androidx.datastore.core.Serializer
import com.carbonesoftware.test.crypting.CryptoManager
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

class DataToEncryptSerializer(
    private val cryptoManager: CryptoManager
    ) : Serializer<DataToEncryptClass>{

    override val defaultValue: DataToEncryptClass
        get() = DataToEncryptClass()

    override suspend fun readFrom(input: InputStream): DataToEncryptClass {
        val decryptedBytes =  cryptoManager.decrypt(input)
        return try{
            Json.decodeFromString(
                deserializer = DataToEncryptClass.serializer(),
                string = decryptedBytes.decodeToString()
            )
        }
        catch (e: SerializationException){
            e.printStackTrace()
            defaultValue
        }
    }

    override suspend fun writeTo(t: DataToEncryptClass, output: OutputStream) {
        cryptoManager.encrypt(
            bytes = Json.encodeToString(
                serializer = DataToEncryptClass.serializer(),
                value = t
            ).encodeToByteArray(),
            outputStream = output
        )
    }
}
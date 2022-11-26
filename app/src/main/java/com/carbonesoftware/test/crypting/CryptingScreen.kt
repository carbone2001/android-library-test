package com.carbonesoftware.test.crypting

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

@Composable
fun CryptingScreen(filesDir: File, cryptoManager: CryptoManager){
    var messageToEncrypt by remember {
        mutableStateOf("")
    }
    var messageToDecrypt by remember {
        mutableStateOf("")
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(32.dp)){
        TextField(
            value = messageToEncrypt,
            onValueChange = { messageToEncrypt = it },
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row() {
            Button(onClick = {
                //Convertimos el mensaje a un array de bytes
                val bytes = messageToEncrypt.encodeToByteArray()

                //Preparamos el archivo donde guardaremos el mensaje encriptado
                val file = File(filesDir, "encripted-message.txt")
                if(!file.exists()){
                    file.createNewFile()
                }
                val fos = FileOutputStream(file)

                //Guardamos la encriptacion y lo mostramos en messageToDecrypt
                messageToDecrypt = cryptoManager.encrypt(
                    bytes = bytes,
                    outputStream = fos
                ).decodeToString()
            }) {
                Text(text = "Encrypt")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(onClick = {
                val file = File(filesDir,"encripted-message.txt")
                messageToEncrypt = cryptoManager.decrypt(
                    inputStream = FileInputStream(file)
                ).decodeToString()
            }) {
                Text(text = "Decrypt")
            }
        }
        Text(text = messageToDecrypt)
    }
}
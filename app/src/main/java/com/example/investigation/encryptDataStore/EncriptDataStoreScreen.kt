package com.example.investigation.encryptDataStore

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Composable
fun EncriptDataStoreScreen(dataStore: DataStore<DataToEncryptClass>) {
    val coroutineScope = rememberCoroutineScope()
    var message by remember {
        mutableStateOf("")
    }

    var decriptedMessage by remember {
        mutableStateOf(DataToEncryptClass())
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(32.dp)){
        TextField(value = message, onValueChange = { message = it })
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Button(onClick = {
                //Guardo los datos en el dataStore
                coroutineScope.launch {
                    dataStore.updateData {
                        DataToEncryptClass(message)
                    }
                }
            }) {
                Text(text = "Save")
            }
            Button(onClick = {
                //Recupero los datos del dataStore
                coroutineScope.launch {
                    decriptedMessage = dataStore.data.first()
                }
            }) {
                Text(text = "Load")
            }
        }
        Text(text = "Decripted message: $decriptedMessage")
    }
}
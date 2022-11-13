package com.example.investigation.encryptDataStore

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
//import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun EncriptDataStoreScreen() {
    var message by remember {
        mutableStateOf("")
    }

    var decriptedMessage by remember {
        mutableStateOf("")
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(32.dp)){
        TextField(value = message, onValueChange = { message = it })
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Button(onClick = { /*TODO*/ }) {
                Text(text = "Save")
            }
            Button(onClick = { /*TODO*/ }) {
                Text(text = "Load")
            }
        }
        Text(text = "Decripted message: $decriptedMessage")
    }
}
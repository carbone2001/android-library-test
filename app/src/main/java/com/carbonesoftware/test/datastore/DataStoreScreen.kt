package com.carbonesoftware.test.datastore

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun DataStoreScreen(
    onWrite: (String, String) -> Unit,
    onRead: (String) -> Unit,
    readValueStateFlow: MutableStateFlow<String>
) {

    var saveKey by rememberSaveable { mutableStateOf("") }
    var saveValue by rememberSaveable { mutableStateOf("") }

    var readKey by rememberSaveable { mutableStateOf("") }
    val readValue by readValueStateFlow.collectAsState()


    Column() {
        TextField(
            value = saveKey,
            onValueChange = { saveKey = it },
            modifier = Modifier.border(1.dp, Color.Red),
            placeholder = { Text(text = "Nombre de la key") }
        )
        TextField(
            value = saveValue,
            onValueChange = { saveValue = it },
            modifier = Modifier.border(1.dp, Color.Red),
            placeholder = { Text(text = "Valor a encriptar") }
        )
        Button(onClick = { onWrite(saveKey, saveValue) }) {
            Text(text = "Save")
        }

        Spacer(modifier = Modifier.size(50.dp))

        TextField(
            value = readKey,
            onValueChange = { readKey = it },
            modifier = Modifier.border(1.dp, Color.Red),
            placeholder = { Text(text = "Nombre de la key para desencriptar") }
        )
        Button(onClick = { onRead(readKey) }) {
            Text(text = "Read")
        }
        Text(text = "Result: " + readValue)
    }
}
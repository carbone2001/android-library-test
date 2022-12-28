package com.carbonesoftware.test.biometricAuth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun BiometricAuthScreen(isAuthStateFlow: MutableStateFlow<Boolean>, onAuthenticate: ()->Unit) {
    val isAuth by isAuthStateFlow.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isAuth) {
            Text(text = "Autenticacion exitosa!")
            Button(onClick = { isAuthStateFlow.value = false }) {
                Text(text = "Logout")
            }
        } else {
            Text(text = "Se require autenticación")
            Button(onClick = {
                onAuthenticate()
            }) {
                Text(text = "Autenticarse")
            }
        }
    }
}
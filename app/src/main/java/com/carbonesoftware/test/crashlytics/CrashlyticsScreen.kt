package com.carbonesoftware.test.crashlytics

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.crashlytics.FirebaseCrashlytics

@Composable
fun CrashlyticsScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { throw RuntimeException("Error de prueba") }) {
            Text(text = "Provocar error")
        }

        Spacer(modifier = Modifier.size(15.dp))

        Button(onClick = {
            val instance = FirebaseCrashlytics.getInstance()
            instance.setUserId("user@email.com")
            instance.setCustomKey("CODIGO_DEL_ERROR","Soy el mensaje del error ;)")
            instance.log("Log del error")
            throw java.lang.RuntimeException("Exception PROVOCADA")
        }) {
            Text(text = "Provocar error CON datos")
        }

        Spacer(modifier = Modifier.size(15.dp))

        Button(onClick = {
            val instance = FirebaseCrashlytics.getInstance()
            instance.log("LOG DE ERROR SIN CRASH")
            instance.recordException(Exception("Exception controlada SIN CRASH"))
        }) {
            Text(text = "Provocar error SIN CRASH")
        }
    }
}
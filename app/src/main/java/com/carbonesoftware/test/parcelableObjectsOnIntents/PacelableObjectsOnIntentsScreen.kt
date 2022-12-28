package com.carbonesoftware.test.parcelableObjectsOnIntents

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch

@Composable
fun ParcelableObjectsOnIntentsScreen(){
    val ctx = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Button(onClick = {
            coroutineScope.launch {
                val sampleObject = SampleClass("Hola mundo!",1234)
                val intent = Intent(ctx, TestActivity::class.java)
                intent.putExtra("sample-object",sampleObject)
                ctx.startActivity(intent)
            }
        }) {
            Text(text = "Iniciar la otra Activity")
        }
    }
}
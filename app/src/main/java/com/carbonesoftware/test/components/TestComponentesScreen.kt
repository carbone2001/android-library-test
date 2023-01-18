package com.carbonesoftware.test.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun TestComponentScreen() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.padding(16.dp)) {
        var swipeButtonState by remember { mutableStateOf(SwipeButtonState.DEFAULT) }
        val coroutineScope = rememberCoroutineScope()

        SwipeButton2(
            label = "Confirmar",
            modifier = Modifier.fillMaxWidth(),
            state = swipeButtonState
        ) {
            coroutineScope.launch {
                swipeButtonState = SwipeButtonState.LOADING
                delay(2000)
                swipeButtonState = SwipeButtonState.SUCCESS
                delay(3000)
                swipeButtonState = SwipeButtonState.DEFAULT
            }
        }

//        SwipeButton(text = "Confirmar", isComplete, modifier = Modifier.fillMaxWidth()){
//            coroutineScope.launch {
//                delay(2000)
//                isComplete = true
//                delay(5000)
//                isComplete = false
//            }
//        }
    }
}
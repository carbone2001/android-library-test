package com.carbonesoftware.test.animations

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun AnimationsScreen() {
    var isVisible by rememberSaveable { mutableStateOf(false) }
    Column(modifier = Modifier.fillMaxSize()) {
        Button(onClick = {
            isVisible = !isVisible
        }) {
            Text("Mostrar / Ocultar")
        }
        AnimatedVisibility(
            visible = isVisible,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            Box(
                modifier = Modifier
                    .size(150.dp)
                    .background(Color.Red)
            )
        }
    }
}

enum class ComponentType {
    Image,
    Text,
    Box,
    Error
}

@Composable
fun CrossfadeAnimationScreen() {
    var currentComponentType by rememberSaveable {
        mutableStateOf(ComponentType.Text)
    }

    Column {
        Button(onClick = { currentComponentType = getRandomComponentType() }) {
            Text(text = "Cambiar componente")
        }

        //Le paso la varible que tendrá los cambios
        Crossfade(targetState = currentComponentType) {
            when(it){
                ComponentType.Text -> Text("Soy un componente texto")
                ComponentType.Image -> Icon(imageVector = Icons.Default.Star, contentDescription = "Star")
                ComponentType.Box -> Box(modifier = Modifier
                    .size(150.dp)
                    .background(Color.Red))
                ComponentType.Error -> Text(text = "Error!")
            }
        }
    }
}

fun getRandomComponentType(): ComponentType {
    val randomIndex = (0..3).random()
    return ComponentType.values()[randomIndex]
}
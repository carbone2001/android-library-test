package com.carbonesoftware.test.shimmerEffect

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.unit.IntSize
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp

@Composable
fun ShimmerEffectScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        Box(modifier = Modifier
            .size(150.dp)
            .shimmerEffect()){}
    }
}

fun Modifier.shimmerEffect(): Modifier = composed {
    var size by remember { mutableStateOf(IntSize.Zero) }
    val transition = rememberInfiniteTransition()
    val startOffsetX by transition.animateFloat(
        //Lado izquierdo como punto de inicio
        initialValue = -2 * size.width.toFloat(),
        //Lado derecho como punto final
        targetValue = 2 * size.width.toFloat(),
        //Animacion infinita
        animationSpec = infiniteRepeatable(animation = tween(1000))
    )

    background(
        //Dibujar gradiente
        brush = Brush.linearGradient(
            colors = listOf(
                Color(0xFFDDDDDD),//Color fondo
                Color(0xFFFFFFFF),//Color del centro
                Color(0xFFDDDDDD),//Color fondo
            ),
            start = Offset(x = startOffsetX, y = 0f),
            end = Offset(
                x = startOffsetX + size.width.toFloat(),
                y = size.height.toFloat()
            )
        )
    )
        .onGloballyPositioned {
            //Obtener tamaño del composable
            size = it.size
        }
}
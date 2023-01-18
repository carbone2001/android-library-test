package com.carbonesoftware.test.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.*
import kotlin.math.roundToInt

enum class SwipeButtonState {
    DEFAULT,
    LOADING,
    SUCCESS,
}

@Composable
fun SwipeButton2(
    label: String,
    modifier: Modifier,
    buttonColor: Color = Color.Red,
    backgroundColor: Color = Color.Gray,
    state: SwipeButtonState = SwipeButtonState.DEFAULT,
    onSwipe: () -> Unit,
) {
    var buttonOffset by remember { mutableStateOf(0f) }
    var buttonSize by remember { mutableStateOf(IntSize.Zero) }
    var containerSize by remember { mutableStateOf(IntSize.Zero) }
    val buttonLimit by derivedStateOf { containerSize.width - buttonSize.width }

    var enableAnimation by remember { mutableStateOf(false) }
    val animatedButtonOffset = animateFloatAsState(
        targetValue = buttonOffset,
        animationSpec = tween(200),
        finishedListener = { enableAnimation = false }
    )

    val progress by derivedStateOf {
        try {
            buttonOffset.roundToInt() * 100 / buttonLimit
        } catch (e: Exception) {
            0
        }
    }

    val color = buttonColor.copy(alpha = progress / 100.toFloat())

    Box {
        Box(
            modifier = modifier
                .clip(RoundedCornerShape(12.dp))
                .height(50.dp)
                .background(if (state != SwipeButtonState.DEFAULT) buttonColor else backgroundColor)
                .onGloballyPositioned {
                    containerSize = it.size
                },
            contentAlignment = Alignment.CenterStart,
        ) {}

        Box(
            modifier = modifier
                .clip(RoundedCornerShape(12.dp))
                .height(50.dp)
                .background(color)
                .onGloballyPositioned {
                    containerSize = it.size
                },
            contentAlignment = Alignment.CenterStart,
        ) {
            Row(
                Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                when (state) {
                    SwipeButtonState.SUCCESS -> {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = White
                        )
                    }
                    SwipeButtonState.LOADING -> {
                        CircularProgressIndicator(
                            strokeWidth = 2.dp,
                            color = White,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                    else -> {
                        Text(text = label)
                    }
                }
            }
            if (state == SwipeButtonState.DEFAULT) {
                Box(
                    modifier = Modifier
                        .offset {
                            IntOffset(
                                (if (enableAnimation) animatedButtonOffset.value else buttonOffset).roundToInt(),
                                0
                            )
                        }
                        .padding(4.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .pointerInput(Unit) {
                            detectHorizontalDragGestures(
                                onHorizontalDrag = { change, dragAmont ->
                                    val newPosition = buttonOffset + change.position.x
                                    if (newPosition < (containerSize.width - 5)) {
                                        buttonOffset += change.position.x
                                    }

                                    buttonOffset = newPosition.coerceIn(0f, (buttonLimit).toFloat())
                                },
                                onDragStart = {
                                    enableAnimation = false
                                },
                                onDragEnd = {
                                    if (buttonOffset.roundToInt() / buttonLimit == 1) {
                                        onSwipe()
                                    }
                                    enableAnimation = true
                                    buttonOffset = 0f
                                },
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(
                        onClick = {},
                        modifier = Modifier
                            .size((containerSize.height-80).dp)
                            .background(Color.Red)
                            .onGloballyPositioned {
                                buttonSize = it.size
                            },
                    ) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowRight,
                            contentDescription = null,
                            tint = White
                        )
                    }
                }
            }
        }
    }
}
package com.carbonesoftware.test.dragAndDrop

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import kotlin.math.abs
import kotlin.math.roundToInt

@Composable
fun CustomDragAndDrop() {
    var coinList by rememberSaveable {
        mutableStateOf(
            mutableListOf(
                Coin(position = 0, ticker = "USDT", price = 350.0),
                Coin(position = 1, ticker = "BTC", price = 19500.0),
                Coin(position = 2, ticker = "ETH", price = 2500.0),
                Coin(position = 3, ticker = "BNB", price = 5000.0),
            )
        )
    }

    val onListOrderChange = { fromPosition: Int, toPosition: Int ->
        val fromCoin = coinList[fromPosition]
        val toCoin = coinList[toPosition]
        coinList[fromPosition] = fromCoin.copy(position = toPosition)
        coinList[toPosition] = toCoin.copy(position = fromPosition)
        coinList = ArrayList(coinList.sortedBy { it.position })
    }

    //VARIABLES GENERALES, DEL DRAGGED ITEM
    var nextPositionProximation by remember { mutableStateOf<Int?>(null) }
    var itemPositionByParent by remember { mutableStateOf(HashMap<Int, Int>()) }
    var dragedItem by remember { mutableStateOf<Coin?>(null) }
    var currentDraggedItemPosition by remember { mutableStateOf<Float?>(null) }

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(coinList, key = { coin -> coin.position }) { coin ->

            //VARIABLES PROPIAS DE CADA ITEM
            var offsetY by remember { mutableStateOf(0f) }
            var isCurrentItemDragging by rememberSaveable { mutableStateOf(false) }
            var someItemIsOver by remember { mutableStateOf(false) }
            var itemSize by rememberSaveable { mutableStateOf(0) }
            var ownPosition by rememberSaveable { mutableStateOf(0f) }

            if (dragedItem != null) {
                currentDraggedItemPosition?.let {
                    val distanceFromDragged = abs(it - ownPosition)
                    Log.d(
                        "COO",
                        "item: ${coin.position} - ownPos: ${ownPosition} - distanceFromDragged: ${distanceFromDragged} - draggedPos: ${it}"
                    )

                    if (dragedItem?.position != coin.position && (distanceFromDragged < itemSize / 3)) {
                        someItemIsOver = true
                        nextPositionProximation = coin.position
                    }
                }
            } else {
                someItemIsOver = false
            }


            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(if (someItemIsOver) Color.Blue else Color.Gray)
                    .zIndex(if (isCurrentItemDragging) 1f else 0f)
                    .offset {
                        IntOffset(
                            if (isCurrentItemDragging) 10 else 0,
                            offsetY.roundToInt()
                        )
                    }
                    .onGloballyPositioned { coordinates ->
                        itemSize = coordinates.parentCoordinates?.size?.height ?: 0
                        itemPositionByParent[coin.position] = itemSize * coin.position
                        val allListSize = itemSize * coinList.size
                        ownPosition = itemSize * coin.position + offsetY
                    }
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDrag = { change: PointerInputChange, dragAmount: Offset ->
                                isCurrentItemDragging = true
                                offsetY += dragAmount.y
                                ownPosition = itemSize * coin.position + offsetY
                                currentDraggedItemPosition = ownPosition

                            },
                            onDragStart = {
                                isCurrentItemDragging = true
                                dragedItem = coin
                            },
                            onDragEnd = {
                                dragedItem?.let {
                                    onListOrderChange(it.position, nextPositionProximation!!)
                                }

                                isCurrentItemDragging = false
                                offsetY = 0f
                                dragedItem = null
                                currentDraggedItemPosition = null
                            },
                            onDragCancel = {
                                isCurrentItemDragging = false
                                offsetY = 0f
                                dragedItem = null
                                currentDraggedItemPosition = null


                            }
                        )
                    },
                elevation = if (isCurrentItemDragging) 8.dp else 0.dp,
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = coin.ticker)
                    Text(text = coin.price.toString())
                }
            }
        }

    }

}
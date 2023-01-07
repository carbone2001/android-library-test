package com.carbonesoftware.test.dragAndDrop

import android.os.Parcelable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import kotlinx.parcelize.Parcelize
import kotlin.math.abs
import kotlin.math.roundToInt

@Parcelize
data class DraggableListState(
    /**
     * Ítems que tengan al ítem arrastrado encima
     */
    val itemsWithItemOver: HashMap<Int, Boolean> = HashMap(),

    /**
     * Posiciones verticales (Y) de cada ítem dentro del padre
     */
    val itemPositionsInParent: HashMap<Int, Int> = HashMap(),

    /**
     * Índice del elemento arrastrado
     */
    val draggedItemIndex: Int? = null,

    /**
     * Posición vertical (Y) del elemento arrastrado
     */
    val currentDraggedItemPosition: Float? = null,
) : Parcelable

@Parcelize
data class DraggableItemState(
    /**
     * Posición vertical del ítem
     */
    val offsetY: Float = 0f,

    /**
     * Devuelve *true* si el ítem actual esta siendo arrastrado
     */
    val isCurrentItemDragging: Boolean = false,

    /**
     * Devuelve *true* si el ítem arrastrado está sobre el item actual
     */
    val someItemIsOver: Boolean = false,

    /**
     * Altura del ítem
     */
    val itemSize: Int = 0,

    /**
     * Posición en vertical del ítem
     */
    val ownPosition: Float = 0f,
) : Parcelable

@Composable
fun TestDraggableList() {

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


    Box(modifier = Modifier.padding(16.dp)){
        DraggableList(list = coinList, onListChanges = { coinList = it }) { item, state ->
            Card(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = item.ticker)
                    Text(text = item.price.toString())
                }
            }
        }
    }
}

@Composable
fun <T> DraggableList(
    list: MutableList<T>,
    onListChanges: (newList: MutableList<T>) -> Unit,
    content: @Composable (T, state: DraggableItemState) -> Unit
) {

    val onListOrderChange = { fromIndex: Int, toIndex: Int ->
        val fromItem = list[fromIndex]
        val toItem = list[toIndex]
        list[fromIndex] = toItem
        list[toIndex] = fromItem
        onListChanges(ArrayList(list))
    }

    var draggableListState by rememberSaveable {
        mutableStateOf(DraggableListState())
    }

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        itemsIndexed(list) { index, item ->
            var itemState by rememberSaveable { mutableStateOf(DraggableItemState()) }
            itemState = itemState.copy(
                someItemIsOver = draggableListState.itemsWithItemOver[index] ?: false
            )

            if (draggableListState.draggedItemIndex != null) {
                draggableListState.currentDraggedItemPosition?.let {
                    val distanceFromDragged = abs(it - itemState.ownPosition)
                    draggableListState.itemsWithItemOver[index] =
                        draggableListState.draggedItemIndex != index && (distanceFromDragged < itemState.itemSize / 3)
                }
            } else {
                itemState = itemState.copy(someItemIsOver = false, offsetY = 0f)
            }

            val horizontalPositionContent by animateIntAsState(
                targetValue = if (itemState.isCurrentItemDragging) 15 else 0,
                animationSpec = tween(durationMillis = 150),
                finishedListener = { }
            )

            val opacityContent by animateFloatAsState(
                targetValue = if (itemState.someItemIsOver) 0.5f else 1f,
                animationSpec = tween(durationMillis = 150),
                finishedListener = { }
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .alpha(opacityContent)
                    .zIndex(if (itemState.isCurrentItemDragging) 1f else 0f)
                    .offset {
                        IntOffset(
                            horizontalPositionContent,
                            itemState.offsetY.roundToInt()
                        )
                    }
                    .onGloballyPositioned { coordinates ->
                        itemState = itemState.copy(
                            itemSize = coordinates.parentCoordinates?.size?.height ?: 0
                        )
                        draggableListState.itemPositionsInParent[index] =
                            itemState.itemSize * index
                        itemState =
                            itemState.copy(ownPosition = itemState.itemSize * index + itemState.offsetY)
                    }
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDrag = { _, dragAmount: Offset ->
                                itemState = itemState.copy(isCurrentItemDragging = true)
                                itemState =
                                    itemState.copy(offsetY = itemState.offsetY + dragAmount.y)
                                itemState =
                                    itemState.copy(ownPosition = itemState.itemSize * index + itemState.offsetY)
                                draggableListState =
                                    draggableListState.copy(currentDraggedItemPosition = itemState.ownPosition)

                            },
                            onDragStart = {
                                itemState = itemState.copy(isCurrentItemDragging = true)
                                draggableListState =
                                    draggableListState.copy(draggedItemIndex = index)
                                draggableListState =
                                    draggableListState.copy(itemsWithItemOver = HashMap())
                            },
                            onDragEnd = {
                                draggableListState.draggedItemIndex?.let {
                                    draggableListState.itemsWithItemOver.forEach { (key, value) ->
                                        if (value) {
                                            onListOrderChange(it, key)
                                            return@forEach
                                        }
                                    }
                                }

                                itemState = itemState.copy(
                                    isCurrentItemDragging = false,
                                    offsetY = 0f,
                                )
                                draggableListState =
                                    draggableListState.copy(
                                        draggedItemIndex = null,
                                        currentDraggedItemPosition = null
                                    )
                            },
                            onDragCancel = {
                                itemState = itemState.copy(
                                    isCurrentItemDragging = false,
                                    offsetY = 0f,
                                )
                                draggableListState =
                                    draggableListState.copy(
                                        draggedItemIndex = null,
                                        currentDraggedItemPosition = null
                                    )
                            }
                        )
                    },
            ) {
                content(item, itemState)
            }
        }

    }

}
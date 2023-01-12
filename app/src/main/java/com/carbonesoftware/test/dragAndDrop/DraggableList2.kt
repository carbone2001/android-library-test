package com.carbonesoftware.test.dragAndDrop

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@Composable
fun TestDraggableList2() {

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



    DragDropList(items = coinList, onMove = { from, to ->
        val coinFrom = coinList[from]
        val coinTo = coinList[to]

        coinList[from] = coinTo
        coinList[to] = coinFrom
    }, modifier = Modifier.fillMaxWidth())
//    Column(modifier = Modifier.padding(16.dp)) {
//        Column(
//            Modifier
//                .background(Color.Red)
//                .fillMaxWidth()
//                .height(200.dp)) {
//
//        }
//
//        DraggableList(list = coinList, onListChanges = { coinList = it }) { item, state ->
//            Card(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(vertical = 8.dp)
//            ) {
//                Row(
//                    modifier = Modifier.padding(20.dp),
//                    horizontalArrangement = Arrangement.SpaceBetween
//                ) {
//                    Text(text = item.ticker)
//                    Text(text = item.price.toString())
//                }
//            }
//        }
//    }
}

@Composable
fun <T> DragDropList(
    items: List<T>,
    onMove: (Int, Int) -> Unit,
    modifier: Modifier = Modifier
) {

    val scope = rememberCoroutineScope()

    var overscrollJob by remember { mutableStateOf<Job?>(null) }

    val dragDropListState = rememberDragDropListState(onMove = onMove)

    LazyColumn(
        modifier = modifier
            .pointerInput(Unit) {
                detectDragGesturesAfterLongPress(
                    onDrag = { change, offset ->
                        change.consumeAllChanges()
                        dragDropListState.onDrag(offset)

                        if (overscrollJob?.isActive == true)
                            return@detectDragGesturesAfterLongPress

                        dragDropListState.checkForOverScroll()
                            .takeIf { it != 0f }
                            ?.let {
                                overscrollJob =
                                    scope.launch { dragDropListState.lazyListState.scrollBy(it) }
                            }
                            ?: run { overscrollJob?.cancel() }
                    },
                    onDragStart = { offset -> dragDropListState.onDragStart(offset) },
                    onDragEnd = { dragDropListState.onDragInterrupted() },
                    onDragCancel = { dragDropListState.onDragInterrupted() }
                )
            },
        state = dragDropListState.lazyListState
    ) {
        itemsIndexed(items) { index, item ->
            Column(
                modifier = Modifier
                    .composed {
                        val offsetOrNull =
                            dragDropListState.elementDisplacement.takeIf {
                                index == dragDropListState.currentIndexOfDraggedItem
                            }

                        Modifier
                            .graphicsLayer {
                                translationY = offsetOrNull ?: 0f
                            }
                    }
                    .background(Color.White, shape = RoundedCornerShape(4.dp))
                    .fillMaxWidth()
            ) { Text(text = "Item $item") }
        }
    }
}


@Composable
fun rememberDragDropListState(
    lazyListState: LazyListState = rememberLazyListState(),
    onMove: (Int, Int) -> Unit,
): DragDropListState {
    return remember { DragDropListState(lazyListState = lazyListState, onMove = onMove) }
}

class DragDropListState(
    val lazyListState: LazyListState,
    private val onMove: (Int, Int) -> Unit
) {
    var draggedDistance by mutableStateOf(0f)

    // used to obtain initial offsets on drag start
    var initiallyDraggedElement by mutableStateOf<LazyListItemInfo?>(null)

    var currentIndexOfDraggedItem by mutableStateOf<Int?>(null)

    val initialOffsets: Pair<Int, Int>?
        get() = initiallyDraggedElement?.let { Pair(it.offset, it.offsetEnd) }

    val elementDisplacement: Float?
        get() = currentIndexOfDraggedItem
            ?.let { lazyListState.getVisibleItemInfoFor(absoluteIndex = it) }
            ?.let { item ->
                (initiallyDraggedElement?.offset ?: 0f).toFloat() + draggedDistance - item.offset
            }

    val currentElement: LazyListItemInfo?
        get() = currentIndexOfDraggedItem?.let {
            lazyListState.getVisibleItemInfoFor(absoluteIndex = it)
        }

    var overscrollJob by mutableStateOf<Job?>(null)

    fun onDragStart(offset: Offset) {
        lazyListState.layoutInfo.visibleItemsInfo
            .firstOrNull { item -> offset.y.toInt() in item.offset..(item.offset + item.size) }
            ?.also {
                currentIndexOfDraggedItem = it.index
                initiallyDraggedElement = it
            }
    }

    fun onDragInterrupted() {
        draggedDistance = 0f
        currentIndexOfDraggedItem = null
        initiallyDraggedElement = null
        overscrollJob?.cancel()
    }

    fun onDrag(offset: Offset) {
        draggedDistance += offset.y

        initialOffsets?.let { (topOffset, bottomOffset) ->
            val startOffset = topOffset + draggedDistance
            val endOffset = bottomOffset + draggedDistance

            currentElement?.let { hovered ->
                lazyListState.layoutInfo.visibleItemsInfo
                    .filterNot { item -> item.offsetEnd < startOffset || item.offset > endOffset || hovered.index == item.index }
                    .firstOrNull { item ->
                        val delta = startOffset - hovered.offset
                        when {
                            delta > 0 -> (endOffset > item.offsetEnd)
                            else -> (startOffset < item.offset)
                        }
                    }
                    ?.also { item ->
                        currentIndexOfDraggedItem?.let { current ->
                            onMove.invoke(
                                current,
                                item.index
                            )
                        }
                        currentIndexOfDraggedItem = item.index
                    }
            }
        }
    }

    fun checkForOverScroll(): Float {
        return initiallyDraggedElement?.let {
            val startOffset = it.offset + draggedDistance
            val endOffset = it.offsetEnd + draggedDistance

            return@let when {
                draggedDistance > 0 -> (endOffset - lazyListState.layoutInfo.viewportEndOffset).takeIf { diff -> diff > 0 }
                draggedDistance < 0 -> (startOffset - lazyListState.layoutInfo.viewportStartOffset).takeIf { diff -> diff < 0 }
                else -> null
            }
        } ?: 0f
    }
}

fun LazyListState.getVisibleItemInfoFor(absoluteIndex: Int): LazyListItemInfo? {
    return this.layoutInfo.visibleItemsInfo.getOrNull(absoluteIndex - this.layoutInfo.visibleItemsInfo.first().index)
}

/*
  Bottom offset of the element in Vertical list
*/
val LazyListItemInfo.offsetEnd: Int
    get() = this.offset + this.size

/*
   Moving element in the list
*/
fun <T> MutableList<T>.move(from: Int, to: Int) {
    if (from == to)
        return

    val element = this.removeAt(from) ?: return
    this.add(to, element)
}

//class ReorderableListState(
//    val lazyListState: LazyListState,
//    private val onMove: (Int, Int) -> Unit
//) {
//    var draggedDistance by mutableStateOf(0f)
//
//    // used to obtain initial offsets on drag start
//    var initiallyDraggedElement by mutableStateOf<LazyListItemInfo?>(null)
//
//    var currentIndexOfDraggedItem by mutableStateOf<Int?>(null)
//
//    val initialOffsets: Pair<Int, Int>?
//        get() = initiallyDraggedElement?.let { Pair(it.offset, it.offsetEnd) }
//
//    val elementDisplacement: Float?
//        get() = currentIndexOfDraggedItem
//            ?.let { lazyListState.getVisibleItemInfoFor(absoluteIndex = it) }
//            ?.let { item -> (initiallyDraggedElement?.offset ?: 0f).toFloat() + draggedDistance - item.offset }
//
//    val currentElement: LazyListItemInfo?
//        get() = currentIndexOfDraggedItem?.let {
//            lazyListState.getVisibleItemInfoFor(absoluteIndex = it)
//        }
//
//    var overscrollJob by mutableStateOf<Job?>(null)
//
//    fun onDragStart(offset: Offset) {
//        lazyListState.layoutInfo.visibleItemsInfo
//            .firstOrNull { item -> offset.y.toInt() in item.offset..(item.offset + item.size) }
//            ?.also {
//                currentIndexOfDraggedItem = it.index
//                initiallyDraggedElement = it
//            }
//    }
//
//    fun onDragInterrupted() {
//        draggedDistance = 0f
//        currentIndexOfDraggedItem = null
//        initiallyDraggedElement = null
//        overscrollJob?.cancel()
//    }
//
//    fun onDrag(offset: Offset) {
//        draggedDistance += offset.y
//
//        initialOffsets?.let { (topOffset, bottomOffset) ->
//            val startOffset = topOffset + draggedDistance
//            val endOffset = bottomOffset + draggedDistance
//
//            currentElement?.let { hovered ->
//                lazyListState.layoutInfo.visibleItemsInfo
//                    .filterNot { item -> item.offsetEnd < startOffset || item.offset > endOffset || hovered.index == item.index }
//                    .firstOrNull { item ->
//                        val delta = startOffset - hovered.offset
//                        when {
//                            delta > 0 -> (endOffset > item.offsetEnd)
//                            else -> (startOffset < item.offset)
//                        }
//                    }
//                    ?.also { item ->
//                        currentIndexOfDraggedItem?.let { current -> onMove.invoke(current, item.index) }
//                        currentIndexOfDraggedItem = item.index
//                    }
//            }
//        }
//    }
//
//    fun checkForOverScroll(): Float {
//        return initiallyDraggedElement?.let {
//            val startOffset = it.offset + draggedDistance
//            val endOffset = it.offsetEnd + draggedDistance
//
//            return@let when {
//                draggedDistance > 0 -> (endOffset - lazyListState.layoutInfo.viewportEndOffset).takeIf { diff -> diff > 0 }
//                draggedDistance < 0 -> (startOffset - lazyListState.layoutInfo.viewportStartOffset).takeIf { diff -> diff < 0 }
//                else -> null
//            }
//        } ?: 0f
//    }
//}
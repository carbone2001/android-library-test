package com.carbonesoftware.test.dragAndDrop

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import org.burnoutcrew.reorderable.*
import javax.inject.Inject


@HiltViewModel
class DragAndDropSortableListViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    var _coinList = mutableStateListOf(
        Coin(position = 0, ticker = "USDT", price = 350.0),
        Coin(position = 1, ticker = "BTC", price = 19500.0),
        Coin(position = 2, ticker = "ETH", price = 2500.0),
        Coin(position = 3, ticker = "BNB", price = 5000.0),
    )
    val coinList: List<Coin> = _coinList

    fun onListReordered(fromPos: ItemPosition, toPos: ItemPosition) {
        //_coinList[fromPos.index].position = toPos.index
        //_coinList[toPos.index].position = fromPos.index
        //_coinList.sortBy { it.position }
        ///_coinList
    }
}

@Composable
fun TestDragAndDropListSortScreen(viewModel: DragAndDropSortableListViewModel) {
    val state = rememberReorderState() // 1.
    val coinList: List<Coin> = viewModel.coinList

    LazyColumn(
        state = state.listState,
        modifier = Modifier
            .reorderable(state, { fromPos, toPos -> // 2.
                viewModel.onListReordered(fromPos, toPos)
            })
            .detectReorderAfterLongPress(state) // 3.
    ) {
        items(coinList, key = { coin -> coin.position }) { coin ->
            Card(modifier = Modifier
                .fillMaxWidth()
                .draggedItem(state.offsetByKey(coin.position))) {
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
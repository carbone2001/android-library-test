package com.carbonesoftware.test.pagination

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

@Composable
fun TestPaginationScreen(viewModel: PaginationViewModel) {
    val state = viewModel.state

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(state.items.size) { i ->
            val item = state.items[i]

            //Cargar nuevos items si se está al final de la lista y hay posiblidad de cargar mas
            if (i >= state.items.size - 1 && !state.endReached && !state.isLoading) {
                viewModel.loadNextItems()
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(text = item.title, fontSize = 20.sp, color = Color.Black)
                Spacer(modifier = Modifier.size(8.dp))
                Text(text = item.description)
            }
        }
        item {
            if (state.isLoading) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp), horizontalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

class PaginationViewModel : ViewModel() {
    private val repository = FakeRepository()

    var state by mutableStateOf(PaginationScreenState())

    private val paginator = DefualtPaginator(
        initialPage = state.page,
        onLoadUpdated = {
            state = state.copy(isLoading = it)
        },
        onRequest = { nextPage ->
            repository.getItems(nextPage, 20)
        },
        getNextPage = { state.page + 1 },
        onError = { state = state.copy(error = it?.localizedMessage) },
        onSuccess = { newItems, newPage ->
            state = state.copy(
                items = state.items + newItems,
                page = newPage,
                endReached = newItems.isEmpty()
            )
        }
    )

    init {
        loadNextItems()
    }

    fun loadNextItems() = viewModelScope.launch {
        paginator.loadNextItems()
    }

    data class PaginationScreenState(
        val isLoading: Boolean = false,
        val items: List<FakeItem> = emptyList(),
        val error: String? = null,
        val endReached: Boolean = false,//Fin de la lista?
        val page: Int = 0,
    )
}
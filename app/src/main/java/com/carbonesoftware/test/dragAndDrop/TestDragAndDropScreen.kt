package com.carbonesoftware.test.dragAndDrop

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.microsoft.device.dualscreen.draganddrop.*

data class Coin(
    var position: Int = 0,
    val ticker: String,
    val price: Double)

@Composable
fun TestDragAndDropScreen() {
    val coinList by rememberSaveable {
        mutableStateOf(
            listOf(
                Coin(ticker = "USDT", price = 350.0),
                Coin(ticker = "BTC", price = 19500.0),
                Coin(ticker = "ETH", price = 2500.0),
                Coin(ticker = "BNB", price = 5000.0),
            )
        )
    }


    DragContainer(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Scaffold(
            topBar = { },
            floatingActionButton = { }
        ) {
            //DragAndDropContent2(coinList)
            DragAndDropContent1(coinList)
        }

    }

}

@Composable
fun DragAndDropContent1(coinList: List<Coin>) {
    var isDragging by rememberSaveable { mutableStateOf(false) }
    var inBounds by rememberSaveable { mutableStateOf(false) }
    Column() {
        coinList.forEach { coin ->
            DragTarget(dragData = DragData(data = coin, type = MimeType.UNKNOWN_TYPE)) {
                Card(modifier = Modifier.fillMaxWidth()) {
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


        DropContainer(modifier = Modifier.fillMaxWidth(), onDrag = { bounds, dragging ->
            isDragging = dragging
            inBounds = bounds
        }) { data ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(21.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (isDragging) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(width = 5.dp, color = if (inBounds) Color.Gray else Color.Blue),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Soltar aquí")
                    }
                }

                (data?.data as Coin?)?.let { coin ->
                    Card(modifier = Modifier.fillMaxWidth()) {
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

    }

    @Composable
    fun DragAndDropContent2(coinList: List<Coin>) {
        var isDragging by rememberSaveable { mutableStateOf(false) }
        var inBounds by rememberSaveable { mutableStateOf(false) }

        Column {
            DragTarget(dragData = DragData(data = coinList[0], type = MimeType.UNKNOWN_TYPE)) {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = coinList[0].ticker)
                        Text(text = coinList[0].price.toString())
                    }
                }
            }

            DragTarget(dragData = DragData(data = coinList[1], type = MimeType.UNKNOWN_TYPE)) {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = coinList[1].ticker)
                        Text(text = coinList[1].price.toString())
                    }
                }
            }

            DropContainer(modifier = Modifier.fillMaxWidth(), onDrag = { bounds, dragging ->
                isDragging = dragging
                inBounds = bounds
            }) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(width = 5.dp, color = if (!inBounds) Color.Gray else Color.Blue),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Soltar aquí")
                }
            }
        }
    }

}


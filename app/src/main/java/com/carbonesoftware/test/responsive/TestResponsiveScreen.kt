package com.carbonesoftware.test.responsive

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TestResponsiveScreen() {
    val windowInfo = rememberWindowInfo()

    if (windowInfo.screenWidthInfo == WindowInfo.WindowType.Compact) {
        //Para resoluciones chicas, mostrar como columna
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CardWithList(modifier = Modifier.weight(1f), "Lista 1")
            CardWithList(modifier = Modifier.weight(1f), "Lista 2")
        }
    } else {
        //Para resoluciones mas grandes, mostrar como file
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CardWithList(modifier = Modifier.weight(1f), "Lista 1")
            CardWithList(modifier = Modifier.weight(1f), "Lista 2")
        }
    }
}

@Composable
fun CardWithList(modifier: Modifier = Modifier, listName: String){
    Card(modifier = modifier) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = listName)
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(10) {
                    Text(
                        text = "Item $it ($listName)",
                        fontSize = 25.sp,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}
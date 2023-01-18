package com.carbonesoftware.test.theming

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.investigation.ui.theme.spacing

@Composable
fun TestThemingScreen() {
    Column(
        Modifier.fillMaxSize().padding(MaterialTheme.spacing.medium),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { }) {
            Text(text = "Test button")
        }
        TextField(value = "Test text field", onValueChange = {}, modifier = Modifier.fillMaxWidth())
        Card(modifier = Modifier.fillMaxWidth()) {
            Text(text = "Test card", modifier = Modifier.padding(MaterialTheme.spacing.medium))
        }
    }
}
package com.carbonesoftware.test.savedStateHandle

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@Composable
fun TestSavedStateHandleScreen(viewModel: TestSavedStateHandleViewModel){
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        Text(text = viewModel.getSavedStateHandleValue() ?: "null")
    }
}

@HiltViewModel
class TestSavedStateHandleViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
    ): ViewModel(){

    fun getSavedStateHandleValue(): String?{
        savedStateHandle["algunaClave"] = "algunValor"
        return savedStateHandle["algunaClave"]
    }

}
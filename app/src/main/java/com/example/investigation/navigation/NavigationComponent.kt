package com.example.investigation.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost

@Composable
fun NavigationComponent(navHostController: NavHostController, paddingValues: PaddingValues) {
    NavHost(
        navController = navHostController,
        startDestination = MainRoute.route,
        modifier = Modifier.padding(paddingValues)
    ) {
        MainRoute.composable(this, navHostController)
        AnotherRoute.composable(this, navHostController)
    }
}
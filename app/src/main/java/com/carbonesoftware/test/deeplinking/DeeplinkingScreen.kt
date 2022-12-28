package com.carbonesoftware.test.deeplinking

import android.content.Intent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink

@Composable
fun DeeplinkingScreen() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        //HOME SCREEN
        composable(route = "home") {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Button(onClick = {
                    navController.navigate("detail")
                }) {
                    Text(text = "To detail")
                }
            }
        }

        //DETAIL SCREEN
        composable(
            route = "detail",
            deepLinks = listOf(
                navDeepLink {
                    //URL donde se desencadenará el deeplinking
                    //Recibirá un id como parámetro
                    uriPattern = "http://192.168.56.1/{id}"
                    action = Intent.ACTION_VIEW
                }
            ),
            //Parámetros del deeplinking
            arguments = listOf(
                navArgument("id") {
                    type = NavType.IntType
                    //Mostrar -1 en caso de tener parámetro
                    defaultValue = -1
                }
            )
        ) { entry ->
            //Obtengo el id de los argumentos
            val id = entry.arguments?.getInt("id")
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "The ID is: $id")
            }
        }
    }
}
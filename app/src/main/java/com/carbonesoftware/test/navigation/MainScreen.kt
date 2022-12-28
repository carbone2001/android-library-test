package com.carbonesoftware.test.navigation

import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.navigation.compose.rememberNavController

/**
 * La ruta asociada a la screen Main
 */
object MainRoute : NavRoute<MainViewModel> {

    override val route = "home/"

    @Composable
    override fun viewModel(): MainViewModel = hiltViewModel()

    @Composable
    override fun Content(viewModel: MainViewModel) = MainScreen(viewModel)
}

/**
 * La ruta asociada a la screen Another
 */
object AnotherRoute : NavRoute<MainViewModel> {

    override val route = "another/"

    @Composable
    override fun viewModel(): MainViewModel = hiltViewModel()

    @Composable
    override fun Content(viewModel: MainViewModel) = AnotherScreen(viewModel)
}

/**
 * El ViewModel. Será utilizado por Main y Another
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    private val routeNavigator: RouteNavigator
): ViewModel(), RouteNavigator by routeNavigator{

    fun onNavigate(route: String){
        navigateToRoute(route)
    }
}

@Composable
fun MainScreen(viewModel: MainViewModel){
    Text(text = "Main screen works!")
    Button(onClick = { viewModel.onNavigate(AnotherRoute.route) }) {
        Text(text = "Ir a otra screen")
    }
}

@Composable
fun AnotherScreen(viewModel: MainViewModel){
    Text(text = "Another screen works!")
    Button(onClick = { viewModel.onNavigate(MainRoute.route) }) {
        Text(text = "Ir a la main screen")
    }
}

@Composable
fun NavigationScafold(){
    val navController = rememberNavController()
    Scaffold() { innerPadding ->
        NavigationComponent(
            navHostController = navController,
            paddingValues = innerPadding)
    }
}
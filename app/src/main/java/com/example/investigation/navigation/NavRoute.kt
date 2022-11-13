package com.example.investigation.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable

/**
 * Representa una ruta de la app.
 * Normalmente estará asociado a una screen
 */
interface NavRoute<T : RouteNavigator> {

    val route: String

    /**
     * Devuelve el contenido de la screen
     */
    @Composable
    fun Content(viewModel: T)

    /**
     * Deveulve el viewModel del screen. Tiene que se sobreescribirse así Hilt puede generar el codigo para fabricar el ViewModel
     */
    @Composable
    fun viewModel(): T

    /**
     * Sobreescribir está funcion si la pagina utiliza argumentos
     *
     * HACERLO AQUI y no en NavigationComponent para mantenerlo centralizado
     */
    fun getArguments(): List<NamedNavArgument> = listOf()

    /**
     * Genera el composable para la ruta
     */
    fun composable(
        builder: NavGraphBuilder,
        navHostController: NavHostController
    ) {
        builder.composable(route, getArguments()) {
            val viewModel = viewModel()
            val viewStateAsState by viewModel.navigationState.collectAsState()

            LaunchedEffect(viewStateAsState) {
                Log.d("Nav", "${this@NavRoute} updateNavigationState to $viewStateAsState")
                updateNavigationState(navHostController, viewStateAsState, viewModel::onNavigated)
            }

            Content(viewModel)
        }
    }

    /**
     * Navega al viewState
     */
    private fun updateNavigationState(
        navHostController: NavHostController,
        navigationState: NavigationState,
        onNavigated: (navState: NavigationState) -> Unit,
    ) {
        when (navigationState) {
            is NavigationState.NavigateToRoute -> {
                navHostController.navigate(navigationState.route)
                onNavigated(navigationState)
            }
            is NavigationState.PopToRoute -> {
                navHostController.popBackStack(navigationState.staticRoute, false)
                onNavigated(navigationState)
            }
            is NavigationState.NavigateUp -> {
                navHostController.navigateUp()
            }
            is NavigationState.Idle -> {
            }
        }
    }
}

fun <T> SavedStateHandle.getOrThrow(key: String): T =
    get<T>(key) ?: throw IllegalArgumentException(
        "Mandatory argument $key missing in arguments."
    )
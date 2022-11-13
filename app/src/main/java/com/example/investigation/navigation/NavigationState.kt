package com.example.investigation.navigation

import java.util.*

sealed class NavigationState {

    /**
     * @param id Es usando para que multiplas instancias de la misma ruta pueda realizar mutiples navigations calls
     */

    object Idle : NavigationState()

    data class NavigateToRoute(val route: String, val id: String = UUID.randomUUID().toString()) :
        NavigationState()

    /**
     * @param staticRoute Es la ruta estatica a las que se accede, sin reemplazo de parametros
     */
    data class PopToRoute(val staticRoute: String, val id: String = UUID.randomUUID().toString()) :
        NavigationState()

    data class NavigateUp(val id: String = UUID.randomUUID().toString()) : NavigationState()
}
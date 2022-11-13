package com.example.investigation.navigation

import androidx.annotation.VisibleForTesting
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * El Navigator que se utilizará cuando se inicialice la navegacion desde el ViewModel
 * El ViewModel extenderá de RouteNavigator
 */
interface RouteNavigator {
    fun onNavigated(state: NavigationState)
    fun navigateUp()
    fun popToRoute(route: String)
    fun navigateToRoute(route: String)

    val navigationState: StateFlow<NavigationState>
}

/**
 * Se extenderá esta clase cuando se el ViewModel extienda de RouteNavigator
 */
class MyRouteNavigator : RouteNavigator {

    /**
     * Tener en cuenta que aqui se esta usando un single state, no una lista de estados. Como resultado,
     * si el estado se actualiza multiples veces, la vista solo recibirá y manejará el ultimo estado,
     * lo cual es suficiente para este caso de uso
     */
    override val navigationState: MutableStateFlow<NavigationState> =
        MutableStateFlow(NavigationState.Idle)

    override fun onNavigated(state: NavigationState) {
        navigationState.compareAndSet(state, NavigationState.Idle)
    }

    override fun popToRoute(route: String) = navigate(NavigationState.PopToRoute(route))

    override fun navigateUp() = navigate(NavigationState.NavigateUp())

    override fun navigateToRoute(route: String) = navigate(NavigationState.NavigateToRoute(route))

    @VisibleForTesting
    fun navigate(state: NavigationState) {
        navigationState.value = state
    }
}
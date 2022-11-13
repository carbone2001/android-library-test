package com.example.investigation

import com.example.investigation.navigation.MyRouteNavigator
import com.example.investigation.navigation.RouteNavigator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

/**
 * View model module
 *
 * Componentes para inyectar a los viewmodels
 * @constructor Create empty View model module
 */
@Module
@InstallIn(ViewModelComponent::class)
class ViewModelModule {

    /**
     * Bind route navigator
     *
     * Bindear Navigator
     * @return
     */
    @Provides
    @ViewModelScoped
    fun bindRouteNavigator(): RouteNavigator = MyRouteNavigator()

    /*/**
     * Bind event launcher
     *
     * Bindear Event Launcher para Crashlytics
     * @return
     */
    @Provides
    @ViewModelScoped
    fun bindEventLauncher(): EventLauncher = MyEventLauncher()*/
}
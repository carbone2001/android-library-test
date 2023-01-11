package com.carbonesoftware.test

import android.content.Context
import com.carbonesoftware.test.navigation.MyRouteNavigator
import com.carbonesoftware.test.navigation.RouteNavigator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

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
}


@Module
@InstallIn(ActivityComponent::class)
object ActivityModule {

    @Provides
    @ActivityScoped
    fun provideSomethingWithContext(
        @ApplicationContext context: Context
    ) = context.getString(R.string.app_name)

    @Provides
    @ActivityScoped
    @Named("Option1")
    fun provideOption1() = "I'm the option 1"

    @Provides
    @ActivityScoped
    @Named("Option2")
    fun provideOption2() = "I'm the option 2"
}
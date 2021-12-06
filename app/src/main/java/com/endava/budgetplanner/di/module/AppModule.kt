package com.endava.budgetplanner.di.module

import android.content.Context
import com.endava.budgetplanner.common.utils.AssetsManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(
    includes = [
        AppBindsModule::class,
        NetworkModule::class,
        SubcomponentsModule::class,
    ]
)
object AppModule {

    @Provides
    @Singleton
    fun provideAssetManager(context: Context) = AssetsManager(context)
}
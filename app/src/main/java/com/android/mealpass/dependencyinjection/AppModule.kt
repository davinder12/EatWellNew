package com.android.mealpass.dependencyinjection

import android.content.Context
import com.android.mealpass.data.service.PreferenceService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Provides
    @Singleton
    fun providePreferenceService(@ApplicationContext context: Context): PreferenceService {
        return PreferenceService(context)
    }

    @Provides
    @Singleton
    fun provideContext(@ApplicationContext context: Context): Context {
        return context
    }

}


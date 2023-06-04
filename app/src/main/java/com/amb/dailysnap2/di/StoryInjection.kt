package com.amb.dailysnap2.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.amb.dailysnap2.data.networking.api.ApiConfig
import com.amb.dailysnap2.data.preference.UserPreferences
import com.amb.dailysnap2.data.repository.StoryRepository

val Context.dataStore: DataStore<Preferences> by preferencesDataStore("DailySnap2")

object StoryInjection {
    fun provideRepository(context: Context): StoryRepository {
        val dataStore = context.dataStore
        val preferences = UserPreferences.getInstance(dataStore)
        val apiService = ApiConfig.getApiClient()
        return StoryRepository.getInstance(preferences, apiService)
    }
}
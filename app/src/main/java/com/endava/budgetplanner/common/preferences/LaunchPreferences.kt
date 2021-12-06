package com.endava.budgetplanner.common.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.endava.budgetplanner.common.base.DataStoreProvider
import javax.inject.Inject

private val Context.launchDataStore by preferencesDataStore("launch_datastore")

class LaunchPreferences @Inject constructor(
    private val context: Context
) : DataStoreProvider() {

    override fun provideDataStore(): DataStore<Preferences> {
        return context.launchDataStore
    }

    companion object {

        val LAUNCH_KEY = booleanPreferencesKey("launch_key")
    }
}
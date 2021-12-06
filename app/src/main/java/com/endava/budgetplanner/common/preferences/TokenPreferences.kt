package com.endava.budgetplanner.common.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.endava.budgetplanner.common.base.DataStoreProvider
import javax.inject.Inject

private const val KEY_TOKEN_NAME = "token_key"
private const val PREFERENCE_NAME = "token_datastore"

private val Context.tokenDataStore by preferencesDataStore(PREFERENCE_NAME)

class TokenPreferences @Inject constructor(private val context: Context) : DataStoreProvider() {

    override fun provideDataStore(): DataStore<Preferences> {
        return context.tokenDataStore
    }

    companion object {

        val TOKEN_KEY = stringPreferencesKey(KEY_TOKEN_NAME)
    }
}
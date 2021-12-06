package com.endava.budgetplanner.common.base

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

abstract class DataStoreProvider {

    abstract fun provideDataStore(): DataStore<Preferences>

    open suspend fun edit(edit: suspend (MutablePreferences) -> Unit) {
        provideDataStore().edit(edit) 
    }

    open suspend fun <T> getData(map: suspend (Preferences) -> T): Flow<T> {
        return provideDataStore().data.catch {
            if (it is IOException) {
                it.printStackTrace()
                emit(emptyPreferences())
            } else {
                throw it
            }
        }.map(map)
    }
}
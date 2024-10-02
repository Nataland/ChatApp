package com.nataland.chatapp.picker

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.nataland.chatapp.picker.CatInfo.toCat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class StoreCat(private val context: Context) {

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")
        val CAT_NAME_KEY = stringPreferencesKey("recent_cat_name")
    }

    val getCat: Flow<Cat?> = context.dataStore.data
        .map { preferences ->
            preferences[CAT_NAME_KEY]?.toCat() ?: Cat.Bobby
        }

    suspend fun saveCat(name: String) {
        context.dataStore.edit { preferences ->
            preferences[CAT_NAME_KEY] = name
        }
    }
}
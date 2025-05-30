package com.example.yemeksiparisapp.data.local

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore("favorites")

class FavoritesManager(private val context: Context) {

    private val FAVORITES_KEY: Preferences.Key<Set<String>> =
        stringSetPreferencesKey("favorite_items")

    val favoriYemekler: Flow<Set<String>> = context.dataStore.data
        .map { prefs -> prefs[FAVORITES_KEY] ?: emptySet<String>() }

    suspend fun favoriEkle(yemekAdi: String) {
        context.dataStore.edit { prefs ->
            val mevcut = prefs[FAVORITES_KEY]?.toMutableSet() ?: mutableSetOf()
            mevcut.add(yemekAdi)
            prefs[FAVORITES_KEY] = mevcut
        }
    }

    suspend fun favoriSil(yemekAdi: String) {
        context.dataStore.edit { prefs ->
            val mevcut = prefs[FAVORITES_KEY]?.toMutableSet() ?: mutableSetOf()
            mevcut.remove(yemekAdi)
            prefs[FAVORITES_KEY] = mevcut
        }
    }
}

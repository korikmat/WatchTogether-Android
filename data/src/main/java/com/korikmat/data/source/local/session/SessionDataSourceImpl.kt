package com.korikmat.data.source.local.session

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import kotlinx.coroutines.flow.map
import com.korikmat.data.source.local.SessionDataSource
import kotlinx.coroutines.flow.Flow

object SessionKeys {
    val USER_ID = longPreferencesKey("user_id")
}

class SessionDataSourceImpl(private val dataStore: DataStore<Preferences>) : SessionDataSource {

    override val currentUserId: Flow<Long?> = dataStore.data
        .map { it[SessionKeys.USER_ID] }

    override suspend fun setUser(id: Long) {
        dataStore.edit { prefs ->
            prefs[SessionKeys.USER_ID] = id
        }
    }

    override suspend fun clearUser() {
        dataStore.edit { prefs ->
            prefs.remove(SessionKeys.USER_ID)
        }
    }
}
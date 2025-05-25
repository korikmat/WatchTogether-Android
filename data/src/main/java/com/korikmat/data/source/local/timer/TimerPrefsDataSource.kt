package com.korikmat.data.source.local.timer

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class TimerPrefsDataSource(
    private val timerStore: DataStore<Preferences>
) {
    private object Keys {
        val START_EPOCH = longPreferencesKey("start_epoch_ms")
        val DURATION = longPreferencesKey("duration_ms")
    }

    val activeTimer: Flow<Pair<Long?, Long>?> = timerStore.data
        .map { prefs ->
            val start = prefs[Keys.START_EPOCH]
            val dur = prefs[Keys.DURATION] ?: return@map null
            start to dur
        }

    suspend fun save(startEpochMs: Long, durationMs: Long) {
        timerStore.edit { prefs ->
            prefs[Keys.START_EPOCH] = startEpochMs
            prefs[Keys.DURATION] = durationMs
        }
    }

    suspend fun clear() = timerStore.edit { prefs ->
        prefs.remove(Keys.START_EPOCH)
//        prefs.remove(Keys.DURATION)
    }
}
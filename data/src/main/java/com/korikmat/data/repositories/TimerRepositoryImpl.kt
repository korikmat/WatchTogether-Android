package com.korikmat.data.repositories

import com.korikmat.data.source.local.systemAlarm.SystemAlarmDataSource
import com.korikmat.data.source.local.timer.TimerPrefsDataSource
import com.korikmat.domain.repositories.TimerRepository
import com.korikmat.domain.usecases.ActiveTimer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TimerRepositoryImpl (
    private val alarm: SystemAlarmDataSource,
    private val prefs: TimerPrefsDataSource
) : TimerRepository {

    override suspend fun startTimer(durationMs: Long) {
        val start = System.currentTimeMillis()
        prefs.save(start, durationMs)
        alarm.schedule(durationMs)
    }

    override suspend fun cancelTimer() {
        alarm.cancel()
        prefs.clear()
    }

    override fun activeTimer(): Flow<ActiveTimer?> =
        prefs.activeTimer.map { it?.let { (s, d) -> ActiveTimer(s, d) } }
}
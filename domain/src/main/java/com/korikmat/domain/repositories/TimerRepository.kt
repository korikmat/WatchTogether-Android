package com.korikmat.domain.repositories

import com.korikmat.domain.usecases.ActiveTimer
import kotlinx.coroutines.flow.Flow

interface TimerRepository {
    suspend fun startTimer(durationMs: Long)
    suspend fun cancelTimer()
    fun activeTimer(): Flow<ActiveTimer?>
}
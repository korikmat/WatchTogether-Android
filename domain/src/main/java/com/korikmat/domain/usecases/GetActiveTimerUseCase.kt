package com.korikmat.domain.usecases

import com.korikmat.domain.repositories.TimerRepository
import kotlinx.coroutines.flow.Flow

data class ActiveTimer(val startEpochMs: Long?, val durationMs: Long)

class GetActiveTimerUseCase(
    private val repo: TimerRepository
) {
    operator fun invoke(): Flow<ActiveTimer?> = repo.activeTimer()
}
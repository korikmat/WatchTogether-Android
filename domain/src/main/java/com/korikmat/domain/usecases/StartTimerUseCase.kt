package com.korikmat.domain.usecases

import com.korikmat.domain.repositories.TimerRepository

class StartTimerUseCase(
    private val repo: TimerRepository
) {
    suspend operator fun invoke(durationMs: Long) =
        repo.startTimer(durationMs)
}
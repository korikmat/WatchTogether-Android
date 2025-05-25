package com.korikmat.domain.usecases

import com.korikmat.domain.repositories.TimerRepository

class CancelTimerUseCase (
    private val repo: TimerRepository
) {
    suspend operator fun invoke() = repo.cancelTimer()
}
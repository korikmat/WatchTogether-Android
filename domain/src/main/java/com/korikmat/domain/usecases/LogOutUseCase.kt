package com.korikmat.domain.usecases

import com.korikmat.domain.repositories.SessionRepository

class LogOutUseCase(private val sessionRepository: SessionRepository) {
    suspend operator fun invoke() {
        sessionRepository.clearCurrentUser()
    }
}
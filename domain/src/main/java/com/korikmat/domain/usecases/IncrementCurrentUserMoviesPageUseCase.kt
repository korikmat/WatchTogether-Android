package com.korikmat.domain.usecases

import com.korikmat.domain.repositories.SessionRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest

class IncrementCurrentUserMoviesPageUseCase(private val sessionRepository: SessionRepository) {
    suspend operator fun invoke() {
        sessionRepository.incrementUserMoviesPage(sessionRepository.currentUserId.first())
    }
}
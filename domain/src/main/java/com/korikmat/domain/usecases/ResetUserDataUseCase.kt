package com.korikmat.domain.usecases

import com.korikmat.domain.repositories.MoviesRepository
import com.korikmat.domain.repositories.SessionRepository
import kotlinx.coroutines.flow.first

class ResetUserDataUseCase(
    private val sessionRepository: SessionRepository,
    private val moviesRepository: MoviesRepository,
) {
    suspend operator fun invoke() {
        val userId = sessionRepository.currentUserId.first()
        sessionRepository.setUserMoviesPage(userId, 1)
        moviesRepository.clearRatedMoviesByUserId(userId)
    }
}
package com.korikmat.domain.usecases

import com.korikmat.domain.repositories.MoviesRepository
import com.korikmat.domain.repositories.SessionRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import java.io.IOException

class DislikeMoviesUseCase(
    private val moviesRepository: MoviesRepository,
    private val sessionRepository: SessionRepository
) {
    suspend fun execute(movieId: Set<Long>) {
        val uid = sessionRepository.currentUserId.first()
        for (id in movieId) {
            val movie = try {
                moviesRepository.getMovie(id).firstOrNull()
            } catch (e: IOException) {
                break
            } catch (e: Exception) {
                break
            }
            if (movie == null) {
                break
            }

            val updated = movie.copy(
                userId = uid,
                liked = false,
                interactedAt = System.currentTimeMillis()
            )

            moviesRepository.addRatedMovie(updated)
        }
    }
}

package com.korikmat.domain.usecases

import com.korikmat.domain.models.MovieDataModel
import com.korikmat.domain.repositories.MoviesRepository
import com.korikmat.domain.repositories.SessionRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import java.io.IOException

class GetMoviesUseCase(
    private val moviesRepository: MoviesRepository,
    private val sessionRepository: SessionRepository
) {

    operator fun invoke(): Flow<List<MovieDataModel>> = flow {
        val user = sessionRepository.currentUser().first()
        val uid = user?.id ?: 0
        var page = user?.currentMoviesPage ?: 1
        val threshold = 20

        val ratedIds = moviesRepository.fetchRated(uid)
            .first().map { it.id }

        val result = mutableListOf<MovieDataModel>()
        while (result.size < threshold) {
//            val response = moviesRepository.getPopularMovies(page).first()
            val response: List<MovieDataModel> = try {
                moviesRepository.getPopularMovies(page)
                    .firstOrNull()
                    .orEmpty()
            } catch (e: IOException) {
                delay(5000)
                continue
            } catch (e: Exception) {
                delay(5000)
                continue
            }

            val filtered = response.filter { it.id !in ratedIds }
            result += filtered

            if (result.size < threshold) {
                page++
            } else {
                break
            }
        }
        sessionRepository.setUserMoviesPage(uid, page)

        emit(result)
    }


}

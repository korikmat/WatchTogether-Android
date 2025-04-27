package com.korikmat.domain.usecases

import com.korikmat.domain.models.MovieDataModel
import com.korikmat.domain.repositories.MoviesRepository
import com.korikmat.domain.repositories.SessionRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class SearchInLikedMoviesUseCase(
    private val moviesRepository: MoviesRepository,
    private val sessionRepository: SessionRepository
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    fun execute(query: String): Flow<List<MovieDataModel>> =
        sessionRepository.currentUserId.flatMapLatest { id ->
            if (id == null) {
                flowOf(emptyList())
            } else {
                moviesRepository.fetchLiked(id).map { list ->
                    if (query.isBlank()) {
                        list
                    } else {
                        list.filter { movie ->
                            movie.title.contains(query, ignoreCase = true)
                                    || movie.releaseDate.contains(query)
                        }
                    }
                }
            }
        }

}
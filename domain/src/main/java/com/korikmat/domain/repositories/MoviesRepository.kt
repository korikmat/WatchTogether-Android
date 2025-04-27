package com.korikmat.domain.repositories

import com.korikmat.domain.models.MovieDataModel
import kotlinx.coroutines.flow.Flow

interface MoviesRepository {
    fun getPopularMovies(page: Int): Flow<List<MovieDataModel>>
    suspend fun getMovie(movieId: Long): Flow<MovieDataModel>
    suspend fun addRatedMovie(movie: MovieDataModel)
    fun fetchRated(uId: Long): Flow<List<MovieDataModel>>
    fun fetchLiked(uId: Long): Flow<List<MovieDataModel>>
    fun fetchDisliked(uId: Long): Flow<List<MovieDataModel>>
    suspend fun clearRatedMoviesByUserId(userId: Long?)
}
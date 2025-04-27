package com.korikmat.data.source.remote

import com.korikmat.data.source.remote.dto.MovieDto
import com.korikmat.data.source.remote.dto.MovieListDto
import retrofit2.Response

interface RemoteMoviesSource {
    suspend fun getPopularMovies(
        page: Int,
        language: String? = null
    ): Response<MovieListDto>

    suspend fun getTopRatedMovies(
        page: Int,
        language: String? = null
    ): Response<MovieListDto>

    suspend fun getMovie(
        movieId: Long,
        language: String? = null
    ): Response<MovieDto>
}
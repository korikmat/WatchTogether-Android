package com.korikmat.data.source.remote.tmdbService

import com.korikmat.data.source.remote.dto.MovieDto
import com.korikmat.data.source.remote.RemoteMoviesSource
import com.korikmat.data.source.remote.dto.MovieListDto
import retrofit2.Response

class TMDBRemoteMoviesImpl(private val apiService: TMDBApiService, private val apiKey: String) : RemoteMoviesSource {
    override suspend fun getPopularMovies(
        page: Int,
        language: String?
    ): Response<MovieListDto> {
        return apiService.getPopularMovies(apiKey = apiKey, page = page, language = language)
    }

    override suspend fun getTopRatedMovies(
        page: Int,
        language: String?
    ): Response<MovieListDto> {
        return apiService.getTopRatedMovies(apiKey, page, language)
    }

    override suspend fun getMovie(
        movieId: Long,
        language: String?
    ): Response<MovieDto> {
        return apiService.getMovieDetails(movieId, apiKey, language)
    }
}
package com.korikmat.data.source.remote.tmdbService


import com.korikmat.data.source.remote.dto.MovieDto
import com.korikmat.data.source.remote.dto.MovieListDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TMDBApiService {

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String,  // ваш API-ключ TMDb
        @Query("page") page: Int = 1,
        @Query("language") language: String? = null
    ): Response<MovieListDto>


    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int = 1,
        @Query("language") language: String? = null
    ): Response<MovieListDto>


    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Long,
        @Query("api_key") apiKey: String,
        @Query("language") language: String? = null
    ): Response<MovieDto>

}
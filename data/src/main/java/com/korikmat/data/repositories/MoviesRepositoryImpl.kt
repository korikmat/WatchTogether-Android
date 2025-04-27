package com.korikmat.data.repositories

import android.util.Log
import com.korikmat.data.source.remote.RemoteMoviesSource
import com.korikmat.domain.models.MovieDataModel
import com.korikmat.domain.repositories.MoviesRepository
import kotlinx.coroutines.flow.flow
import com.korikmat.data.mappers.*
import com.korikmat.data.source.local.DatabaseLocalSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import retrofit2.HttpException

class MoviesRepositoryImpl(
    private val remoteMoviesSource: RemoteMoviesSource,
    private val databaseLocalSource: DatabaseLocalSource
) : MoviesRepository {

    override fun getPopularMovies(page: Int) = flow<List<MovieDataModel>> {
        with(remoteMoviesSource.getPopularMovies(page)) {
            if (isSuccessful) {
                Log.d("Pages count", "Pages count: ${this.body()?.total_pages}")
                Log.d("Current Page", "Current Page: ${this.body()?.page}")
                Log.d("Total Results", "Total Results: ${this.body()?.total_results}")
                this.body()?.let { emit(it.toDomain()) }
            } else {
                Log.e("MoviesRepository", "Error: ${this.errorBody().toString()}")
                throw HttpException(this)
            }
        }

    }.catch { e ->
        e.printStackTrace()
        Log.e("MoviesRepository", "Error: ${e.message}", e)
        throw e
    }

    override suspend fun clearRatedMoviesByUserId(userId: Long?) {
        databaseLocalSource.ratedMovieDao().deleteAllByUserId(userId)
    }

    override suspend fun getMovie(movieId: Long) = flow<MovieDataModel> {
        with(remoteMoviesSource.getMovie(movieId)) {
            if (isSuccessful) {
                this.body()?.let { emit(it.toDomain()) }
            } else {
                Log.e("MoviesRepository", "Error: ${this.errorBody().toString()}")
                throw HttpException(this)
            }
        }
    }.catch { e ->
        e.printStackTrace()
        Log.e("MoviesRepository", "Error: ${e.message}", e)
        throw e
    }

    override suspend fun addRatedMovie(movie: MovieDataModel) {
        databaseLocalSource.ratedMovieDao().insert(movie.toEntity())

    }

    override fun fetchRated(uId: Long): Flow<List<MovieDataModel>> =
        databaseLocalSource.ratedMovieDao().getAll(uId).map { list -> list.map { it.toDomain() } }

    override fun fetchLiked(uId: Long): Flow<List<MovieDataModel>> =
        databaseLocalSource.ratedMovieDao().getLiked(uId).map { list -> list.map { it.toDomain() } }


    override fun fetchDisliked(uId: Long): Flow<List<MovieDataModel>> =
        databaseLocalSource.ratedMovieDao().getDisliked(uId).map { list -> list.map { it.toDomain() } }

}
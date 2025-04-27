package com.korikmat.data.mappers

import com.korikmat.data.source.local.roomDatabase.entities.RatedMovieEntity
import com.korikmat.data.source.local.roomDatabase.entities.UserEntity
import com.korikmat.data.source.remote.dto.MovieDto
import com.korikmat.data.source.remote.dto.MovieListDto
import com.korikmat.domain.models.MovieDataModel
import com.korikmat.domain.models.UserDataModel

fun MovieDto.toDomain(): MovieDataModel {
    return MovieDataModel(
        id = this.id,
        title = this.title ?: "",
        overview = this.overview ?: "",
        posterUrl = if (this.poster_path.isNullOrBlank()) ""
        else "https://image.tmdb.org/t/p/w500${this.poster_path}",
        releaseDate = this.release_date ?: "",
        rating = this.vote_average ?: 0.0,
        liked = false,
    )
}

fun MovieDataModel.toEntity(): RatedMovieEntity {
    return RatedMovieEntity(
        id = this.id,
        title = this.title,
        overview = this.overview,
        poster_path = this.posterUrl,
        release_date = this.releaseDate,
        rating = this.rating,
        userId = this.userId ?: 1,
        liked = this.liked,
        interactedAt = this.interactedAt,
    )
}

fun RatedMovieEntity.toDomain(): MovieDataModel {
    return MovieDataModel(
        id = this.id,
        title = this.title ?: "",
        overview = this.overview ?: "",
        posterUrl = if (this.poster_path.isNullOrBlank()) ""
        else "https://image.tmdb.org/t/p/w500${this.poster_path}",
        releaseDate = this.release_date ?: "",
        rating = this.rating ?: 0.0,
        userId = this.userId,
        liked = this.liked ?: false,
        interactedAt = this.interactedAt,
    )
}

fun MovieListDto.toDomain(): List<MovieDataModel> {
    return this.results.map { it.toDomain() }
}

fun UserEntity.toDomain(): UserDataModel {
    return UserDataModel(
        id = this.id,
        name = this.name,
        nickname = this.nickname,
        currentMoviesPage = this.currentMoviesPage,
        imageUri = imageUri,
    )
}

fun UserDataModel.toEntity(): UserEntity {
    return UserEntity(
        id = this.id,
        name = this.name,
        nickname = this.nickname,
        currentMoviesPage = this.currentMoviesPage,
        imageUri = this.imageUri,
    )
}

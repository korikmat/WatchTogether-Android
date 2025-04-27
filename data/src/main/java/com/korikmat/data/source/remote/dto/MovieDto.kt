package com.korikmat.data.source.remote.dto

data class MovieDto(
    val id: Long,
    val title: String?,
    val overview: String?,
    val poster_path: String?,
    val backdrop_path: String?,
    val release_date: String?,
    val vote_average: Double?,
    val genres: List<MovieGenreDto>?,
)

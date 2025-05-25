package com.korikmat.watchtogether.presentation.contracts

import com.korikmat.domain.models.MovieDataModel

class MovieSelectionContract {
    data class State(
        val movies: List<MovieDataModel> = emptyList(),
        val currentMovie: MovieDataModel,
        val secondMovie: MovieDataModel,
        val thirdMovie: MovieDataModel,
    )
}
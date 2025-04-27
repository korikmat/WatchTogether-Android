package com.korikmat.watchtogether.presentation.contracts

import com.korikmat.domain.models.MovieDataModel

class MovieSelectionContract {
    data class State(
        val currentMovie: MovieDataModel,
        val nextMovie: MovieDataModel? = null,
    )
}
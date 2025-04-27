package com.korikmat.watchtogether.presentation.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.korikmat.domain.models.MovieDataModel
import com.korikmat.domain.usecases.DislikeMoviesUseCase
import com.korikmat.domain.usecases.GetMoviesUseCase
import com.korikmat.domain.usecases.LikeMoviesUseCase
import com.korikmat.watchtogether.presentation.contracts.MovieSelectionContract
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MovieSelectionViewModel(
    private val getMoviesUseCase: GetMoviesUseCase,
    private val likeMoviesUseCase: LikeMoviesUseCase,
    private val dislikeMoviesUseCase: DislikeMoviesUseCase,
) : ViewModel() {

    private val moviesQueue = ArrayDeque<MovieDataModel>()

    private val _state = MutableStateFlow(
        MovieSelectionContract.State(
            currentMovie = MovieDataModel(
                id = 0,
                title = "",
                overview = "",
                posterUrl = "",
                releaseDate = "",
                rating = 0.0,
                liked = false,
            ),
            nextMovie = MovieDataModel(
                id = 0,
                title = "",
                overview = "",
                posterUrl = "",
                releaseDate = "",
                rating = 0.0,
                liked = false,
            ),
        )
    )
    val state: StateFlow<MovieSelectionContract.State> = _state

    init {
        nextMovie()
    }

    private fun updateState(newState: MovieSelectionContract.State) {
        _state.value = newState
    }


    private suspend fun getMoviesData() {
        getMoviesUseCase().first() {
            moviesQueue.addAll(it)
        }
    }

    private fun nextMovie() {
        viewModelScope.launch(Dispatchers.IO) {
            if (moviesQueue.size < 2) {
                getMoviesData()
            }
            val newState = _state.value.copy(
                currentMovie = moviesQueue.removeFirstOrNull() ?: MovieDataModel(
                    id = 0,
                    title = "",
                    overview = "",
                    posterUrl = "",
                    releaseDate = "",
                    rating = 0.0,
                    liked = false,
                ),
                nextMovie = moviesQueue.firstOrNull() ?: MovieDataModel(
                    id = 0,
                    title = "",
                    overview = "",
                    posterUrl = "",
                    releaseDate = "",
                    rating = 0.0,
                    liked = false,
                )
            )
            updateState(newState)
        }
    }

    fun likeMovie() {
        viewModelScope.launch(Dispatchers.IO) {

            likeMoviesUseCase.execute(setOf(_state.value.currentMovie.id))

            nextMovie()
        }

    }

    fun dislikeMovie() {
        viewModelScope.launch(Dispatchers.IO) {
            dislikeMoviesUseCase.execute(setOf(_state.value.currentMovie.id))

            nextMovie()
        }
    }
}
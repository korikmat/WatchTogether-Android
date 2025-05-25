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
                title = "Loading...",
                overview = "Loading...",
                posterUrl = "Loading...",
                releaseDate = "Loading...",
                rating = 0.0,
            ),
            secondMovie = MovieDataModel(
                id = 1,
                title = "Loading...",
                overview = "Loading...",
                posterUrl = "Loading...",
                releaseDate = "Loading...",
                rating = 0.0,
            ),
            thirdMovie = MovieDataModel(
                id = 2,
                title = "Loading...",
                overview = "Loading...",
                posterUrl = "Loading...",
                releaseDate = "Loading...",
                rating = 0.0,
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
            moviesQueue.clear()
            moviesQueue.addAll(it)
        }
    }

    private fun nextMovie() {
        viewModelScope.launch(Dispatchers.IO) {
            moviesQueue.removeFirstOrNull()
            if (moviesQueue.size <= 2) {
                getMoviesData()
//                newState = _state.value.copy(
//                    movies = moviesQueue.toList().reversed()
//                )
            }
            val newState = _state.value.copy(
                currentMovie = moviesQueue.first(),
                secondMovie = moviesQueue.getOrNull(1) ?: MovieDataModel(
                    id = 0,
                    title = "No more movies",
                    overview = "Please wait for more movies to be loaded.",
                    posterUrl = "",
                    releaseDate = "",
                    rating = 0.0,
                ),
                thirdMovie = moviesQueue.getOrNull(2) ?: MovieDataModel(
                    id = 0,
                    title = "No more movies",
                    overview = "Please wait for more movies to be loaded.",
                    posterUrl = "",
                    releaseDate = "",
                    rating = 0.0,
                ),
                movies = moviesQueue.toList().reversed()
            )
            updateState(newState)

        }
    }

    fun likeMovie() {
        viewModelScope.launch(Dispatchers.IO) {
            likeMoviesUseCase.execute(setOf(moviesQueue.first().id))
            nextMovie()
        }

    }

    fun dislikeMovie() {
        viewModelScope.launch(Dispatchers.IO) {
            dislikeMoviesUseCase.execute(setOf(moviesQueue.first().id))
            nextMovie()
        }
    }
}
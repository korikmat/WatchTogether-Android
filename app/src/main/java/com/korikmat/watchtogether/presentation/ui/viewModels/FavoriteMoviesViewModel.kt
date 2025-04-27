package com.korikmat.watchtogether.presentation.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.korikmat.domain.models.MovieDataModel
import com.korikmat.domain.usecases.DislikeMoviesUseCase
import com.korikmat.domain.usecases.SearchInLikedMoviesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class MoviesUiState(
    val query: String = "",
    val movies: List<MovieDataModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class FavoriteMoviesViewModel(
    private val searchInLikedMoviesUseCase: SearchInLikedMoviesUseCase,
    private val dislikeMoviesUseCase: DislikeMoviesUseCase,
) : ViewModel() {

    private val query = MutableStateFlow("")

    @OptIn(ExperimentalCoroutinesApi::class)
    val state: StateFlow<MoviesUiState> = combine(
        query,
        query.flatMapLatest { q ->
            searchInLikedMoviesUseCase.execute(q)
        }
    ) { query, movies ->
        MoviesUiState(
            query = query,
            movies = movies,
            isLoading = false,
            error = null
        )
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = MoviesUiState()
        )

    fun deleteMoviesById(moviesId: Set<Long>) {
        viewModelScope.launch(Dispatchers.IO) {
            dislikeMoviesUseCase.execute(moviesId)
        }
    }

    fun onQueryChange(newQuery: String) {
        query.value = newQuery
    }
}
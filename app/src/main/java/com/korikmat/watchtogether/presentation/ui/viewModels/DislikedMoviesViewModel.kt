package com.korikmat.watchtogether.presentation.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.korikmat.domain.usecases.LikeMoviesUseCase
import com.korikmat.domain.usecases.SearchInDislikedMoviesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DislikedMoviesViewModel(
    private val searchInDislikedMoviesUseCase: SearchInDislikedMoviesUseCase,
    private val likeMoviesUseCase: LikeMoviesUseCase,
) : ViewModel() {
    private val query = MutableStateFlow("")

    @OptIn(ExperimentalCoroutinesApi::class)
    val state: StateFlow<MoviesUiState> = combine(
        query,
        query.flatMapLatest { q ->
            searchInDislikedMoviesUseCase.execute(q)
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
            likeMoviesUseCase.execute(moviesId)
        }
    }

    fun onQueryChange(newQuery: String) {
        query.value = newQuery
    }
}
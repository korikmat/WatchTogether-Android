package com.korikmat.watchtogether.presentation.ui.viewModels

import androidx.lifecycle.ViewModel
import com.korikmat.domain.models.MovieDataModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MatchesViewModel() : ViewModel() {
    private val _state = MutableStateFlow(
        listOf<MovieDataModel>()
    )
    val state: StateFlow<List<MovieDataModel>> = _state

    private fun updateState(newState: List<MovieDataModel>) {
        _state.value = newState
    }

}
package com.korikmat.watchtogether.presentation.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.korikmat.domain.models.UserDataModel
import com.korikmat.domain.usecases.GetCurrentUserUseCase
import com.korikmat.domain.usecases.LogOutUseCase
import com.korikmat.domain.usecases.ResetUserDataUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class UserProfileViewModel(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val resetUserDataUseCase: ResetUserDataUseCase,
    private val logOutUseCase: LogOutUseCase,
) : ViewModel() {
    val state: StateFlow<UserDataModel> = getCurrentUserUseCase().map {
        it ?: UserDataModel(
            name = "Unknown",
            nickname = "Unknown",
        )
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = UserDataModel(
                name = "Loading",
                nickname = "Loading",
            )
        )

    fun resetRatedMovies() {
        viewModelScope.launch {
            resetUserDataUseCase()
        }
    }

    fun logout() {
        viewModelScope.launch {
            logOutUseCase()
        }
    }


}
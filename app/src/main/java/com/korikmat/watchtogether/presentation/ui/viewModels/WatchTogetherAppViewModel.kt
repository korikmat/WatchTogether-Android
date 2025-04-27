package com.korikmat.watchtogether.presentation.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.korikmat.domain.models.UserDataModel
import com.korikmat.domain.usecases.GetAllUsersUseCase
import com.korikmat.domain.usecases.GetCurrentUserUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class UsersUiState(
    val currentUser: UserDataModel? = UserDataModel(
        id = 0,
        name = "Loading",
        nickname = "Loading",
    ),
    val allUsers: List<UserDataModel> = emptyList(),
)

class WatchTogetherAppViewModel(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getAllUsersUseCase: GetAllUsersUseCase
) : ViewModel() {

    val state: StateFlow<UsersUiState> = combine(
        getCurrentUserUseCase(),
        getAllUsersUseCase(),
    ) { currentUser, allUsers ->
        UsersUiState(
            currentUser = currentUser,
            allUsers = allUsers,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = UsersUiState(),
    )

}
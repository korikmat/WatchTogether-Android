package com.korikmat.watchtogether.presentation.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.korikmat.domain.models.UserDataModel
import com.korikmat.domain.usecases.DeleteUsersUseCase
import com.korikmat.domain.usecases.GetAllUsersUseCase
import com.korikmat.domain.usecases.SetCurrentUserUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SelectUserScreenViewModel(
    private val getAllUsersUseCase: GetAllUsersUseCase,
    private val setCurrentUserUseCase: SetCurrentUserUseCase,
    private val deleteUserUseCase: DeleteUsersUseCase,
) : ViewModel() {
    val users: StateFlow<List<UserDataModel>> = getAllUsersUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = emptyList()
        )

    fun setCurrentUser(user: UserDataModel) {
        viewModelScope.launch {
            setCurrentUserUseCase(user)
        }
    }

    fun deleteUsers(usersId: Set<Long>) {
        viewModelScope.launch {
            deleteUserUseCase(usersId)
        }
    }
 }
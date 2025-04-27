package com.korikmat.watchtogether.presentation.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.korikmat.domain.models.UserDataModel
import com.korikmat.domain.usecases.GetCurrentUserUseCase
import com.korikmat.domain.usecases.UpdateUserDetailsUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class EditUserProfileViewModel(
    getCurrentUserUseCase: GetCurrentUserUseCase,
    private val updateUserDetailsUseCase: UpdateUserDetailsUseCase
): ViewModel() {
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

    fun updateUserProfile(name: String, nickname: String, imageUri: String?) {
        viewModelScope.launch {
            updateUserDetailsUseCase(
                name = name,
                nickname = nickname,
                imageUri = imageUri,
            )
        }
    }

}
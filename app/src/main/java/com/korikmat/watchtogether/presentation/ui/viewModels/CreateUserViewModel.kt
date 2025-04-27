package com.korikmat.watchtogether.presentation.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.korikmat.domain.models.UserDataModel
import com.korikmat.domain.usecases.CreateNewUserUseCase
import kotlinx.coroutines.launch

class CreateUserViewModel(private val createNewUserUseCase: CreateNewUserUseCase) : ViewModel() {
    fun createUser(name: String, nickname: String) {
        viewModelScope.launch {
            createNewUserUseCase(UserDataModel(
                name = name,
                nickname = nickname,
            ))
        }
    }
}
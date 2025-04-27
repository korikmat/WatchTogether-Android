package com.korikmat.domain.usecases

import com.korikmat.domain.models.UserDataModel
import com.korikmat.domain.repositories.SessionRepository
import kotlinx.coroutines.flow.first

class UpdateUserDetailsUseCase(private val sessionRepository: SessionRepository,) {
    suspend operator fun invoke(name: String, nickname: String, imageUri: String?) {
        val uId = sessionRepository.currentUserId.first()
        sessionRepository.updateUserData(UserDataModel(
            id = uId,
            name = name,
            nickname = nickname,
            imageUri = imageUri,
        ))
    }
}
package com.korikmat.domain.usecases

import com.korikmat.domain.models.UserDataModel
import com.korikmat.domain.repositories.SessionRepository

class CreateNewUserUseCase(
    private val sessionRepository: SessionRepository,
) {
    suspend operator fun invoke(user: UserDataModel) {
        val newUId = sessionRepository.createUser(user)
        sessionRepository.setCurrentUser(newUId)
    }
}
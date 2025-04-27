package com.korikmat.domain.usecases

import com.korikmat.domain.models.UserDataModel
import com.korikmat.domain.repositories.SessionRepository

class SetCurrentUserUseCase(
    private val sessionRepository: SessionRepository,
) {
    suspend operator fun invoke(user: UserDataModel) {
        sessionRepository.setCurrentUser(user.id?: 0)
    }
}
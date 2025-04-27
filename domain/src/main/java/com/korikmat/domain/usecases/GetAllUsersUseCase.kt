package com.korikmat.domain.usecases

import com.korikmat.domain.repositories.SessionRepository

class GetAllUsersUseCase(
    private val sessionRepository: SessionRepository,
) {
    operator fun invoke() = sessionRepository.getAllUsers()
}
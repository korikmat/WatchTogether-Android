package com.korikmat.domain.usecases

import com.korikmat.domain.repositories.SessionRepository

class DeleteUsersUseCase(private val sessionRepository: SessionRepository) {
    suspend operator fun invoke(uId: Set<Long>) {
        for (id in uId) {
            sessionRepository.deleteUserById(id)
        }
    }
}
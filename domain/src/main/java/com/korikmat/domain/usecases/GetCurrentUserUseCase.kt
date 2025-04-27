package com.korikmat.domain.usecases

import com.korikmat.domain.models.UserDataModel
import com.korikmat.domain.repositories.SessionRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class GetCurrentUserUseCase(
    private val sessionRepository: SessionRepository,
    scope: CoroutineScope
) {

    private val flow = sessionRepository.currentUser()
        .map {
            it
        }
        .stateIn(
            scope, SharingStarted.Lazily, UserDataModel(
                id = 1,
                name = "waiting",
                nickname = "waiting",
            )
        )

    operator fun invoke(): StateFlow<UserDataModel?> = flow
}
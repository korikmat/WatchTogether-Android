package com.korikmat.domain.repositories

import com.korikmat.domain.models.UserDataModel
import kotlinx.coroutines.flow.Flow

interface SessionRepository {
    suspend fun setCurrentUser(id: Long)
    fun currentUser(): Flow<UserDataModel?>
    val currentUserId: Flow<Long?>
    suspend fun updateUserData(user: UserDataModel)
    suspend fun incrementUserMoviesPage(id: Long?)
    suspend fun setUserMoviesPage(id: Long?, page: Int)
    suspend fun createUser(user: UserDataModel): Long
    fun getAllUsers(): Flow<List<UserDataModel>>
    suspend fun deleteUserById(id: Long)
    suspend fun clearCurrentUser()
}
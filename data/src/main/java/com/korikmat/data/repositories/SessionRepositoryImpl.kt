package com.korikmat.data.repositories

import com.korikmat.data.mappers.toDomain
import com.korikmat.data.mappers.toEntity
import com.korikmat.data.source.local.DatabaseLocalSource
import com.korikmat.data.source.local.SessionDataSource
import com.korikmat.domain.models.UserDataModel
import com.korikmat.domain.repositories.SessionRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class SessionRepositoryImpl(
    private val sessionDataSource: SessionDataSource,
    private val databaseLocalSource: DatabaseLocalSource
) : SessionRepository {

    override val currentUserId: Flow<Long?> =
        sessionDataSource.currentUserId

    override suspend fun setCurrentUser(id: Long) {
        sessionDataSource.setUser(id)
    }

    override suspend fun clearCurrentUser() {
        sessionDataSource.clearUser()
    }

    override suspend fun deleteUserById(id: Long) {
        databaseLocalSource.userDao().deleteUserById(id)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun currentUser(): Flow<UserDataModel?> =
        currentUserId.flatMapLatest { id ->
            if (id == null) flowOf(null)
            else databaseLocalSource.userDao()
                .getUserById(id)
                .map { it?.toDomain() }
        }

    override fun getAllUsers(): Flow<List<UserDataModel>> =
        databaseLocalSource.userDao()
            .getAll()
            .map { it.map { user -> user.toDomain() } }

    override suspend fun createUser(user: UserDataModel): Long =
        databaseLocalSource.userDao().insert(user.toEntity())


    override suspend fun setUserMoviesPage(id: Long?, page: Int) {
        databaseLocalSource.userDao().setUserMoviesPage(id, page)
    }

    override suspend fun updateUserData(user: UserDataModel) {
        databaseLocalSource.userDao().updateUser(user.toEntity())
    }

    override suspend fun incrementUserMoviesPage(id: Long?) {
        databaseLocalSource.userDao().incrementUserMoviesPage(id)
    }
}
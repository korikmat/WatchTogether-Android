package com.korikmat.data.source.local

import kotlinx.coroutines.flow.Flow

interface SessionDataSource {
    val currentUserId: Flow<Long?>

    suspend fun setUser(id: Long)
    suspend fun clearUser()
}
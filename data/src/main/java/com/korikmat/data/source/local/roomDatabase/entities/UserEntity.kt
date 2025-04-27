package com.korikmat.data.source.local.roomDatabase.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = 0L,
    val name: String,
    val nickname: String,
    val currentMoviesPage: Int = 1,
    val currentMovieInPage: Int = 0,
    val imageUri: String? = null,
    val createdAt: Long? = null,
    val updatedAt: Long? = null,
)

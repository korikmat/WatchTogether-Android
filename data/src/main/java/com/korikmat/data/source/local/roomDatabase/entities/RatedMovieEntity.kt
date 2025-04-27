package com.korikmat.data.source.local.roomDatabase.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "rated_movies",
    primaryKeys = ["id", "userId"],
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("userId"), Index("id")]
)
data class RatedMovieEntity(
    val id: Long,
    val title: String?,
    val overview: String?,
    val poster_path: String?,
    val release_date: String?,
    val rating: Double?,
    val userId: Long,
    val liked: Boolean?,
    val interactedAt: Long? = null
)
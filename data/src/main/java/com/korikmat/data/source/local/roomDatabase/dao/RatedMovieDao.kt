package com.korikmat.data.source.local.roomDatabase.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.korikmat.data.source.local.roomDatabase.entities.RatedMovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RatedMovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movie: RatedMovieEntity)

    @Query("SELECT * FROM rated_movies WHERE userId = :uId")
    fun getAll(uId: Long): Flow<List<RatedMovieEntity>>

    @Query("SELECT * FROM rated_movies WHERE liked = 1 AND userId = :uId ORDER BY interactedAt DESC")
    fun getLiked(uId: Long): Flow<List<RatedMovieEntity>>

    @Query("SELECT * FROM rated_movies WHERE liked = 0 AND userId = :uId ORDER BY interactedAt DESC")
    fun getDisliked(uId: Long): Flow<List<RatedMovieEntity>>

    @Query("DELETE FROM rated_movies WHERE userId = :userId")
    suspend fun deleteAllByUserId(userId: Long?)
}
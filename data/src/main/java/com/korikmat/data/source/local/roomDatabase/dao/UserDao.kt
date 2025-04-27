package com.korikmat.data.source.local.roomDatabase.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.korikmat.data.source.local.roomDatabase.entities.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: UserEntity): Long

    @Query("SELECT * FROM users")
    fun getAll(): Flow<List<UserEntity>>

    @Query("SELECT * FROM users WHERE id = :id")
    fun getUserById(id: Long): Flow<UserEntity?>

    @Query("DELETE FROM users WHERE id = :uId")
    suspend fun deleteUserById(uId: Long)

    @Upsert
    suspend fun updateUser(user: UserEntity)

    @Query(
        """
        UPDATE users
        SET currentMoviesPage = currentMoviesPage + 1
        WHERE id = :id
    """
    )
    suspend fun incrementUserMoviesPage(id: Long?)

    @Query(
        """
        UPDATE users
        SET currentMoviesPage = :page
        WHERE id = :id
    """
    )
    suspend fun setUserMoviesPage(id: Long?, page: Int)
}
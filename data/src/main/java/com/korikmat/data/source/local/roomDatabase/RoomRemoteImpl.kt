package com.korikmat.data.source.local.roomDatabase

import androidx.room.Database
import androidx.room.RoomDatabase
import com.korikmat.data.source.local.DatabaseLocalSource
import com.korikmat.data.source.local.roomDatabase.dao.RatedMovieDao
import com.korikmat.data.source.local.roomDatabase.dao.UserDao
import com.korikmat.data.source.local.roomDatabase.entities.RatedMovieEntity
import com.korikmat.data.source.local.roomDatabase.entities.UserEntity

@Database(entities = [RatedMovieEntity::class, UserEntity::class], version = 1)
abstract class RoomRemoteImpl : RoomDatabase(), DatabaseLocalSource {
    abstract override fun userDao(): UserDao
    abstract override fun ratedMovieDao(): RatedMovieDao
}
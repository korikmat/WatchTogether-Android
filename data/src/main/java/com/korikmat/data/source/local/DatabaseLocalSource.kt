package com.korikmat.data.source.local

import com.korikmat.data.source.local.roomDatabase.dao.RatedMovieDao
import com.korikmat.data.source.local.roomDatabase.dao.UserDao

interface DatabaseLocalSource {
    abstract fun userDao(): UserDao
    abstract fun ratedMovieDao(): RatedMovieDao
}
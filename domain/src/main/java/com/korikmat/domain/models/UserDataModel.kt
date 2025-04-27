package com.korikmat.domain.models


data class UserDataModel(
    val id: Long? = null,
    val name: String,
    val nickname: String,
    val imageUri: String? = null,
    val currentMoviesPage: Int = 1,

    )

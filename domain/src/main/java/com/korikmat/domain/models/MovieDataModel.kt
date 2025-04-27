package com.korikmat.domain.models

import java.time.LocalDateTime

data class MovieDataModel(
    val id: Long,
    val title: String,
    val overview: String,
    val posterUrl: String,
    val releaseDate: String,
    val rating: Double,
    var userId: Long? = null,
    var liked: Boolean? = null,
    var interactedAt: Long? = null,

    )

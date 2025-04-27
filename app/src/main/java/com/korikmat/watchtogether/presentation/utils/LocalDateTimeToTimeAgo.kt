package com.korikmat.watchtogether.presentation.utils

import java.time.Duration
import java.time.Instant

fun Long?.timeAgo(nowMillis: Long = System.currentTimeMillis()): String {
    if (this == null || this > nowMillis) return ""
    val duration = Duration.between(
        Instant.ofEpochMilli(this),
        Instant.ofEpochMilli(nowMillis)
    )

    val minutes = duration.toMinutes()
    val hours = duration.toHours()
    val days = duration.toDays()

    return when {
        minutes < 1 -> "just now"
        minutes < 60 -> "$minutes minute${if (minutes > 1) "s" else ""} ago"
        hours < 24 -> "$hours hour${if (hours > 1) "s" else ""} ago"
        days < 7 -> "$days day${if (days > 1) "s" else ""} ago"
        days < 30 -> {
            val weeks = days / 7
            "$weeks week${if (weeks > 1) "s" else ""} ago"
        }

        else -> {
            val months = days / 30
            "$months month${if (months > 1) "s" else ""} ago"
        }
    }
}
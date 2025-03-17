package com.korikmat.watchtogether.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.korikmat.watchtogether.R
import kotlinx.coroutines.launch

// Модель данных фильма
data class Movie(val title: String, val image: Painter, val description: String)



@Composable
fun MovieRatingScreen() {
    val sampleMovies = listOf(
        Movie("Фильм 1", painterResource(R.drawable.placeholder), "Описание фильма 1"),
        Movie("Фильм 2", painterResource(R.drawable.placeholder), "Описание фильма 2"),
        Movie("Фильм 3", painterResource(R.drawable.placeholder), "Описание фильма 3")
    )
    val movieList by remember { mutableStateOf(sampleMovies) }
    var currentIndex by remember { mutableIntStateOf(0) }

    if (currentIndex < movieList.size) {
        val currentMovie = movieList[currentIndex]
        val nextMovie = movieList.getOrNull(currentIndex + 1)
        MovieStack(
            currentMovie = currentMovie,
            nextMovie = nextMovie,
            onLike = { currentIndex++ },
            onDislike = { currentIndex++ }
        )
    } else {
        Text(
            text = "Фильмы закончились!",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.Center)
        )
    }
}

@Composable
fun UserProfileScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "Профиль пользователя", style = MaterialTheme.typography.titleLarge)
    }
}

@Composable
fun MovieStack(currentMovie: Movie, nextMovie: Movie?, onLike: () -> Unit, onDislike: () -> Unit) {
    val offsetX = remember { Animatable(0f) }
    val coroutineScope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 8.dp)) {
        nextMovie?.let {
            MovieCard(
                movie = it,
                modifier = Modifier.fillMaxSize()
            )
        }

        MovieCard(
            movie = currentMovie,
            modifier = Modifier
                .fillMaxSize()
                .offset { IntOffset(offsetX.value.toInt(), 0) }
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onDragEnd = {
                            coroutineScope.launch {
                                if (offsetX.value > 300) {
                                    offsetX.animateTo(1000f, animationSpec = tween(300))
                                    onLike()
                                    offsetX.snapTo(0f)
                                } else if (offsetX.value < -300) {
                                    offsetX.animateTo(-1000f, animationSpec = tween(300))
                                    onDislike()
                                    offsetX.snapTo(0f)
                                } else {
                                    offsetX.animateTo(0f, animationSpec = tween(300))
                                }
                            }
                        },
                        onHorizontalDrag = { change, dragAmount ->
                            change.consume()
                            coroutineScope.launch {
                                offsetX.snapTo(offsetX.value + dragAmount)
                            }
                        }
                    )
                }
        )
    }
}

@Composable
fun MovieCard(movie: Movie, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = movie.image,
                contentDescription = movie.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .statusBarsPadding()
            )
            Column(modifier = Modifier.padding(16.dp).weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = movie.title, style = MaterialTheme.typography.titleLarge)
            }
        }
    }
}



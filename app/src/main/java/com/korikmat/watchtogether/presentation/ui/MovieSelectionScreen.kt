package com.korikmat.watchtogether.presentation.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.korikmat.watchtogether.R
import kotlinx.coroutines.launch

// Модель данных фильма
data class Movie(val title: String, val image: Painter, val description: String)


@Preview
@Composable
fun MovieSelectionScreen() {
    val sampleMovies = listOf(
        Movie("Фильм 1", painterResource(R.drawable.placeholder2), "Описание фильма 1"),
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
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.Center)
        )
    }
}


@Composable
fun MovieStack(
    currentMovie: Movie,
    nextMovie: Movie?,
    onLike: () -> Unit,
    onDislike: () -> Unit,
) {
    val offsetX = remember { Animatable(0f) }
    val coroutineScope = rememberCoroutineScope()
    var showSheet by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 26.dp),
        contentAlignment = Alignment.BottomCenter
//            .padding(start = 16.dp, top = 90.dp, end = 16.dp, bottom = 26.dp)
    ) {
        nextMovie?.let {
            MovieCard2(
                movie = it,
//                modifier = Modifier.fillMaxSize()
            )
        }

        MovieCard2(
            movie = currentMovie,
            modifier = Modifier
//                .fillMaxSize()
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
                .clickable { showSheet = true }
        )
        if (showSheet) {
            MovieDetailsSheet(
                onDismissRequest = { showSheet = false },
                onBackClick = { /* сделать что-то или тоже закрыть */ }
            )
        }
    }
}

@Composable
fun MovieCard(movie: Movie, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(16f / 9f),
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
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = movie.title, style = MaterialTheme.typography.titleLarge)
            }
        }
    }
}

@Composable
fun MovieCard2(
    movie: Movie,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(9f / 16f),
//            .padding(8.dp), // Немного отступа вокруг карточки
//            .fillMaxWidth()
//            .height(250.dp), // Фиксированная высота, чтобы карточки выглядели аккуратнее
        shape = RoundedCornerShape(12.dp), // Скруглённые углы
        elevation = CardDefaults.cardElevation(8.dp) // Повышенная тень
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black),
        ) {
            // 1) Фоновая картинка
            Image(
                painter = movie.image,
                contentDescription = movie.title,
//                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxSize(),
                alignment = Alignment.TopCenter
            )

            // 2) Небольшой градиент в нижней части, чтобы текст читался лучше
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 1f)
                            ),
                            startY = 100f, // Начинаем затемнять ближе к низу
                            endY = 1500f
                        )
                    )
            )

            // 3) Название фильма поверх изображения
            Column(verticalArrangement = Arrangement.Bottom, modifier = Modifier.fillMaxSize()) {
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = Color.White
                    ),
                    modifier = Modifier

//                        .padding(vertical = 150.dp, horizontal = 26.dp)
                        .padding(start = 26.dp, top = 0.dp, end = 16.dp, bottom = 8.dp)
                        .fillMaxWidth()
                )
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(0.9f).padding(bottom = 8.dp).align(Alignment.CenterHorizontally),
                    color = Color.Gray
                )
                Text(
                    text = movie.description,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color.White
                    ),
                    modifier = Modifier
                        .padding(start = 26.dp, top = 0.dp, end = 16.dp, bottom = 100.dp)
                        .fillMaxWidth()
                )
            }
        }
    }
}


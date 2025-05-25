package com.korikmat.watchtogether.presentation.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.korikmat.domain.models.MovieDataModel
import com.korikmat.watchtogether.R
import com.korikmat.watchtogether.presentation.ui.viewModels.MovieSelectionViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import kotlin.math.sqrt


@Composable
fun MovieSelectionScreen(moviesViewModel: MovieSelectionViewModel = koinViewModel()) {

    val uiState = moviesViewModel.state.collectAsState().value

    MovieStack(
        movies = uiState.movies,
        currentMovie = uiState.currentMovie,
        secondMovie = uiState.secondMovie,
        thirdMovie = uiState.thirdMovie,
        onLike = { moviesViewModel.likeMovie() },
        onDislike = { moviesViewModel.dislikeMovie() },
    )
}

@Composable
fun MovieStack(
    movies: List<MovieDataModel> = emptyList(),
    currentMovie: MovieDataModel,
    secondMovie: MovieDataModel,
    thirdMovie: MovieDataModel,
    onLike: () -> Unit,
    onDislike: () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 30.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        SwipeText(
            swipeRightText = stringResource(R.string.swipe_right),
            swipeLeftText = stringResource(R.string.swipe_left)
        )

        movies.forEach {
            if (it.id == currentMovie.id || it.id == secondMovie.id) {
                AnimatedMovieCard(
                    movie = it,
                    onLike = onLike,
                    onDislike = onDislike,
                    coroutineScope = coroutineScope,
                )
            }
            else if (it.id == thirdMovie.id) {
                MovieCard(
                    movie = it
                )
            }
        }
    }
}

@Composable
fun SwipeText(
    swipeRightText: String,
    swipeLeftText: String
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    )
    {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp), horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = swipeRightText,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier
                    .padding(top = 8.dp, bottom = 8.dp, start = 4.dp, end = 4.dp)

            )
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 4.dp, end = 4.dp)
                    .align(Alignment.CenterVertically),
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 4.dp, end = 4.dp)
                    .align(Alignment.CenterVertically)
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp), horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                painter = painterResource(id = R.drawable.outline_heart_broken_24),
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 4.dp, end = 4.dp)
                    .align(Alignment.CenterVertically)
            )
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 4.dp, end = 4.dp)
                    .align(Alignment.CenterVertically),
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Text(
                text = swipeLeftText,
                style = MaterialTheme.typography.headlineSmall,

                modifier = Modifier
                    .padding(top = 8.dp, bottom = 8.dp, start = 4.dp, end = 4.dp)

            )
        }
    }
}

@Composable
fun AnimatedMovieCard(
    modifier: Modifier = Modifier,
    movie: MovieDataModel,
    onLike: () -> Unit = {},
    onDislike: () -> Unit = {},
    coroutineScope: CoroutineScope,
) {
    val offsetX = remember { Animatable(0f) }
    val offsetY = remember { Animatable(0f) }

    var showSheet by remember(movie.id) { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .offset { IntOffset(offsetX.value.toInt(), offsetY.value.toInt()) }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragEnd = {
                        val dx = offsetX.value
                        val dy = offsetY.value
                        val distance = sqrt(dx * dx + dy * dy)

                        // If the user swiped far enough
                        if (distance > 300f) {

                            // normalizes a vector
                            val ux = dx / distance
                            val uy = dy / distance
                            val targetDistance = 3000f

                            coroutineScope.launch {
                                // animate X and Y simultaneously
                                val xAnimation = launch {
                                    offsetX.animateTo(
                                        targetValue = ux * targetDistance,
                                        animationSpec = tween(600)
                                    )
                                }
                                val yAnimation = launch {
                                    offsetY.animateTo(
                                        targetValue = uy * targetDistance,
                                        animationSpec = tween(600)
                                    )
                                }

                                xAnimation.join()
                                yAnimation.join()


//                                    offsetX.snapTo(0f)
//                                    offsetY.snapTo(0f)
                                if (dx > 0) onLike() else onDislike()
                            }
                        } else {
                            // too short — return back
                            coroutineScope.launch {
                                launch {
                                    offsetX.animateTo(0f, tween(300))
                                }
                                launch {
                                    offsetY.animateTo(0f, tween(300))
                                }
                            }
                        }
                    },
                    onDragCancel = {
                        coroutineScope.launch {
                            offsetX.animateTo(0f, animationSpec = tween(300))
                            offsetY.animateTo(0f, animationSpec = tween(300))
                        }
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        coroutineScope.launch {
                            offsetX.snapTo(offsetX.value + dragAmount.x)
                            offsetY.snapTo(offsetY.value + dragAmount.y)
                        }
                    }
                )
            }
            .then(modifier)
            .fillMaxWidth()
            .aspectRatio(500f / 750f)
            .clip(RoundedCornerShape(12.dp))
            .clickable { showSheet = true },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent),
        ) {
            AsyncImage(
                model = movie.posterUrl.takeIf { it.isNotBlank() },
                contentDescription = movie.title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                alignment = Alignment.TopCenter
            )
        }
    }

    if (showSheet) {
        MovieDetailsSheet(
            movie = movie,
            onDismissRequest = { showSheet = false },
            onBackClick = { showSheet = false }
        )
    }
}

@Composable
fun MovieCard(
    movie: MovieDataModel,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(500f / 750f),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent),
        ) {
            AsyncImage(
                model = movie.posterUrl.takeIf { it.isNotBlank() },
                contentDescription = movie.title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                alignment = Alignment.TopCenter
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MovieSelectionScreenPreview() {
    MovieStack(
        currentMovie = MovieDataModel(
            id = 0,
            title = "...",
            posterUrl = "",
            releaseDate = "...",
            overview = "...",
            rating = 0.0,
        ),
        secondMovie = MovieDataModel(
            id = 1,
            title = "...",
            posterUrl = "",
            releaseDate = "...",
            overview = "...",
            rating = 0.0,
        ),
        thirdMovie = MovieDataModel(
            id = 2,
            title = "...",
            posterUrl = "",
            releaseDate = "...",
            overview = "...",
            rating = 0.0,
        ),
        onLike = {},
        onDislike = {},
    )
}

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.korikmat.domain.models.MovieDataModel
import com.korikmat.watchtogether.R
import com.korikmat.watchtogether.presentation.ui.viewModels.MovieSelectionViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import kotlin.math.sqrt


@Composable
fun MovieSelectionScreen(moviesViewModel: MovieSelectionViewModel = koinViewModel()) {

    val uiState = moviesViewModel.state.collectAsState().value

    MovieStack(
        currentMovie = uiState.currentMovie,
        nextMovie = uiState.nextMovie,
        onLike = { moviesViewModel.likeMovie() },
        onDislike = { moviesViewModel.dislikeMovie() },
    )

}

@Composable
fun MovieStack(
    currentMovie: MovieDataModel,
    nextMovie: MovieDataModel?,
    onLike: () -> Unit,
    onDislike: () -> Unit,
) {
    val offsetX = remember { Animatable(0f) }
    val offsetY = remember { Animatable(0f) }


    val coroutineScope = rememberCoroutineScope()
    var showSheet by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 30.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        )
        {
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp), horizontalArrangement = Arrangement.End){
                Text(
                    text = stringResource(R.string.swipe_right),
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier
                        .padding(top = 8.dp, bottom = 8.dp, start = 4.dp, end = 4.dp)

                )
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(start = 4.dp, end = 4.dp)
                        .align(Alignment.CenterVertically)
                )
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(start = 4.dp, end = 4.dp)
                        .align(Alignment.CenterVertically)
                )
            }
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp), horizontalArrangement = Arrangement.Start) {
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
                        .align(Alignment.CenterVertically)
                )
                Text(
                    text = stringResource(R.string.swipe_left),
                    style = MaterialTheme.typography.headlineSmall,

                    modifier = Modifier
                        .padding(top = 8.dp, bottom = 8.dp, start = 4.dp, end = 4.dp)

                )
            }
        }

        nextMovie?.let {
            MovieCard(
                movie = it,
            )
        }

        MovieCard(
            movie = currentMovie,
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
                                val targetDistance = 1900f

                                coroutineScope.launch {
                                    // animate X and Y simultaneously
                                    val xAnimation = launch {
                                        offsetX.animateTo(
                                            targetValue = ux * targetDistance,
                                            animationSpec = tween(400)
                                        )
                                    }
                                    val yAnimation = launch {
                                        offsetY.animateTo(
                                            targetValue = uy * targetDistance,
                                            animationSpec = tween(400)
                                        )
                                    }

                                    xAnimation.join()
                                    yAnimation.join()

                                    if (dx > 0) onLike() else onDislike()

                                    offsetX.snapTo(0f)
                                    offsetY.snapTo(0f)
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
                .clickable { showSheet = true }
        )

        if (showSheet) {
            MovieDetailsSheet(
                movie = currentMovie,
                onDismissRequest = { showSheet = false },
                onBackClick = { showSheet = false }
            )
        }
    }
}

@Composable
fun MovieCard(
    movie: MovieDataModel,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(500f / 750f),
        shape = RoundedCornerShape(12.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black),
        ) {
            AsyncImage(
                model = movie.posterUrl.takeIf { it.isNotBlank() },
                contentDescription = movie.title,
                modifier = Modifier.fillMaxSize(),
                alignment = Alignment.TopCenter
            )

//            Box(
//                modifier = Modifier
//                    .matchParentSize()
//                    .background(
//                        Brush.verticalGradient(
//                            listOf(
//                                Color.Transparent,
//                                Color.Black.copy(alpha = 1f)
//                            ),
//                            startY = 100f,
//                            endY = 1500f
//                        )
//                    )
//            )

//            Column(verticalArrangement = Arrangement.Bottom, modifier = Modifier.fillMaxSize()) {
//                Text(
//                    text = movie.title,
//                    style = MaterialTheme.typography.headlineMedium.copy(
//                        color = Color.White
//                    ),
//                    modifier = Modifier
//
////                        .padding(vertical = 150.dp, horizontal = 26.dp)
//                        .fillMaxWidth()
////                        .padding(start = 100.dp, top = 0.dp, end = 16.dp, bottom = 8.dp)
//
//                        .align(Alignment.CenterHorizontally)
//                )
//                HorizontalDivider(
//                    modifier = Modifier
//                        .fillMaxWidth(0.9f)
//                        .padding(bottom = 50.dp)
//                        .align(Alignment.CenterHorizontally),
//                    color = Color.Gray
//                )
//                Text(
//                    text = movie.overview,
//                    style = MaterialTheme.typography.bodyMedium.copy(
//                        color = Color.White
//                    ),
//                    modifier = Modifier
//                        .padding(start = 26.dp, top = 0.dp, end = 16.dp, bottom = 100.dp)
//                        .fillMaxWidth()
//                )
//            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MovieSelectionScreenPreview() {
    MovieStack(
        currentMovie = MovieDataModel(
            id = 1,
            title = "Movie Title",
            overview = "This is a brief overview of the movie.",
            posterUrl = "https://example.com/poster.jpg",
            releaseDate = "2023-10-01",
            rating = 8.5,
        ),
        nextMovie = null,
        onLike = {},
        onDislike = {}
    )
}

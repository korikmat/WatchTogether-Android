package com.korikmat.watchtogether.presentation.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.korikmat.domain.models.MovieDataModel
import com.korikmat.watchtogether.R
import com.korikmat.watchtogether.presentation.ui.viewModels.MatchesViewModel
import com.korikmat.watchtogether.presentation.utils.timeAgo
import org.koin.androidx.compose.koinViewModel

@Composable
fun MatchesScreen(
    matchesViewModel: MatchesViewModel = koinViewModel(),
    onFavoritesClick: () -> Unit,
    onDislikedClick: () -> Unit,
) {
    val movies = matchesViewModel.state.collectAsState().value

    Matches(movies, onFavoritesClick, onDislikedClick)

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Matches(movies: List<MovieDataModel>, onFavoritesClick: () -> Unit, onDislikedClick: () -> Unit) {
    var selectedMovie by remember { mutableStateOf<MovieDataModel?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        CenterAlignedTopAppBar(
            navigationIcon = {
                IconButton(onClick = onDislikedClick) {
                    Icon(
                        painter = painterResource(id = R.drawable.outline_heart_broken_24),
                        contentDescription = stringResource(R.string.disliked_movies)
                    )
                }
            },
            title = {
                Text(stringResource(R.string.matches))
            },
            actions = {
                IconButton(onClick = onFavoritesClick) {
                    Icon(
                        Icons.Default.FavoriteBorder,
                        contentDescription = stringResource(R.string.show_my_favorites)
                    )
                }
            },
            windowInsets = WindowInsets(0)
        )
        if (movies.isEmpty()) {

            Text(
                text = stringResource(R.string.not_implemented_yet),
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            )

        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(20.dp)),
                verticalArrangement = Arrangement.spacedBy(
                    space = 8.dp,
                )
            ) {
                items(
                    items = movies,
                    key = { it.id },
                ) { movie ->
                    MovieItem(
                        posterUrl = movie.posterUrl,
                        title = movie.title,
                        releaseDate = movie.releaseDate,
                        time = movie.interactedAt.timeAgo(),
                        onMovieClicked = { selectedMovie = movie },
                        modifier = Modifier
                            .height(120.dp)
                            .padding(start = 16.dp, top = 8.dp, end = 16.dp)
                    )

                }
            }
        }
    }


    selectedMovie?.let { movie ->
        MovieDetailsSheet(
            movie = movie,
            onDismissRequest = { selectedMovie = null },
            onBackClick = { selectedMovie = null }
        )
    }
}


@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun MatchesScreenPreview() {
    Matches(listOf(), onFavoritesClick = {}, onDislikedClick = {})
}
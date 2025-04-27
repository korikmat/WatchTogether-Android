package com.korikmat.watchtogether.presentation.ui.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.korikmat.watchtogether.R
import com.korikmat.watchtogether.presentation.ui.viewModels.DislikedMoviesViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun DislikedMoviesScreen(
    dislikedMoviesViewModel: DislikedMoviesViewModel = koinViewModel(),
    onBackClick: () -> Unit
) {
    val movies = dislikedMoviesViewModel.state.collectAsState().value.movies
    val query = dislikedMoviesViewModel.state.collectAsState().value.query
    MoviesList(
        movies = movies,
        query = query,
        topBarTitle = stringResource(R.string.disliked_movies),
        onSelectionIcon = {
            Icon(
                Icons.Default.Favorite,
                contentDescription = stringResource(R.string.liked_movies)
            )
        },
        onQueryChange = dislikedMoviesViewModel::onQueryChange,
        onBackClick = onBackClick,
        onDeleteMovies = dislikedMoviesViewModel::deleteMoviesById
    )
}
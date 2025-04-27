package com.korikmat.watchtogether.presentation.ui.screens

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.korikmat.watchtogether.R
import com.korikmat.watchtogether.presentation.ui.viewModels.FavoriteMoviesViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun FavoriteMoviesScreen(
    favoriteMoviesViewModel: FavoriteMoviesViewModel = koinViewModel(),
    onBackClick: () -> Unit
) {
    val movies = favoriteMoviesViewModel.state.collectAsState().value.movies
    val query = favoriteMoviesViewModel.state.collectAsState().value.query
    MoviesList(
        movies = movies,
        query = query,
        topBarTitle = stringResource(R.string.liked_movies),
        onSelectionIcon = {
            Icon(
                painter = painterResource(id = R.drawable.outline_heart_broken_24),
                contentDescription = stringResource(R.string.delete)
            )
        },
        onQueryChange = favoriteMoviesViewModel::onQueryChange,
        onBackClick = onBackClick,
        onDeleteMovies = favoriteMoviesViewModel::deleteMoviesById
    )
}
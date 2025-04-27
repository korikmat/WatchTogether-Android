package com.korikmat.watchtogether.presentation.ui.screens

import android.graphics.drawable.Icon
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.korikmat.domain.models.MovieDataModel
import com.korikmat.watchtogether.R
import com.korikmat.watchtogether.presentation.utils.timeAgo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoviesList(
    movies: List<MovieDataModel>,
    query: String,
    topBarTitle: String,
    onSelectionIcon: @Composable () -> Unit,
    onQueryChange: (String) -> Unit,
    onBackClick: () -> Unit,
    onDeleteMovies: (Set<Long>) -> Unit,
) {
    var selectionMode by rememberSaveable { mutableStateOf(false) }
    var selectedIds by rememberSaveable { mutableStateOf(setOf<Long>()) }
    var showDialog by remember { mutableStateOf(false) }

    var selectedMovie by remember { mutableStateOf<MovieDataModel?>(null) }
    Scaffold(
        topBar = {
            if (selectionMode) {
                CenterAlignedTopAppBar(
                    title = { Text("${selectedIds.size}") },
                    navigationIcon = {
                        IconButton(onClick = {
                            selectionMode = false; selectedIds = emptySet()
                        }) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = stringResource(R.string.cancel)
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = {
                            showDialog = true
                        }) {
                            onSelectionIcon()
                        }
                    }
                )
            } else {
                CenterAlignedTopAppBar(
                    title = {
                        Text(topBarTitle)
                    },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(
                                    R.string.back
                                )
                            )
                        }
                    },
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                verticalArrangement = Arrangement.spacedBy(
                    space = 8.dp,

                    ),
                contentPadding = PaddingValues(
                    bottom = 70.dp
                ),
            ) {
                items(
                    items = movies,
                    key = { it.id },
                ) { movie ->
                    val isSelected = movie.id in selectedIds
                    SwipeableItem(
                        onDeleteClicked = {
                            selectedIds += movie.id
                            showDialog = true
                        },
                        selectionMode = selectionMode,
                        modifier = Modifier
                            .height(120.dp)
                            .padding(start = 16.dp, top = 8.dp, end = 16.dp)
                    ) {
                        MovieItem(
                            posterUrl = movie.posterUrl,
                            title = movie.title,
                            releaseDate = movie.releaseDate,
                            time = movie.interactedAt.timeAgo(),
                            selected = isSelected && selectionMode,
                            onMovieClicked = {
                                if (selectionMode) {
                                    selectedIds = if (isSelected)
                                        selectedIds - movie.id
                                    else
                                        selectedIds + movie.id
                                    if (selectedIds.isEmpty()) selectionMode = false
                                } else {
                                    selectedMovie = movie
                                }
                            },
                            onLongClick = {
                                if (!selectionMode) {
                                    selectionMode = true
                                    selectedIds = setOf(movie.id)
                                }
                            },
                        )
                    }

                }
            }
            TextField(
                value = query,
                onValueChange = { onQueryChange(it) },
                label = { Text(stringResource(R.string.search)) },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = stringResource(R.string.search)
                    )
                },
                singleLine = true,
                modifier = Modifier
                    .padding(start = 16.dp, bottom = 35.dp, end = 16.dp)
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .shadow(shape = RoundedCornerShape(20.dp), elevation = 4.dp),
                shape = RoundedCornerShape(20.dp),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent
                ),
            )
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(stringResource(R.string.delete_movie_s)) },
            text = { Text(stringResource(R.string.are_you_sure_you_want_to_delete_it)) },
            confirmButton = {
                TextButton(onClick = {
                    onDeleteMovies(selectedIds)
                    selectedIds = emptySet()
                    selectionMode = false
                    showDialog = false
                }) { Text(stringResource(R.string.yes)) }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) { Text(stringResource(R.string.no)) }
            }
        )
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
fun MoviesListPreview() {

    MoviesList(
        movies = listOf(
            MovieDataModel(
                id = 1,
                title = "Movie Title",
                posterUrl = "https://image.tmdb.org/t/p/w500/t6HJH3gXtUqVinyFKWi7Bjh73TM.jpg",
                interactedAt = System.currentTimeMillis(),
                overview = "A brief overview of the movie.",
                releaseDate = "2023-01-01",
                rating = 4.5,
                liked = true
            )
        ),
        query = "",
        topBarTitle = "Favorite Movies",
        onSelectionIcon = {

        },
        onQueryChange = {},
        onBackClick = {},
        onDeleteMovies = {},

        )
}
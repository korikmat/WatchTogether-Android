package com.korikmat.watchtogether.presentation.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.korikmat.watchtogether.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenreSelectionScreen(
    availableGenres: List<String> = listOf(
        "Action", "Comedy", "Drama", "Horror", "Romance", "Sci-Fi"
    ),
    initialSelectedGenres: List<String> = emptyList(),
    onBackClick: () -> Unit,
    onSaveClick: (List<String>) -> Unit
) {
    val selectedGenres = remember {
        mutableStateOf(initialSelectedGenres.toMutableSet())
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(stringResource(R.string.select_genres)) }, navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back)
                    )
                }
            }, actions = {
                TextButton(
                    onClick = {
                        onSaveClick(selectedGenres.value.toList())
                    }) {
                    Text(stringResource(R.string.not_implemented_yet), fontWeight = FontWeight.Bold)
                }
            })
        }) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Text(
                text = stringResource(R.string.select_your_favorite_genres_to_get_movies_tailored_to_your_taste),
                style = MaterialTheme.typography.titleMedium
            )
            FlowRow(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
            ) {


                availableGenres.forEach { genre ->
                    val isSelected = genre in selectedGenres.value

                    FilterChip(
                        selected = isSelected, onClick = {
                            selectedGenres.value = selectedGenres.value.toMutableSet().apply {
                                if (isSelected) remove(genre) else add(genre)
                            }
                        }, label = {
                            Text(genre)
                        },

                        trailingIcon = {
                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.Check, contentDescription = null
                                )
                            }
                        }, shape = RoundedCornerShape(16.dp), modifier = Modifier
                            .height(56.dp)
                    )
                }
            }
        }
    }
}

@Preview(
    showBackground = true, showSystemUi = true
)
@Composable
fun GenreSelectionScreenPreview() {
    GenreSelectionScreen(
        availableGenres = listOf("Action", "Comedy", "Drama", "Horror", "Romance", "Sci-Fi"),
        initialSelectedGenres = listOf("Action", "Comedy"),
        onBackClick = { },
        onSaveClick = { })
}
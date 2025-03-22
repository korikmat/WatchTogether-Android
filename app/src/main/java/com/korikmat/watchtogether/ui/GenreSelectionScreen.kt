package com.korikmat.watchtogether.ui

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun GenreSelectionScreen(
    availableGenres: List<String> = listOf(
        "Action", "Comedy", "Drama", "Horror", "Romance", "Sci-Fi"
    ),
    initialSelectedGenres: List<String> = emptyList(),
    onBackClick: () -> Unit,
    onSaveClick: (List<String>) -> Unit
) {
    // Состояние выбранных категорий
    // Превращаем initialSelectedGenres в MutableSet, чтобы удобно добавлять/удалять
    val selectedGenres = remember {
        mutableStateOf(initialSelectedGenres.toMutableSet())
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Выбрать категории") }, navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Назад"
                    )
                }
            }, actions = {
                // Кнопка "Сохранить"
                TextButton(
                    onClick = {
                        // Передаём выбранные жанры наверх
                        onSaveClick(selectedGenres.value.toList())
                    }) {
                    Text("Сохранить", fontWeight = FontWeight.Bold)
                }
            })
        }) { paddingValues ->
        // Основной контент экрана
        FlowRow(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = "Выберите любимые жанры, чтобы получать фильмы по вкусу:",
                style = MaterialTheme.typography.titleMedium
            )

            // Список категорий (FilterChip'ы)
            availableGenres.forEach { genre ->
                val isSelected = genre in selectedGenres.value

                // FilterChip - современный вариант, можно заменить на Checkbox + Text
                FilterChip(
                    selected = isSelected, onClick = {
                    // При клике добавляем/убираем из selectedGenres
                    selectedGenres.value = selectedGenres.value.toMutableSet().apply {
                        if (isSelected) remove(genre) else add(genre)
                    }
                }, label = {
                    Text(genre)
                },
//                    leadingIcon = {
//                        // Иконка (галочка), если чип выбран
//                        if (isSelected) {
//                            Icon(
//                                imageVector = Icons.Default.Check,
//                                contentDescription = null
//                            )
//                        }
//                    },
                    trailingIcon = {
                        // Иконка (галочка), если чип выбран
                        if (isSelected) {
                            Icon(
                                imageVector = Icons.Default.Check, contentDescription = null
                            )
                        }
                    }, shape = RoundedCornerShape(16.dp), modifier = Modifier
//                        .fillMaxWidth()
                        .height(56.dp)
                )
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
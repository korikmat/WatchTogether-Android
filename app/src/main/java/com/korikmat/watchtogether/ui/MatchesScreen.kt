package com.korikmat.watchtogether.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding

import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import com.korikmat.watchtogether.R

data class Movie1(
    val title: String,
    @DrawableRes val posterResId: Int
)

@Composable
fun MatchesScreen() {
    val movies = listOf(
        Movie1("Хантер", R.drawable.placeholder),
        Movie1("Матрица", R.drawable.placeholder),
        Movie1("Интерстеллар", R.drawable.placeholder),
        Movie1("Матрица", R.drawable.placeholder),
        Movie1("Матрица", R.drawable.placeholder),
        Movie1("Матрица", R.drawable.placeholder),
        Movie1("Матрица", R.drawable.placeholder),
        Movie1("Матрица", R.drawable.placeholder),
        Movie1("Матрица", R.drawable.placeholder),
        Movie1("Матрица", R.drawable.placeholder),
        Movie1("Матрица", R.drawable.placeholder),
        Movie1("Матрица", R.drawable.placeholder),
        Movie1("Матрица", R.drawable.placeholder),
        Movie1("Матрица", R.drawable.placeholder),
    )
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(
            space = 8.dp,
            alignment = Alignment.Bottom
        )
    ) {
        items(movies) { movie ->
            MovieItem(movie)
        }
    }
}

@Composable
fun MovieItem(movie: Movie1) {
    Card(
        // Отступ вокруг карточки
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, top = 8.dp, end = 16.dp),
        // Закруглённые углы
        shape = RoundedCornerShape(16.dp),
        // Тень (elevation)
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp,
            pressedElevation = 2.dp,
            focusedElevation = 12.dp
        ),
        colors = CardDefaults.cardColors(Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = movie.posterResId),
                contentDescription = movie.title,
                modifier = Modifier
                    .size(100.dp)
                    .shadow(5.dp, shape = RoundedCornerShape(10.dp))
                    .clip(RoundedCornerShape(10.dp))


            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = movie.title,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun MatchesScreenPreview() {
    MatchesScreen()
}
package com.korikmat.watchtogether.presentation.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.korikmat.domain.models.MovieDataModel
import com.korikmat.watchtogether.R
import com.korikmat.watchtogether.presentation.utils.timeAgo

@Composable
fun SwipeableItem(
    onDeleteClicked: () -> Unit,
    selectionMode: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable() (RowScope.() -> Unit) = {},
) {
    val canSwipe = !selectionMode
    val swipeState = rememberSwipeToDismissBoxState(
        positionalThreshold = { it * 0.30f },
        confirmValueChange = { value ->
            if (value == SwipeToDismissBoxValue.EndToStart && canSwipe) {
                onDeleteClicked()
            }
            false
        }
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp,
            pressedElevation = 2.dp,
            focusedElevation = 12.dp
        ),
    ) {
        SwipeToDismissBox(
            state = swipeState,
            backgroundContent = {
                if (canSwipe) Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.errorContainer)
                        .padding(25.dp),
                    contentAlignment = Alignment.CenterEnd
                ) {

                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(R.string.delete),
                        tint = MaterialTheme.colorScheme.onErrorContainer
                    )

                }
            },
            enableDismissFromEndToStart = canSwipe,
            enableDismissFromStartToEnd = false,

            content = content
        )
    }

}

@Composable
fun MovieItem(
    posterUrl: String,
    title: String,
    releaseDate: String,
    time: String,
    selected: Boolean = false,
    onMovieClicked: () -> Unit,
    onLongClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val bg by animateColorAsState(
        if (selected) MaterialTheme.colorScheme.secondaryContainer
        else MaterialTheme.colorScheme.surface
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier)
            .combinedClickable(onClick = onMovieClicked, onLongClick = onLongClick),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp,
            pressedElevation = 2.dp,
            focusedElevation = 12.dp
        ),
        colors = CardDefaults.cardColors(containerColor = bg)
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),

            ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                AsyncImage(
                    model = posterUrl,
                    contentDescription = title,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .shadow(5.dp, shape = RoundedCornerShape(10.dp))
                        .clip(RoundedCornerShape(10.dp)),
                )
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.headlineSmall,
                    )
                    Text(
                        text = releaseDate,
                        style = MaterialTheme.typography.bodySmall,

                        )
                }
            }
            Text(
                text = time,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth(Alignment.End)
            )
        }


    }
}

@Preview(
    showBackground = true,
)
@Composable
fun MovieItemPreview() {
    MovieItem(
        posterUrl = "TODO()",
        title = "TODO()",
        releaseDate = "TODO()",
        time = "TODO()",
        onMovieClicked = {},
        modifier = Modifier
            .height(120.dp)
            .padding(start = 16.dp, top = 8.dp, end = 16.dp),

        )
}
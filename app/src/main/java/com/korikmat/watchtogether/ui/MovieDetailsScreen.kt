package com.korikmat.watchtogether.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.korikmat.watchtogether.R

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun MovieDetailsSheetPreview() {
    MovieDetailsSheet(onDismissRequest = {})
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailsSheet(
    onDismissRequest: () -> Unit,
    onBackClick: () -> Unit = {}
) {
    // Состояние шторки
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false // Можно true, если хотите только свернутое/полное
    )

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        // Можно задать полупрозрачный фон позади шторки
        scrimColor = Color.Black.copy(alpha = 0.5f),
        containerColor = Color.Transparent,
    ) {
        // Весь контент из MovieDetailsScreen, только теперь это контент внутри шторки
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(16.dp, RoundedCornerShape(30.dp))
                .clip(RoundedCornerShape(30.dp))
            // Опционально: ограничиваем высоту (иначе может занимать весь экран)
//                .heightIn(min = 300.dp, max = 800.dp)
        ) {
            // 1) Фоновое изображение (постер)
            Image(
                painter = painterResource(id = R.drawable.placeholder),
                contentDescription = "poster",
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.Crop
            )

            // 2) Тёмный градиент сверху вниз, чтобы текст был читаем
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0.5f),
                                Color.Black.copy(alpha = 0.7f),
                                Color.Black
                            )
                        )
                    )
            )

            // 3) Основной контент, скроллимый
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(
                        top = 16.dp,  // отступ сверху под кнопку назад
                        start = 16.dp,
                        end = 16.dp,
                        bottom = 16.dp
                    ),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Название фильма
                Text(
                    text = "Хантер",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    ),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                // Слоган (tagline)
                Text(
                    text = "«Будь как отец Гона!»",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = Color.White.copy(alpha = 0.9f)
                    ),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                // Жанры (список)
                Text(
                    text = "Жанры: Самый крутой жанр хули сказать",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = Color.White
                    )
                )

                // Дата выхода и продолжительность
                Row(
                    modifier = Modifier,
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Дата выхода: 18.10.2003",
                        style = MaterialTheme.typography.bodyMedium.copy(color = Color.White)
                    )

                    // Пример: 123 мин → "2 ч 3 мин"
                    val hours = 123 / 60
                    val minutes = 123 % 60
                    Text(
                        text = "Продолжительность: ${
                            if (hours > 0) "${hours}ч " else ""
                        }${minutes} мин",
                        style = MaterialTheme.typography.bodyMedium.copy(color = Color.White)
                    )
                }

                // Обзор (overview)
                Text(
                    text = "Описание:",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    ),
                    modifier = Modifier.padding(top = 8.dp)
                )

                Text(
                    text = "Охотник — это тот, кто путешествует по миру, выполняя различные опасные миссии: от поимки преступниковОхотник — это тот, кто путешествует по миру, выполняя различные опасные миссии: от поимки преступниковОхотник — это тот, кто путешествует по миру, выполняя различные опасные миссии: от поимки преступниковОхотник — это тот, кто путешествует по миру, выполняя различные опасные миссии: от поимки преступниковОхотник — это тот, кто путешествует по миру, выполняя различные опасные миссии: от поимки преступниковОхотник — это тот, кто путешествует по миру, выполняя различные опасные миссии: от поимки преступников до поиска сокровищ в неизведанных землях. Главный герой — мальчик по имени Гон. Его отец Джин был охотником, но исчез много лет назад. Гон считает, что если пойдёт по стопам отца и станет охотником, то рано или поздно вновь встретится с ним. Мальчик надеется, что, повстречав отца, наконец сможет задать ему один-единственный вопрос: почему он предпочёл жизнь охотника своему маленькому сынишке.",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color.White
                    )
                )
            }
        }
    }
}

package com.korikmat.watchtogether.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditUserProfileScreen(
    onBackClick: () -> Unit,
    onSaveClick: (String, String, Uri?) -> Unit // Передаём новые (name, nickname, imageUri)
) {
    // Текущее состояние
    var name by remember { mutableStateOf("Иван Иванов") }
    var nickname by remember { mutableStateOf("ivan123") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    // Лаунчер для выбора изображения из галереи
    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        // Если пользователь не отменил выбор
        if (uri != null) {
            imageUri = uri
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Редактировать профиль") }, navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Назад"
                    )
                }
            }, actions = {
                TextButton(
                    onClick = {
                        // По нажатию «Сохранить», передаём новые значения
                        onSaveClick(name, nickname, imageUri)
                    }) {
                    Text("Сохранить", fontWeight = FontWeight.Bold)
                }
            })
        }) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)  // Учитываем высоту AppBar
                .padding(16.dp)         // Основной отступ
                .fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Аватарка (круглая)
            Box(
                modifier = Modifier
                    .size(150.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.Gray.copy(alpha = 0.3f))
                    .clickable {
                        // При нажатии просим выбрать изображение
                        pickImageLauncher.launch("image/*")
                    }, contentAlignment = Alignment.Center
            ) {
                if (imageUri == null) {
                    // Если ещё не выбрана картинка, можно показать заглушку / иконку
                    Text("Фото", color = Color.DarkGray)
                } else {

//                    // Отображаем выбранную картинку (через Coil)
//                    AsyncImage(
//                        model = ImageRequest.Builder(LocalContext.current)
//                            .data(imageUri)
//                            .build(),
//                        contentDescription = "User photo",
//                        modifier = Modifier.fillMaxSize()
//                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Поле для имени
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Имя") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words, keyboardType = KeyboardType.Text
                ),
                shape = RoundedCornerShape(15.dp),
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Поле для никнейма
            OutlinedTextField(
                value = nickname,
                onValueChange = { nickname = it },
                label = { Text("Никнейм") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.None, keyboardType = KeyboardType.Ascii
                ),
                shape = RoundedCornerShape(15.dp),
            )
        }
    }
}

@Preview(
    showBackground = true, showSystemUi = true
)
@Composable
fun UserProfileEditScreenPreview() {
    EditUserProfileScreen(onBackClick = {}, onSaveClick = { _, _, _ -> })
}
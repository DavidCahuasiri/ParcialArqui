package com.example.fittrainer.ui.components


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ArticleItem(title: String, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            //.padding(8.dp)
            .clickable(onClick = onClick)
    ) {
        Text(text = title)
        // Aquí puedes agregar más información sobre el artículo
    }
}

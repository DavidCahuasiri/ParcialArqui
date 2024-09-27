package com.example.fittrainer.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.fittrainer.domain.NutritionArticleManager
import com.example.fittrainer.data.model.NutritionArticle

@Composable
fun NutritionArticleScreen(articleManager: NutritionArticleManager) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var articles by remember { mutableStateOf(articleManager.getArticles()) }

    // Comprobación de estado inicial de articles
    LaunchedEffect(Unit) {
        articles = articleManager.getArticles()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Título del Artículo") }
        )
        TextField(
            value = content,
            onValueChange = { content = it },
            label = { Text("Contenido del Artículo") }
        )
        Button(onClick = {
            articleManager.addArticle(title, content)
            articles = articleManager.getArticles() // Actualizar la lista de artículos
            title = "" // Limpiar el campo
            content = "" // Limpiar el campo
        }) {
            Text("Agregar Artículo")
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("Artículos de Nutrición", style = MaterialTheme.typography.titleLarge)

        articles.forEach { article ->
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(article.title, style = MaterialTheme.typography.titleLarge) // Cambiar a titleLarge
                    Text(article.content, style = MaterialTheme.typography.bodyMedium) // Cambiar a bodyMedium
                }
            }
        }
    }
}

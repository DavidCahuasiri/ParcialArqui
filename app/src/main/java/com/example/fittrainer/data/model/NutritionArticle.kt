package com.example.fittrainer.data.model


import android.net.Uri
import java.util.Date

data class NutritionArticle(
    val id: Int,                        // ID único para el artículo
    val title: String,                  // Título del artículo
    val content: String,                // Contenido del artículo
    val imageUri: Uri? = null,         // URI de la imagen (opcional)
    val createdAt: Date = Date()        // Fecha de creación
)

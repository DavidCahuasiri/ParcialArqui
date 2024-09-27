package com.example.fittrainer.domain

import com.example.fittrainer.data.model.NutritionArticle
import com.example.fittrainer.data.repository.NutritionArticleRepository

class NutritionArticleManager(private val repository: NutritionArticleRepository) {

    // Agregar un nuevo artículo de nutrición
    fun addArticle(title: String, content: String) {
        val newArticle = NutritionArticle(id = 0, title = title, content = content) // ID será auto-generado
        repository.addArticle(newArticle)
    }

    // Obtener todos los artículos de nutrición
    fun getArticles(): List<NutritionArticle> {
        return repository.getAllArticles()
    }

    // Eliminar un artículo de nutrición por ID
    fun deleteArticle(id: Int) {
        repository.deleteArticleById(id)
    }
}

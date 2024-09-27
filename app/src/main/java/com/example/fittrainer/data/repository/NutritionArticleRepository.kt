package com.example.fittrainer.data.repository


import com.example.fittrainer.data.model.NutritionArticle

class NutritionArticleRepository {
    private val articles = mutableListOf<NutritionArticle>() // Lista en memoria para almacenar artículos

    // Agregar un nuevo artículo
    fun addArticle(article: NutritionArticle) {
        articles.add(article)
    }

    // Obtener todos los artículos
    fun getAllArticles(): List<NutritionArticle> {
        return articles.toList() // Retorna una copia de la lista de artículos
    }

    // Obtener un artículo por ID
    fun getArticleById(id: Int): NutritionArticle? {
        return articles.find { it.id == id }
    }

    // Eliminar un artículo por ID
    fun deleteArticleById(id: Int) {
        articles.removeAll { it.id == id }
    }
}

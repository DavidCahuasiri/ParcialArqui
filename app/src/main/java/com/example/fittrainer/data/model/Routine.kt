package com.example.fittrainer.data.model

data class Routine(
    val id: Int,
    var name: String,
    var description: String,
    var imageUri: String?, // Ruta o URI a la imagen
    //var exercises: List<Exercise> = emptyList() // Valor por defecto: lista vacía
)

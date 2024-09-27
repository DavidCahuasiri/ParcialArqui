package com.example.fittrainer.data.model

data class Exercise(
    val id: Int,
    val name: String,
    val description: String,
    val duration: Int, // Duración en minutos
    val repetitions: Int? = null
)

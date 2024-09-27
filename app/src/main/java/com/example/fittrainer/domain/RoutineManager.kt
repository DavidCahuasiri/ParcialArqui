package com.example.fittrainer.domain

import android.content.Context
import com.example.fittrainer.data.model.Routine
import com.example.fittrainer.data.png.PNGGenerator
import com.example.fittrainer.data.repository.RoutineRepository
import java.io.File

class RoutineManager(private val repository: RoutineRepository) {

    // Crear una rutina nueva
    fun createRoutine(name: String, description: String, imageUri: String): Routine {
        val newRoutine = Routine(id = 0, name = name, description = description, imageUri = imageUri) // id será auto-generado
        repository.addRoutine(newRoutine)
        return newRoutine
    }

    // Obtener todas las rutinas
    fun getRoutines(): List<Routine> {
        return repository.getAllRoutines()
    }

    // Editar una rutina existente
    fun editRoutine(id: Int, newName: String, newDescription: String, newImageUri: String) {
        val routine = repository.getRoutineById(id)
        routine?.let {
            it.name = newName
            it.description = newDescription
            it.imageUri = newImageUri
            repository.updateRoutine(it) // Actualiza la rutina en la base de datos
        }
    }

    // Eliminar una rutina
    fun deleteRoutine(id: Int) {
        repository.deleteRoutineById(id) // Elimina la rutina de la base de datos y la lista en memoria
    }

    // Actualizar una rutina existente (sobrecarga adicional por si se pasa un objeto Routine completo)
    fun updateRoutine(updatedRoutine: Routine) {
        repository.updateRoutine(updatedRoutine) // Actualiza directamente la rutina
    }

    // Generar un PNG para la rutina
    fun generatePNG(context: Context, routineName: String, routineDescription: String): File? {
        val pngGenerator = PNGGenerator()
        return pngGenerator.generatePNG(context, routineName, routineDescription)
    }
}

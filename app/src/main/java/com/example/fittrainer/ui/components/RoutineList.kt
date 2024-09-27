package com.example.fittrainer.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.fittrainer.domain.RoutineManager
import com.example.fittrainer.data.model.Routine
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Delete

@Composable
fun RoutineList(routineManager: RoutineManager) {
    val routines = routineManager.getRoutines() // Asegúrate de que este método existe
    var showDialog by remember { mutableStateOf(false) }
    var currentRoutine by remember { mutableStateOf<Routine?>(null) }

    LazyColumn {
        items(routines) { routine: Routine ->
            RoutineItem(routine, routineManager, onEdit = {
                currentRoutine = routine
                showDialog = true
            })
        }
    }

    // Mostrar el diálogo solo si showDialog es verdadero
    if (showDialog && currentRoutine != null) {
        showEditDialog(currentRoutine!!, routineManager) {
            showDialog = false
            currentRoutine = null
        }
    }
}

@Composable
fun RoutineItem(routine: Routine, routineManager: RoutineManager, onEdit: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { /* Navegar a detalle de rutina */ }
    ) {
        Text(routine.name, modifier = Modifier.weight(1f))

        IconButton(onClick = {
            onEdit() // Invoca la función de edición
        }) {
            Icon(Icons.Filled.Edit, contentDescription = "Editar")
        }

        IconButton(onClick = {
            // Lógica para eliminar la rutina
            routineManager.deleteRoutine(routine.id)
        }) {
            Icon(Icons.Filled.Delete, contentDescription = "Eliminar")
        }
    }
}

@Composable
fun showEditDialog(routine: Routine, routineManager: RoutineManager, onDismiss: () -> Unit) {
    var newName by remember { mutableStateOf(routine.name) }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Editar Rutina") },
        text = {
            TextField(
                value = newName,
                onValueChange = { newName = it },
                label = { Text("Nombre de la rutina") }
            )
        },
        confirmButton = {
            Button(onClick = {
                routineManager.updateRoutine(routine.copy(name = newName)) // Asegúrate de que este método existe
                onDismiss() // Cierra el diálogo después de guardar
            }) {
                Text("Guardar")
            }
        },
        dismissButton = {
            Button(onClick = { onDismiss() }) {
                Text("Cancelar")
            }
        }
    )
}
package com.example.fittrainer.ui.components

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.fittrainer.domain.RoutineManager

@Composable
fun ShowCreateDialog(
    routineManager: RoutineManager,
    onDismiss: () -> Unit,
    onCreateRoutine: (String, String, String) -> Unit,
    onSelectImage: () -> Unit, // Callback para seleccionar imagen
    selectedImageUri: Uri? // URI de la imagen seleccionada
) {
    val name = remember { mutableStateOf("") }
    val description = remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Crear Rutina") },
        text = {
            Column {
                TextField(
                    value = name.value,
                    onValueChange = { name.value = it },
                    label = { Text("Nombre") }
                )
                TextField(
                    value = description.value,
                    onValueChange = { description.value = it },
                    label = { Text("Descripción") }
                )

                // Botón para seleccionar la imagen
                Button(onClick = onSelectImage) {
                    Text("Seleccionar Imagen")
                }

                // Mostrar la imagen seleccionada (si existe)
                selectedImageUri?.let {
                    Image(
                        painter = rememberImagePainter(it),
                        contentDescription = null,
                        modifier = Modifier
                            .size(100.dp)
                            .padding(top = 8.dp)
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                // Llama al callback con el nombre, la descripción y la URI de la imagen seleccionada
                onCreateRoutine(name.value, description.value, selectedImageUri?.toString() ?: "")
                onDismiss() // Cierra el diálogo
            }) {
                Text("Crear")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

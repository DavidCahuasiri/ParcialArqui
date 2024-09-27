package com.example.fittrainer.ui.components

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.fittrainer.data.model.Routine

@Composable
fun RoutineItem(
    routine: Routine,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onEdit() } // Hacer clic en el item lleva al modo de edición
            .padding(8.dp)
    ) {
        // Muestra la imagen de la rutina
        routine.imageUri?.let { imageUri ->
            Image(
                painter = rememberImagePainter(imageUri),
                contentDescription = "Imagen de la rutina",
                modifier = Modifier
                    .size(200.dp)
                    .align(Alignment.CenterHorizontally)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Muestra el nombre de la rutina
        Text(text = routine.name, style = MaterialTheme.typography.titleMedium)

        // Muestra la descripción de la rutina
        Text(text = routine.description, style = MaterialTheme.typography.bodyMedium)

        // Botones de editar y eliminar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = onEdit) {
                Text("Editar")
            }
            Button(onClick = onDelete) {
                Text("Eliminar")
            }
        }
    }
}

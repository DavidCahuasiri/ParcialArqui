package com.example.fittrainer.ui.screen

import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import androidx.compose.foundation.Image


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateRoutineScreen(
    onSaveRoutine: (String, String, String) -> Unit,
    onCancel: () -> Unit,
    selectedImageUri: Uri?,
    onImageSelected: (Uri) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Routine Name") }
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") }
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Mostrar la imagen seleccionada si existe
        selectedImageUri?.let {
            val painter = rememberAsyncImagePainter(it)
            Image(painter = painter, contentDescription = null, modifier = Modifier.size(100.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row {
            Button(onClick = { onSaveRoutine(name, description, selectedImageUri.toString()) }) {
                Text("Save")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { onCancel() }) {
                Text("Cancel")
            }
        }
    }
}

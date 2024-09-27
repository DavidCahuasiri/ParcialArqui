package com.example.fittrainer.ui.screen

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import coil.compose.rememberImagePainter
import com.example.fittrainer.data.model.Routine
import com.example.fittrainer.domain.RoutineManager
import com.example.fittrainer.ui.components.RoutineItem
import com.example.fittrainer.ui.components.showEditDialog
import com.example.fittrainer.ui.components.ShowCreateDialog
import kotlinx.coroutines.launch
import androidx.navigation.NavController



// Función para enviar la rutina por WhatsApp
fun sendRoutineByWhatsApp(context: Context, routine: Routine) {
    val sendIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, "Rutina creada: \nNombre: ${routine.name}\nDescripción: ${routine.description}")
        setPackage("com.whatsapp")
    }

    try {
        context.startActivity(sendIntent)
    } catch (e: Exception) {
        e.printStackTrace()
        Toast.makeText(context, "WhatsApp no está instalado", Toast.LENGTH_SHORT).show()
    }
}

@Composable
fun MainScreen(
    navController: NavController, // Añadir NavController como parámetro
    routineManager: RoutineManager,
    onCreateRoutine: (String, String, Uri?) -> Routine,
    onDownloadPDF: (Routine) -> Unit,
    onSelectImage: () -> Unit,
    selectedImageUri: Uri?
) {
    var routines by remember { mutableStateOf(listOf<Routine>()) }
    var selectedRoutine by remember { mutableStateOf<Routine?>(null) }
    var showCreateDialog by remember { mutableStateOf(false) }
    var currentImageUri by remember { mutableStateOf<Uri?>(null) }
    val coroutineScope = rememberCoroutineScope() // Crear un CoroutineScope

    // Función para cargar rutinas
    fun loadRoutines() {
        coroutineScope.launch {
            try {
                routines = routineManager.getRoutines() // Obtener rutinas
            } catch (e: Exception) {
                // Manejo de errores
                println("Error loading routines: ${e.message}")
            }
        }
    }

    // Cargar rutinas desde RoutineManager al iniciar la pantalla
    LaunchedEffect(Unit) {
        loadRoutines() // Cargar rutinas al inicio
    }

    // Contenedor principal
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            item {
                Text("Personal Trainer", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(16.dp))

                // Botón para navegar a NutritionArticleScreen
                Button(onClick = {
                    navController.navigate("nutrition_articles") // Navegar a NutritionArticleScreen
                }) {
                    Text("Ir a Artículos de Nutrición")
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            items(routines) { routine ->
                // Añadimos un borde alrededor del RoutineItem
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(2.dp, Color.Black, RoundedCornerShape(8.dp))
                        .background(Color(0xFFF3E5F5)) // Color lila clarito
                        .padding(16.dp)
                ) {
                    Column {
                        RoutineItem(
                            routine = routine,
                            onEdit = { selectedRoutine = routine }, // Elige la rutina para editar
                            onDelete = {
                                // Aquí implementamos la lógica de eliminación
                                coroutineScope.launch {
                                    routineManager.deleteRoutine(routine.id) // Eliminar la rutina
                                    loadRoutines() // Volver a cargar rutinas
                                }
                            }
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        // Añadir las acciones: Descargar PDF, Enviar por WhatsApp y Descargar como PNG
                        RoutineActions(routine, onDownloadPDF, LocalContext.current, routineManager)

                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        Button(
            onClick = { showCreateDialog = true },
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.TopEnd)
        ) {
            Text("+ Crear Rutina")
        }

        selectedRoutine?.let { routineToEdit ->
            showEditDialog(
                routine = routineToEdit,
                routineManager = routineManager,
                onDismiss = { selectedRoutine = null }
            )
        }

        if (showCreateDialog) {
            ShowCreateDialog(
                routineManager = routineManager,
                onDismiss = {
                    showCreateDialog = false
                    currentImageUri = null
                },
                onCreateRoutine = { name, description, imageUri ->
                    val newRoutine = onCreateRoutine(name, description, imageUri?.let { Uri.parse(it) })
                    routines = routines + newRoutine // Agregar la nueva rutina
                    showCreateDialog = false
                    currentImageUri = null
                },
                onSelectImage = {
                    onSelectImage()
                    currentImageUri = selectedImageUri // Asegúrate de que selectedImageUri se actualice
                },
                selectedImageUri = currentImageUri // Cambiado para usar currentImageUri
            )
        }

        currentImageUri?.let {
            Image(
                painter = rememberImagePainter(it),
                contentDescription = null,
                modifier = Modifier
                    .size(200.dp)
                    .align(Alignment.Center)
                    .padding(top = 16.dp)
            )
        }
    }
}

// Función para las acciones de rutina: Descargar PDF, Enviar por WhatsApp y Descargar como PNG
@Composable
fun RoutineActions(
    routine: Routine,
    onDownloadPDF: (Routine) -> Unit,
    context: Context,
    routineManager: RoutineManager
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly // Distribuir botones uniformemente
    ) {
        // Botón para descargar PDF con color verde claro
        Button(
            onClick = { onDownloadPDF(routine) },
            modifier = Modifier.width(90.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF006400)) // Verde claro
        ) {
            Text("PDF")
        }

        // Botón para enviar por WhatsApp con color verde claro
        Button(
            onClick = { sendRoutineByWhatsApp(context, routine) },
            modifier = Modifier.width(110.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF006400)) // Verde claro
        ) {
            Text("WhatsApp")
        }

        // Botón para descargar como PNG con color azul
        Button(
            onClick = {
                val file = routineManager.generatePNG(context, routine.name, routine.description)
                if (file != null) {
                    Toast.makeText(context, "PNG generado: ${file.absolutePath}", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(context, "Error al generar el PNG", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.width(90.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E90FF)) // Azul
        ) {
            Text("PNG")
        }
    }
}

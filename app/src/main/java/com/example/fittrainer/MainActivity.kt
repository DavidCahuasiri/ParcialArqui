package com.example.fittrainer

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.fittrainer.data.model.Routine
import com.example.fittrainer.data.pdf.PDFGenerator
import com.example.fittrainer.data.repository.RoutineRepository
import com.example.fittrainer.domain.RoutineManager
import com.example.fittrainer.ui.screen.MainScreen // Asegúrate de que esta línea sea correcta
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    private val STORAGE_PERMISSION_CODE = 100
    private var imageUri: Uri? = null // Almacena la URI de la imagen seleccionada
    private lateinit var routineManager: RoutineManager
    private lateinit var imagePickerLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Solicitar permisos de escritura solo si es necesario (para versiones anteriores a Android 10)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    STORAGE_PERMISSION_CODE
                )
            }
        }

        // Pasar el contexto al crear el RoutineRepository
        val repository = RoutineRepository(this) // Pasamos 'this' como contexto
        routineManager = RoutineManager(repository)

        // Configurar el lanzador para seleccionar imágenes
        imagePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                imageUri = result.data?.data // Obtener el URI de la imagen seleccionada
                Toast.makeText(this, "Imagen seleccionada: $imageUri", Toast.LENGTH_SHORT).show()
                setMainScreenContent(routineManager) // Actualizar la pantalla principal
            }
        }

        // Cargar la UI inicial
        setMainScreenContent(routineManager)
    }

    // Función para establecer la pantalla principal con la lógica de selección de imagen y creación de rutina
    private fun setMainScreenContent(routineManager: RoutineManager) {
        setContent {
            MainScreen(
                navController = rememberNavController(), // Añadir el NavController
                routineManager = routineManager,
                onCreateRoutine = { name: String, description: String, imageUri: Uri? -> // Especificar tipos
                    routineManager.createRoutine(name, description, imageUri?.toString() ?: "") // Crear rutina con imagen
                },
                onDownloadPDF = { routine: Routine -> // Especificar tipos
                    val pdfGenerator = PDFGenerator()
                    try {
                        val pdfUri = pdfGenerator.generatePDF(this@MainActivity, routine) // Obtener el URI del PDF
                        val message = if (pdfUri != null) {
                            "PDF descargado en: ${pdfUri.path}"
                        } else {
                            "Error al generar el PDF"
                        }
                        Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show() // Mostrar el URI
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Toast.makeText(this@MainActivity, "Error al generar el PDF: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                },
                onSelectImage = {
                    val intent = Intent(Intent.ACTION_PICK).apply {
                        type = "image/*"
                    }
                    imagePickerLauncher.launch(intent) // Usar el lanzador para abrir la galería
                },
                selectedImageUri = imageUri // Pasar el URI de la imagen seleccionada a la UI
            )
        }
    }
}

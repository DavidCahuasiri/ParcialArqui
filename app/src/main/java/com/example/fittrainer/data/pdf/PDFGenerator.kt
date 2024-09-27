package com.example.fittrainer.data.pdf

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.os.Handler
import android.os.Looper
import com.example.fittrainer.data.model.Routine
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Paragraph
import java.io.File
import java.io.FileOutputStream

class PDFGenerator {

    fun generatePDF(context: Context, routine: Routine): Uri {
        // Definir el nombre del archivo y la ubicación
        val pdfDir = File(Environment.getExternalStorageDirectory(), "FitTrainer")
        if (!pdfDir.exists()) {
            pdfDir.mkdirs() // Crear el directorio si no existe
        }
        val pdfFile = File(pdfDir, "${routine.name}.pdf")

        try {
            // Crear el documento PDF
            val writer = PdfWriter(FileOutputStream(pdfFile))
            val pdfDocument = com.itextpdf.kernel.pdf.PdfDocument(writer)
            val document = Document(pdfDocument)

            // Añadir contenido al documento
            document.add(Paragraph("Nombre de la Rutina: ${routine.name}"))
            document.add(Paragraph("Descripción: ${routine.description}"))

            // Añadir la lista de ejercicios (si hay)
            routine.exercises.forEach { exercise ->
                document.add(Paragraph("Ejercicio: ${exercise.name}"))
                document.add(Paragraph("Descripción: ${exercise.description}"))
                document.add(Paragraph("Repeticiones: ${exercise.repetitions}"))
            }

            document.close()

            // Retornar el URI del PDF
            val pdfUri = Uri.fromFile(pdfFile)

            // Abrir el PDF automáticamente después de un pequeño retraso
            Handler(Looper.getMainLooper()).postDelayed({
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    setDataAndType(pdfUri, "application/pdf")
                    flags = Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_GRANT_READ_URI_PERMISSION
                }

                // Verifica si hay una aplicación que pueda abrir el PDF
                if (intent.resolveActivity(context.packageManager) != null) {
                    context.startActivity(intent) // Inicia la actividad para abrir el PDF
                }
            }, 500) // 500 ms de retraso

            return pdfUri // Retorna el URI del archivo PDF
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return Uri.EMPTY // Retorna un URI vacío en caso de error
    }
}

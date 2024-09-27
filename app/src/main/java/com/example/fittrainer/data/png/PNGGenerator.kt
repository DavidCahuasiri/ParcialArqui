package com.example.fittrainer.data.png

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Environment
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class PNGGenerator {

    fun generatePNG(context: Context, routineName: String, routineDescription: String): File? {
        val bitmap = Bitmap.createBitmap(600, 800, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val paint = Paint()

        // Dibujar nombre y descripción de la rutina
        paint.textSize = 40f
        canvas.drawText("Rutina: $routineName", 50f, 100f, paint)
        paint.textSize = 30f
        canvas.drawText("Descripción: $routineDescription", 50f, 200f, paint)

        // Guardar la imagen como PNG
        val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val file = File(path, "rutina_${routineName}.png")
        try {
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            outputStream.flush()
            outputStream.close()
            return file
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
    }
}

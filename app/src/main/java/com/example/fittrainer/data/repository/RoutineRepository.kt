package com.example.fittrainer.data.repository

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.fittrainer.data.model.Routine

class RoutineRepository(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "fit_trainer.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_ROUTINES = "routines"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_DESCRIPTION = "description"
        private const val COLUMN_IMAGE_URI = "image_uri"
    }

    // Base de datos en memoria (inicializada vacía)
    private val routines = mutableListOf<Routine>()

    // Crear tabla en la base de datos
    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = """
            CREATE TABLE $TABLE_ROUTINES (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME TEXT NOT NULL,
                $COLUMN_DESCRIPTION TEXT NOT NULL,
                $COLUMN_IMAGE_URI TEXT
            )
        """.trimIndent()
        db.execSQL(createTableQuery)
    }

    // Actualizar tabla (solo si se cambia la estructura)
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_ROUTINES")
        onCreate(db)
    }

    // Añadir una rutina tanto en memoria como en la base de datos
    fun addRoutine(routine: Routine) {
        // Agregar a la base de datos
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, routine.name)
            put(COLUMN_DESCRIPTION, routine.description)
            put(COLUMN_IMAGE_URI, routine.imageUri)
        }
        val newId = db.insert(TABLE_ROUTINES, null, values) // Devuelve el ID generado

        // Actualizar el ID de la rutina y agregarla a la lista en memoria
        val routineWithId = routine.copy(id = newId.toInt())
        routines.add(routineWithId)
        db.close()
    }

    // Obtener todas las rutinas tanto de la memoria como de la base de datos
    fun getAllRoutines(): List<Routine> {
        // Si la lista en memoria está vacía, la llenamos desde la base de datos
        if (routines.isEmpty()) {
            val db = readableDatabase
            val cursor = db.query(TABLE_ROUTINES, null, null, null, null, null, null)

            while (cursor.moveToNext()) {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
                val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
                val description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION))
                val imageUri = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE_URI))
                routines.add(Routine(id, name, description, imageUri))
            }
            cursor.close()
            db.close()
        }
        return routines
    }

    // Obtener una rutina por su ID
    fun getRoutineById(id: Int): Routine? {
        return routines.find { it.id == id } ?: run {
            val db = readableDatabase
            val cursor = db.query(
                TABLE_ROUTINES,
                null,
                "$COLUMN_ID = ?",
                arrayOf(id.toString()),
                null,
                null,
                null
            )
            var routine: Routine? = null
            if (cursor.moveToFirst()) {
                val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
                val description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION))
                val imageUri = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE_URI))
                routine = Routine(id, name, description, imageUri)
                routines.add(routine)
            }
            cursor.close()
            db.close()
            routine
        }
    }

    // Actualizar una rutina en memoria y en la base de datos
    fun updateRoutine(updatedRoutine: Routine) {
        // Actualizar en la base de datos
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, updatedRoutine.name)
            put(COLUMN_DESCRIPTION, updatedRoutine.description)
            put(COLUMN_IMAGE_URI, updatedRoutine.imageUri)
        }
        db.update(TABLE_ROUTINES, values, "$COLUMN_ID = ?", arrayOf(updatedRoutine.id.toString()))

        // Actualizar en la lista en memoria
        val index = routines.indexOfFirst { it.id == updatedRoutine.id }
        if (index != -1) {
            routines[index] = updatedRoutine
        }
        db.close()
    }

    // Eliminar una rutina tanto de la memoria como de la base de datos
    fun deleteRoutineById(id: Int) {
        // Eliminar de la base de datos
        val db = writableDatabase
        db.delete(TABLE_ROUTINES, "$COLUMN_ID = ?", arrayOf(id.toString()))

        // Eliminar de la lista en memoria
        routines.removeIf { it.id == id }
        db.close()
    }
}

package com.example.notesapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.notesapp.model.Note

@Database(entities = arrayOf(Note::class), version = 1)
abstract class NoteDatabase : RoomDatabase() {

    abstract fun getNoteDao() : NoteDAO

    companion object {

        private var instance: NoteDatabase? =null

        fun getDatabaseInstance(context: Context) : NoteDatabase {

            if(instance == null) {
                instance = Room.databaseBuilder(context, NoteDatabase::class.java, "notes.db").build()
            }

            return instance!!
        }
    }
}
package com.example.notesapp.repository

import androidx.lifecycle.LiveData
import com.example.notesapp.database.NoteDAO
import com.example.notesapp.model.Note

class NotesRepository(private val noteDAO: NoteDAO) {

    val allNotes : LiveData<List<Note>> = noteDAO.getAllNotes()

    suspend fun insert(note: Note) {
        noteDAO.insert(note)
    }

    suspend fun delete(note: Note) {
        noteDAO.delete(note)
    }

    suspend fun update(note: Note) {
        noteDAO.update(note.id, note.title, note.note, note.date)
    }


}
package com.example.notesapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesapp.model.Note
import com.example.notesapp.repository.NotesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteViewModel(private val notesRepository: NotesRepository) : ViewModel() {

    val allNotes : LiveData<List<Note>> = notesRepository.allNotes

    fun insertNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            notesRepository.insert(note)
        }
    }

    fun updateNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            notesRepository.update(note)
        }
    }

    fun deletetNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            notesRepository.delete(note)
        }
    }

}
package com.example.notesapp

import androidx.cardview.widget.CardView
import com.example.notesapp.model.Note

interface NotesClickListener {

    fun onItemClicked(note: Note)

    fun onLongItemClicked(note: Note, cardView: CardView)

}
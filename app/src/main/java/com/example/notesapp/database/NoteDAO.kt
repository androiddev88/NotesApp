package com.example.notesapp.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.notesapp.model.Note

@Dao
interface NoteDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: Note)

    @Delete
    suspend fun delete(note: Note)

    @Query("SELECT * FROM notes_table ORDER BY id ASC")
    fun getAllNotes() : LiveData<List<Note>>

    @Query("UPDATE notes_table SET title = :title, note = :note, date = :date WHERE id = :id")
    suspend fun update(id: Int?, title: String?, note: String?, date: String?)

}
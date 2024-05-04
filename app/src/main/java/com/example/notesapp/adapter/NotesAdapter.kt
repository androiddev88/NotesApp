package com.example.notesapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp.NotesClickListener
import com.example.notesapp.R
import com.example.notesapp.model.Note
import kotlin.random.Random

class NotesAdapter(private val context: Context, val listener: NotesClickListener) : RecyclerView.Adapter<NotesAdapter.NotesViewHolder>() {
    private val notesList = ArrayList<Note>()
    private val fullList = ArrayList<Note>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        return NotesViewHolder(
            LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return notesList.size
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        val currentNote = notesList[position]
        holder.title.text = currentNote.title
        holder.title.isSelected = true
        holder.notes.text = currentNote.note
        holder.date.text = currentNote.date
        holder.date.isSelected = true

        holder.notes_layout.setCardBackgroundColor(holder.itemView.resources.getColor(randomColor(), null))

        holder.notes_layout.setOnClickListener {

            listener.onItemClicked((notesList[holder.adapterPosition]))
        }

        holder.notes_layout.setOnLongClickListener {
            listener.onLongItemClicked(notesList[holder.adapterPosition], holder.notes_layout)
            true
        }
    }

    fun randomColor() : Int {
        val list = ArrayList<Int>()
        list.add(R.color.NoteColor1)
        list.add(R.color.NoteColor2)
        list.add(R.color.NoteColor3)
        list.add(R.color.NoteColor4)
        list.add(R.color.NoteColor5)
        list.add(R.color.NoteColor6)

        val seed = System.currentTimeMillis().toInt()
        val randomIndex = Random(seed).nextInt(list.size)
        return list[randomIndex]
    }

    fun updateList(newList : List<Note>) {

        fullList.clear()
        fullList.addAll(newList)

        notesList.clear()
        notesList.addAll(fullList)

        notifyDataSetChanged()

    }

    fun filterList(search : String) {

        notesList.clear()

        for(item in fullList) {
            if(item.title?.lowercase()?.contains(search.lowercase()) == true  ||
                item.note?.lowercase()?.contains(search.lowercase()) == true) {

                notesList.add(item)
            }
        }

        notifyDataSetChanged()
    }

    inner class NotesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val notes_layout = itemView.findViewById<CardView>(R.id.card_layout)
        val title = itemView.findViewById<TextView>(R.id.txt_title)
        val notes = itemView.findViewById<TextView>(R.id.txt_note)
        val date = itemView.findViewById<TextView>(R.id.txt_date)
    }

}
package com.example.notesapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.notesapp.databinding.ActivityAddNoteBinding
import com.example.notesapp.model.Note
import java.text.SimpleDateFormat
import java.util.Date

class AddNote : AppCompatActivity() {

    private lateinit var binding: ActivityAddNoteBinding

    private lateinit var note: Note
    private lateinit var old_note: Note
    var isUpdated = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        try {
            old_note = intent.getSerializableExtra("current_note") as Note
            binding.edtTitle.setText(old_note.title)
            binding.edtNote.setText(old_note.note)
            isUpdated = true
        } catch (e: Exception) {
            e.printStackTrace()
        }

        binding.imgCheck.setOnClickListener {
            val title = binding.edtTitle.text.toString()
            val note_desc = binding.edtNote.text.toString()

            if(title.isNotEmpty() || note_desc.isNotEmpty()) {
                val formatter = SimpleDateFormat("EEE, d MMM yyyy HH:mm a")
                if (isUpdated && (old_note.title != title || old_note.note != note_desc)) {
                    note = Note(old_note.id, title, note_desc, "Last Modified : " + formatter.format(
                        Date()
                    ))
                }else {
                    note = Note(null, title, note_desc, formatter.format(Date()))
                }

                val intent = Intent()
                intent.putExtra("note", note)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }else {
                Toast.makeText(this@AddNote, "Please Enter Some Data", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
        }

        binding.imgBackArrow.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}
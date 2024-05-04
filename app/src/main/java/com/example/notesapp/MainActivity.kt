package com.example.notesapp

import android.app.ActionBar
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.notesapp.adapter.NotesAdapter
import com.example.notesapp.database.NoteDatabase
import com.example.notesapp.databinding.ActivityMainBinding
import com.example.notesapp.databinding.DialogDeleteNoteBinding
import com.example.notesapp.model.Note
import com.example.notesapp.repository.NotesRepository
import com.example.notesapp.viewmodel.NoteViewModel
import com.example.notesapp.viewmodel.NoteViewModelFactory

class MainActivity : AppCompatActivity(), NotesClickListener, PopupMenu.OnMenuItemClickListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: NoteViewModel
    private lateinit var database: NoteDatabase

    lateinit var adapter: NotesAdapter
    lateinit var selectedNote : Note


    private val updateNote = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode == Activity.RESULT_OK) {
            val note = it.data?.getSerializableExtra("note") as? Note
            if(note != null) {
                viewModel.updateNote(note)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUI()

        database = NoteDatabase.getDatabaseInstance(this)

        viewModel = ViewModelProvider(
            this,
            NoteViewModelFactory(NotesRepository(database.getNoteDao()))
        )[NoteViewModel::class.java]

        viewModel.allNotes.observe(this) {
            it?.let {
                adapter.updateList(it)
            }
        }
    }

    private fun initUI() {
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager =
            StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        adapter = NotesAdapter(this, this)
        binding.recyclerView.adapter = adapter

        val getContent =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == Activity.RESULT_OK) {
                    val note = it.data?.getSerializableExtra("note") as? Note
                    if (note != null) {
                        viewModel.insertNote(note)
                    }

                }
            }

        binding.floatingActionButton.setOnClickListener {
            val intent = Intent(this, AddNote::class.java)
            getContent.launch(intent)
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    adapter.filterList(newText)
                }
                return true
            }

        })
    }


    override fun onItemClicked(note: Note) {
        val intent = Intent(this@MainActivity, AddNote::class.java)
        intent.putExtra("current_note", note)
        updateNote.launch(intent)

    }

    override fun onLongItemClicked(note: Note, cardView: CardView) {
        selectedNote = note
        popUpDisplay(cardView)
    }

    private fun popUpDisplay(cardView: CardView) {
        val popup = PopupMenu(this, cardView)
        popup.setOnMenuItemClickListener(this@MainActivity)
        popup.inflate(R.menu.pop_up_menu)
        popup.show()

    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        if(item?.itemId == R.id.delete_note) {
            val dialog = Dialog(this)
            val dialogBinding = DialogDeleteNoteBinding.inflate(layoutInflater)
            dialog.setContentView(dialogBinding.root)
            dialog.window?.setLayout( ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT)
            dialog.show()

            dialogBinding.btnDelete.setOnClickListener {
                viewModel.deletetNote(selectedNote)
                dialog.dismiss()
                Toast.makeText(this, "Note has been successfully deleted", Toast.LENGTH_SHORT).show()
            }

            dialogBinding.btnCancel.setOnClickListener {
                dialog.dismiss()
            }

        }

        return false
    }
}
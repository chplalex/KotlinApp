package com.example.NotesGB.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.example.NotesGB.R
import com.example.NotesGB.data.model.Note
import com.example.NotesGB.ui.note.NoteActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: MainViewModel
    lateinit var adapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        adapter = MainAdapter(object : MainAdapter.OnItemClickListener {
            override fun onItemClick(note: Note) {
                startNoteActivity(note)
            }
        })
        mainRecycler.adapter = adapter

        viewModel.viewState().observe(this,
                {t -> t?.let { adapter.notes = it.notes }})

        fab.setOnClickListener { startNoteActivity(null)  }
    }

    private fun startNoteActivity(note: Note?) {
        startActivity(NoteActivity.getStartIntent(context = this, note = note))
    }

}
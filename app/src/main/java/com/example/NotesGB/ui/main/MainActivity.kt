package com.example.NotesGB.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.NotesGB.R
import com.example.NotesGB.ui.note.NoteActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: MainViewModel
    lateinit var adapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        adapter = MainAdapter { NoteActivity.start(context = this, note = it) }

        mainRecycler.adapter = adapter

        viewModel.viewState().observe(this,
                {t -> t?.let { adapter.notes = it.notes }})

        fab.setOnClickListener { NoteActivity.start(context = this)  }
    }

}
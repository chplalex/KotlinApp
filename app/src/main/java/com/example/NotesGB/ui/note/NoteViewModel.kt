package com.example.NotesGB.ui.note

import androidx.lifecycle.ViewModel
import com.example.NotesGB.data.Repository
import com.example.NotesGB.data.model.Note

class NoteViewModel(private val repository: Repository = Repository) : ViewModel() {

    private var pendingNote: Note? = null

    fun saveChanges(note: Note) { pendingNote = note }

    override fun onCleared() { pendingNote?.let { repository.saveNote(it) } }
}
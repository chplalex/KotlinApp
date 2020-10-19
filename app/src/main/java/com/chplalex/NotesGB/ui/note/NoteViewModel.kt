package com.chplalex.NotesGB.ui.note

import androidx.lifecycle.Observer
import com.chplalex.NotesGB.data.Repository
import com.chplalex.NotesGB.data.model.Note
import com.chplalex.NotesGB.data.model.NoteResult
import com.chplalex.NotesGB.data.model.NoteResult.Success
import com.chplalex.NotesGB.data.model.NoteResult.Error
import com.chplalex.NotesGB.ui.base.BaseViewModel

class NoteViewModel(val repository: Repository = Repository) : BaseViewModel<Note?, NoteViewState>() {

    private var pendingNote: Note? = null

    fun saveChanges(note: Note) { pendingNote = note }

    override fun onCleared() { pendingNote?.let { repository.saveNote(it) } }

    fun loadNote(id: String) {
        repository.getNoteById(id).observeForever(Observer<NoteResult> { t ->
            t ?: return@Observer

            when (t) {
                is Success<*> -> viewStateLiveData.value = NoteViewState(note = t.data as? Note)
                is Error -> viewStateLiveData.value = NoteViewState(error = t.error)
            }
        })
    }
}
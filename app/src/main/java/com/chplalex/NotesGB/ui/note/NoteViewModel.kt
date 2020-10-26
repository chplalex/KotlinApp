package com.chplalex.NotesGB.ui.note

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.chplalex.NotesGB.data.Repository
import com.chplalex.NotesGB.data.model.Note
import com.chplalex.NotesGB.data.model.NoteResult
import com.chplalex.NotesGB.data.model.NoteResult.Success
import com.chplalex.NotesGB.data.model.NoteResult.Error
import com.chplalex.NotesGB.ui.base.BaseViewModel

class NoteViewModel(val repository: Repository = Repository) : BaseViewModel<Note?, NoteViewState>() {

    private var pendingNote: Note? = null
    private var noteLiveData: LiveData<NoteResult>? = null
    private val noteObserver = object : Observer<NoteResult> {
        override fun onChanged(t: NoteResult?) {
            t ?: return
            when (t) {
                is Success<*> -> viewStateLiveData.value = NoteViewState(note = t.data as? Note)
                is Error -> viewStateLiveData.value = NoteViewState(error = t.error)
            }
        }

    }

    fun saveChanges(note: Note) { pendingNote = note }

    override fun onCleared() {
        pendingNote?.let { repository.saveNote(it) }
        noteLiveData?.removeObserver(noteObserver)
    }

    fun loadNote(id: String) {
        noteLiveData = repository.getNoteById(id)
        noteLiveData?.observeForever(noteObserver)
    }
}
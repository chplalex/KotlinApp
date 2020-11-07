package com.chplalex.notesgb.ui.note

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.chplalex.notesgb.data.Repository
import com.chplalex.notesgb.data.model.Note
import com.chplalex.notesgb.data.model.NoteResult
import com.chplalex.notesgb.data.model.NoteResult.Success
import com.chplalex.notesgb.data.model.NoteResult.Error
import com.chplalex.notesgb.ui.base.BaseViewModel
import com.chplalex.ui.note.NoteData
import kotlinx.coroutines.launch

class NoteViewModel(val repository: Repository) : BaseViewModel<NoteData>() {

    private val currentNote: Note?
        get() = getViewState().poll()?.note

    fun saveChanges(note: Note) {
        setData(NoteData(note = note))
    }

    fun loadNote(id: String) = launch {
        try {
           repository.getNoteById(id).let { setData(NoteData(note = it)) }
        } catch (error: Throwable) {
            setError(error)
        }
    }

    fun deleteNote() = launch {
        try {
            currentNote?.let { repository.deleteNote(it.id) }
            setData(NoteData(isDeleted = true))
        } catch (error: Throwable) {
            setError(error)
        }
    }

    @VisibleForTesting
    public override fun onCleared() {
        launch {
            currentNote?.let { repository.saveNote(it) }
            super.onCleared()
        }
    }

}
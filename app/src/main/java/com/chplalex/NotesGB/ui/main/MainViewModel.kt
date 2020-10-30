package com.chplalex.notesgb.ui.main

import androidx.lifecycle.Observer
import com.chplalex.notesgb.data.Repository
import com.chplalex.notesgb.data.model.Note
import com.chplalex.notesgb.data.model.NoteResult
import com.chplalex.notesgb.data.model.NoteResult.Success
import com.chplalex.notesgb.data.model.NoteResult.Error
import com.chplalex.notesgb.ui.base.BaseViewModel

class MainViewModel(val repository: Repository) : BaseViewModel<List<Note>?, MainViewState>() {

    private val notesObserver = Observer<NoteResult> { t ->
        t ?: return@Observer

        @Suppress("UNCHECKED_CAST")
        when (t) {
            is Success<*> -> viewStateLiveData.value = MainViewState(notes = t.data as? List<Note>)
            is Error -> viewStateLiveData.value = MainViewState(error = t.error)
        }
    }

    private val repositoryNotes = repository.getNotes()

    init {
        repositoryNotes.observeForever(notesObserver)
    }

    override fun onCleared() {
        repositoryNotes.removeObserver(notesObserver)
        super.onCleared()
    }
}
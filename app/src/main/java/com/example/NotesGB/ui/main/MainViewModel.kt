package com.example.NotesGB.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.NotesGB.data.Repository
import com.example.NotesGB.data.model.Note
import com.example.NotesGB.data.model.NoteResult
import com.example.NotesGB.data.model.NoteResult.Success
import com.example.NotesGB.data.model.NoteResult.Error
import com.example.NotesGB.ui.base.BaseViewModel

class MainViewModel(val repository: Repository = Repository) : BaseViewModel<List<Note>?, MainViewState>() {

    private val repositoryNotes = repository.getNotes()

    private val viewStateLiveData: MutableLiveData<MainViewState> = MutableLiveData()

    private val notesObserver = Observer<NoteResult> { t ->
        if (t == null) return@Observer

        when (t) {
            is Success<*> -> viewStateLiveData.value = MainViewState(notes = t.data as? List<Note>)
            is Error -> viewStateLiveData.value = MainViewState(error = t.error)
        }
    }

    init {
        viewStateLiveData.value = MainViewState()
        repositoryNotes.observeForever { notesObserver }
    }

    override fun onCleared() {
        repositoryNotes.removeObserver(notesObserver)
        super.onCleared()
    }
}
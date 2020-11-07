package com.chplalex.notesgb.ui.main

import com.chplalex.notesgb.data.Repository
import com.chplalex.notesgb.data.model.Note
import com.chplalex.notesgb.data.model.NoteResult
import com.chplalex.notesgb.data.model.NoteResult.Success
import com.chplalex.notesgb.data.model.NoteResult.Error
import com.chplalex.notesgb.ui.base.BaseViewModel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch

class MainViewModel(val repository: Repository) : BaseViewModel<List<Note>?>() {

    private val notesChannel = repository.getNotes()

    init {
        launch {
            notesChannel.consumeEach {
                when (it) {
                    is Success<*> -> setData(it.data as List<Note>)
                    is Error -> setError(it.error)
                }
            }
        }
    }

}
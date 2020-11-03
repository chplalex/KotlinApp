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

class NoteViewModel(val repository: Repository) : BaseViewModel<NoteViewState.Data, NoteViewState>() {

    init {
        viewStateLiveData.postValue(NoteViewState())
    }

    private var pendingNote: Note? = null
    private var loadLiveData: LiveData<NoteResult>? = null
    private var deleteLiveData: LiveData<NoteResult>? = null

    private val noteObserver = object : Observer<NoteResult> {

        override fun onChanged(t: NoteResult?) {
            t ?: return
            when (t) {
                is Success<*> -> {
                    pendingNote = t.data as? Note
                    viewStateLiveData.value = NoteViewState(NoteViewState.Data(note = pendingNote))
                }
                is Error -> viewStateLiveData.value = NoteViewState(error = t.error)
            }
            loadLiveData?.removeObserver(this)
        }
    }

    private val deleteObserver = object : Observer<NoteResult> {
        override fun onChanged(result: NoteResult?) {
            when (result) {
                is Success<*> -> { viewStateLiveData.value = NoteViewState(NoteViewState.Data(isDeleted = true)) }
                is Error -> viewStateLiveData.value = NoteViewState(error = result.error)
            }
            deleteLiveData?.removeObserver(this)
        }
    }

    fun saveChanges(note: Note) {
        pendingNote = note
    }

    @VisibleForTesting
    public override fun onCleared() {
        pendingNote?.let { repository.saveNote(it) }
        loadLiveData?.removeObserver(noteObserver)
        deleteLiveData?.removeObserver(deleteObserver)
        super.onCleared()
    }

    fun loadNote(id: String) {
        loadLiveData = repository.getNoteById(id)
        loadLiveData?.observeForever(noteObserver)
    }

    fun deleteNote() {
        pendingNote?.let {
            deleteLiveData = repository.deleteNote(it.id)
            deleteLiveData?.observeForever(deleteObserver)
        }
        pendingNote = null
    }
}
package com.chplalex.notesgb.ui.note

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.chplalex.notesgb.data.Repository
import com.chplalex.notesgb.data.model.Note
import com.chplalex.notesgb.data.model.NoteResult
import com.chplalex.notesgb.data.model.NoteResult.Success
import com.chplalex.notesgb.data.model.NoteResult.Error
import com.chplalex.notesgb.ui.base.BaseViewModel

class NoteViewModel(val repository: Repository) : BaseViewModel<NoteViewState.Data, NoteViewState>() {

    private var pendingNote: Note? = null
    private var loadLiveData: LiveData<NoteResult>? = null
    private var deleteLiveData: LiveData<NoteResult>? = null
    private val noteObserver = object : Observer<NoteResult> {
        override fun onChanged(t: NoteResult?) {
            t ?: return
            when (t) {
                is Success<*> -> viewStateLiveData.value = NoteViewState(NoteViewState.Data(note = t.data as? Note))
                is Error -> viewStateLiveData.value = NoteViewState(error = t.error)
            }
        }
    }

    fun saveChanges(note: Note) { pendingNote = note }

    override fun onCleared() {
        pendingNote?.let { repository.saveNote(it) }
        loadLiveData?.removeObserver(noteObserver)
        deleteLiveData?.removeObserver(noteObserver)
        super.onCleared()
    }

    fun loadNote(id: String) {
        loadLiveData = repository.getNoteById(id)
        loadLiveData?.observeForever(noteObserver)
    }

    fun deleteNote() {
        Log.d("TTT", "deleteNote()")
        pendingNote?.let {
            deleteLiveData = repository.deleteNote(it.id)
            deleteLiveData?.observeForever(noteObserver)
        }
    }
}
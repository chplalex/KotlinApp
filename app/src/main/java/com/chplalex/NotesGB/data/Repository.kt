package com.chplalex.NotesGB.data

import android.util.Log
import androidx.lifecycle.LiveData
import com.chplalex.NotesGB.data.model.Note
import com.chplalex.NotesGB.data.model.NoteResult
import com.chplalex.NotesGB.data.provider.FirestoreProvider
import com.chplalex.NotesGB.data.provider.RemoteDataProvider
import com.chplalex.NotesGB.extensions.TAG

object Repository {

    private val remoteDataProvider: RemoteDataProvider = FirestoreProvider()

    fun getNotes(): LiveData<NoteResult> {
        Log.d(TAG, "Repository.getNotes()")
        return remoteDataProvider.getNotes()
    }
    fun saveNote(note: Note) = remoteDataProvider.saveNote(note)
    fun getNoteById(id: String) = remoteDataProvider.getNoteById(id)

}
package com.example.NotesGB.data

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.NotesGB.data.model.Note
import com.example.NotesGB.data.model.NoteResult
import com.example.NotesGB.data.provider.FirestoreProvider
import com.example.NotesGB.data.provider.RemoteDataProvider
import com.example.NotesGB.extensions.TAG

object Repository {

    private val remoteDataProvider: RemoteDataProvider = FirestoreProvider()

    fun getNotes(): LiveData<NoteResult> {
        Log.d(TAG, "Repository.getNotes()")
        return remoteDataProvider.subscribeToAllNotes()
    }
    fun saveNote(note: Note) = remoteDataProvider.saveNote(note)
    fun getNoteById(id: String) = remoteDataProvider.getNoteById(id)

}
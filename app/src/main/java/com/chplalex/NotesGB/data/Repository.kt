package com.chplalex.NotesGB.data

import com.chplalex.NotesGB.data.model.Note
import com.chplalex.NotesGB.data.provider.FirestoreProvider
import com.chplalex.NotesGB.data.provider.RemoteDataProvider

object Repository {

    private val remoteDataProvider: RemoteDataProvider = FirestoreProvider()

    fun getNotes() = remoteDataProvider.getNotes()
    fun deleteNote(id: String) = remoteDataProvider.deleteNote(id)
    fun saveNote(note: Note) = remoteDataProvider.saveNote(note)
    fun getNoteById(id: String) = remoteDataProvider.getNoteById(id)
    fun getCurrentUser()= remoteDataProvider.getCurrentUser()

}
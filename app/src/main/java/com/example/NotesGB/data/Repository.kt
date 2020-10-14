package com.example.NotesGB.data

import com.example.NotesGB.data.model.Note
import com.example.NotesGB.data.provider.FirestoreProvider
import com.example.NotesGB.data.provider.RemoteDataProvider

object Repository {

    private val remoteDataProvider: RemoteDataProvider = FirestoreProvider()

    fun getNotes() = remoteDataProvider.subscribeToAllNotes()
    fun saveNote(note: Note) = remoteDataProvider.saveNote(note)
    fun getNoteById(id: String) = remoteDataProvider.getNoteById(id)

}
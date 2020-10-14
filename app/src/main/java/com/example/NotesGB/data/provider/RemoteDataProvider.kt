package com.example.NotesGB.data.provider

import androidx.lifecycle.LiveData
import com.example.NotesGB.data.model.Note
import com.example.NotesGB.data.model.NoteResult

interface RemoteDataProvider {
    fun subscribeToAllNotes(): LiveData<NoteResult>
    fun getNoteById(id: String): LiveData<NoteResult>
    fun saveNote(note: Note): LiveData<NoteResult>
}
package com.chplalex.NotesGB.data.provider

import androidx.lifecycle.LiveData
import com.chplalex.NotesGB.data.model.Note
import com.chplalex.NotesGB.data.model.NoteResult
import com.chplalex.NotesGB.data.model.User

interface RemoteDataProvider {
    fun getNotes(): LiveData<NoteResult>
    fun getNoteById(id: String): LiveData<NoteResult>
    fun saveNote(note: Note): LiveData<NoteResult>
    fun getCurrentUser(): LiveData<User?>
}
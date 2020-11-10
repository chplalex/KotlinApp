package com.chplalex.notesgb.data.provider

import com.chplalex.notesgb.data.model.Note
import com.chplalex.notesgb.data.model.NoteResult
import com.chplalex.notesgb.data.model.User
import kotlinx.coroutines.channels.ReceiveChannel

interface DataProvider {
    fun getNotes(): ReceiveChannel<NoteResult>
    suspend fun getNoteById(id: String): Note
    suspend fun deleteNote(id: String): Unit
    suspend fun saveNote(note: Note): Note
    suspend fun getCurrentUser(): User?
}
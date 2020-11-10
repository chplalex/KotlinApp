package com.chplalex.notesgb.data

import com.chplalex.notesgb.data.model.Note
import com.chplalex.notesgb.data.provider.DataProvider

class Repository(val dataProvider: DataProvider) {
    fun getNotes() = dataProvider.getNotes()
    suspend fun deleteNote(id: String) = dataProvider.deleteNote(id)
    suspend fun saveNote(note: Note) = dataProvider.saveNote(note)
    suspend fun getNoteById(id: String) = dataProvider.getNoteById(id)
    suspend fun getCurrentUser()= dataProvider.getCurrentUser()

}
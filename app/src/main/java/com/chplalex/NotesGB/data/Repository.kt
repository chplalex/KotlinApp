package com.chplalex.notesgb.data

import com.chplalex.notesgb.data.model.Note
import com.chplalex.notesgb.data.provider.DataProvider

class Repository(val dataProvider: DataProvider) {
    fun getNotes() = dataProvider.getNotes()
    fun deleteNote(id: String) = dataProvider.deleteNote(id)
    fun saveNote(note: Note) = dataProvider.saveNote(note)
    fun getNoteById(id: String) = dataProvider.getNoteById(id)
    fun getCurrentUser()= dataProvider.getCurrentUser()

}
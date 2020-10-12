package com.example.NotesGB.ui.main

import com.example.NotesGB.data.model.Note

class MainViewState(val notes: List<Note>) {

    fun copy(notes: List<Note>) = MainViewState(notes)

}
package com.example.NotesGB.ui.main

import com.example.NotesGB.data.model.Note
import com.example.NotesGB.ui.base.BaseViewState

class MainViewState(notes: List<Note>? = null, error: Throwable? = null) : BaseViewState<List<Note>?>(notes, error)
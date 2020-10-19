package com.chplalex.NotesGB.ui.main

import com.chplalex.NotesGB.data.model.Note
import com.chplalex.NotesGB.ui.base.BaseViewState

class MainViewState(notes: List<Note>? = null, error: Throwable? = null) : BaseViewState<List<Note>?>(notes, error)
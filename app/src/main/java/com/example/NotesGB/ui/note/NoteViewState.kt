package com.example.NotesGB.ui.note

import com.example.NotesGB.data.model.Note
import com.example.NotesGB.ui.base.BaseViewState

class NoteViewState(note: Note? = null, error: Throwable? = null) : BaseViewState<Note?>(note, error)
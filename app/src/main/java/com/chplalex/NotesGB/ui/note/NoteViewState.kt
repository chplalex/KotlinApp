package com.chplalex.NotesGB.ui.note

import com.chplalex.NotesGB.data.model.Note
import com.chplalex.NotesGB.ui.base.BaseViewState

class NoteViewState(note: Note? = null, error: Throwable? = null) : BaseViewState<Note?>(note, error)
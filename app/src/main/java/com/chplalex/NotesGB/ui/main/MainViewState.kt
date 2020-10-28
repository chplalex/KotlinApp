package com.chplalex.notesgb.ui.main

import com.chplalex.notesgb.data.model.Note
import com.chplalex.notesgb.ui.base.BaseViewState

class MainViewState(notes: List<Note>? = null, error: Throwable? = null) : BaseViewState<List<Note>?>(notes, error)
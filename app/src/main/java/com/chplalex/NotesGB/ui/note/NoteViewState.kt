package com.chplalex.notesgb.ui.note

import com.chplalex.notesgb.data.model.Note
import com.chplalex.notesgb.ui.base.BaseViewState

class NoteViewState(data: Data = Data(), error: Throwable? = null) : BaseViewState<NoteViewState.Data>(data, error) {

    data class Data(val isDeleted: Boolean = false, val note: Note? = null)

}
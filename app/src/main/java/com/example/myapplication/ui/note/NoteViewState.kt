package com.example.myapplication.ui.note

import com.example.myapplication.data.entity.Note
import com.example.myapplication.ui.base.BaseViewState

class NoteViewState(data: Data = Data(), error: Throwable? = null) : BaseViewState<NoteViewState.Data>(data, error) {
    data class Data(val isDeleted: Boolean = false, val note: Note? = null)
}
package com.example.myapplication.ui.note

import com.example.myapplication.data.entity.Note
import com.example.myapplication.ui.base.BaseViewState

class NoteViewState(note: Note? = null, error: Throwable? = null): BaseViewState<Note?>(note, error)
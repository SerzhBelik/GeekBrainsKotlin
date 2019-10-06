package com.example.myapplication.ui.main

import com.example.myapplication.data.entity.Note
import com.example.myapplication.ui.base.BaseViewState

class MainViewState(val notes: List<Note>? = null, error: Throwable? = null) : BaseViewState<List<Note>?>(notes, error)
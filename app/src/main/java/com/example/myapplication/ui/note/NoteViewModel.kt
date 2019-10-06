package com.example.myapplication.ui.note

import androidx.lifecycle.Observer
import com.example.myapplication.data.NotesRepository
import com.example.myapplication.data.entity.Note
import com.example.myapplication.data.model.NoteResult
import com.example.myapplication.ui.base.BaseViewModel

class NoteViewModel(private val notesRepository: NotesRepository) : BaseViewModel<Note?, NoteViewState>() {

    init {
        viewStateLiveData.value = NoteViewState()
    }

    private var pendingNote: Note? = null

    fun save(note: Note) {
        pendingNote = note
    }

    override fun onCleared() {
        if (pendingNote != null) {
            notesRepository.saveNote(pendingNote!!)
        }
    }

    fun loadNote(noteId: String) {
        notesRepository.getNoteById(noteId).observeForever(Observer<NoteResult> {
            if (it == null) return@Observer

            when (it) {
                is NoteResult.Success<*> -> viewStateLiveData.value = NoteViewState(note = it.data as? Note)
                is NoteResult.Error -> viewStateLiveData.value = NoteViewState(error = it.error)
            }
        })
    }
}
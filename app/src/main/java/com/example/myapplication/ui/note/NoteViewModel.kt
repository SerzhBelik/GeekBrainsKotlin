package com.example.myapplication.ui.note

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.Observer
import com.example.myapplication.data.NotesRepository
import com.example.myapplication.data.entity.Note
import com.example.myapplication.data.model.NoteResult
import com.example.myapplication.ui.base.BaseViewModel

open class NoteViewModel(private val notesRepository: NotesRepository) : BaseViewModel<NoteViewState.Data, NoteViewState>() {

    init {
        viewStateLiveData.value = NoteViewState()
    }

    private val pendingNote: Note?
        get() = viewStateLiveData.value?.data?.note

    fun save(note: Note) {
        viewStateLiveData.value = NoteViewState(NoteViewState.Data(note = note))
    }

    @VisibleForTesting
    public override fun onCleared() {
        pendingNote?.let {
            notesRepository.saveNote(it)
        }
    }

    fun loadNote(noteId: String) {
        notesRepository.getNoteById(noteId).observeForever(Observer<NoteResult> {
            if (it == null) return@Observer

            viewStateLiveData.value =  when (it) {
                is NoteResult.Success<*> ->  NoteViewState(NoteViewState.Data(note = it.data as? Note))
                is NoteResult.Error ->  NoteViewState(error = it.error)
            }
        })
    }

    fun deleteNote() {
        pendingNote?.let {
            notesRepository.deleteNote(it.id).observeForever { result ->
                result?.let {
                    viewStateLiveData.value = when(result){
                        is NoteResult.Success<*> -> {
                            NoteViewState(NoteViewState.Data(isDeleted = true))
                        }
                        is NoteResult.Error -> NoteViewState(error = result.error)
                    }
                }
            }
        }
    }
}
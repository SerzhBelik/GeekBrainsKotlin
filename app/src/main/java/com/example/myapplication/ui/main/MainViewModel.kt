package com.example.myapplication.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.myapplication.data.NotesRepository
import com.example.myapplication.data.entity.Note
import com.example.myapplication.data.model.NoteResult
import com.example.myapplication.ui.base.BaseViewModel

class MainViewModel(private val notesRepository: NotesRepository) : BaseViewModel<List<Note>?, MainViewState>() {

    private val notesObserver = Observer<NoteResult> {
        if (it == null) return@Observer

        when (it) {
            is NoteResult.Success<*> -> {
                viewStateLiveData.value = MainViewState(notes = it.data as? List<Note>)
            }
            is NoteResult.Error -> {
                viewStateLiveData.value = MainViewState(error = it.error)
            }
        }
    }

    private val repositoryNotes = notesRepository.getNotes()

    init {
        viewStateLiveData.value = MainViewState()
        repositoryNotes.observeForever(notesObserver)
    }


    override fun onCleared() {
        repositoryNotes.removeObserver(notesObserver)
    }

    fun viewState(): LiveData<MainViewState> = viewStateLiveData
}
package com.example.myapplication.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.myapplication.data.NotesRepository
import com.example.myapplication.data.entity.Note
import com.example.myapplication.data.model.NoteResult
import com.example.myapplication.ui.base.BaseViewModel

class MainViewModel : BaseViewModel<List<Note>?, MainViewState>() {

    private val noteObserver = Observer<NoteResult>{
    if (it == null) return@Observer

        when(it){
            is NoteResult.Success<*> ->
                viewStateLiveData.value = MainViewState(notes = it.data as? List<Note>)
            is NoteResult.Error ->
                viewStateLiveData.value = MainViewState(error = it.error)
        }


}

    private val repositoryNotes = NotesRepository.getNotes()

    init {
       viewStateLiveData.value = MainViewState()
        repositoryNotes.observeForever(noteObserver)
    }

    override fun onCleared() {
        repositoryNotes.removeObserver(noteObserver)
    }
    fun viewState(): LiveData<MainViewState> = viewStateLiveData
}
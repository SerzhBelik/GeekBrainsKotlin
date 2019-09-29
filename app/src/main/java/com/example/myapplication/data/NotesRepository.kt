package com.example.myapplication.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.data.entity.Note
import com.example.myapplication.data.model.NoteResult
import com.example.myapplication.data.provider.FireStoreProvider
import com.example.myapplication.data.provider.RemoteDataProvider
import java.util.*

object NotesRepository {
    private val remoteProvider: RemoteDataProvider = FireStoreProvider()
    fun getNotes(): LiveData<NoteResult> = remoteProvider.subscribeToAllNotes()
    fun saveNote(note: Note): LiveData<NoteResult> = remoteProvider.saveNote(note)
    fun getNoteById(id: String): LiveData<NoteResult> = remoteProvider.getNoteById(id)
    fun getCurrentUser() = remoteProvider.getCurrentUser()

}
package com.example.myapplication.data

import com.example.myapplication.data.entity.Note
import com.example.myapplication.data.provider.RemoteDataProvider


class NotesRepository(private val remoteProvider: RemoteDataProvider) {
    fun getNotes() = remoteProvider.subscribeToAllNotes()
    fun saveNote(note: Note) = remoteProvider.saveNote(note)
    fun getNoteById(id: String) = remoteProvider.getNoteById(id)
    fun getCurrentUser() = remoteProvider.getCurrentUser()
}
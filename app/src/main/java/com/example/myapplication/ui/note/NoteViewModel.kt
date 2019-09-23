package com.example.myapplication.ui.note

import android.provider.ContactsContract
import androidx.lifecycle.ViewModel
import com.example.myapplication.data.NotesRepository
import com.example.myapplication.data.entity.Note

class NoteViewModel : ViewModel() {
    private var pendingNote: Note? = null

    fun save(note : Note){
        pendingNote = note
    }

    override fun onCleared() {
        if (pendingNote != null){
            NotesRepository.saveNote(pendingNote!!)
        }
    }
}
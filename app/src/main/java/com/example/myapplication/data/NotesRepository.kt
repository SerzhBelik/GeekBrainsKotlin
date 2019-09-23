package com.example.myapplication.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.data.entity.Note
import java.util.*

object NotesRepository {

    private val noteLiveData = MutableLiveData<List<Note>>()

    var notes = mutableListOf(
        Note(
                UUID.randomUUID().toString(),
            "Первая заметка",
            "Текс первой заметки. Не очень длинный, но очень интересный",
            color = Note.Color.BLUE
        ),
        Note(
                UUID.randomUUID().toString(),
            "Вторая заметка",
            "Текс второй заметки. Не очень длинный, но очень интересный",
                color = Note.Color.GREEN
        ),
        Note(
                UUID.randomUUID().toString(),
            "Третья заметка",
            "Текст третьей заметки. Не очень длинный, но очень интересный",
                color = Note.Color.PINK
        ),
        Note(
                UUID.randomUUID().toString(),
            "Четвертая заметка",
            "Текс четвертой заметки. Не очень длинный, но очень интересный",
                color = Note.Color.RED
        ),
        Note(
                UUID.randomUUID().toString(),
            "Пятая заметка",
            "Текс пятой заметки. Не очень длинный, но очень интересный",
                color = Note.Color.VIOLET
        ),
        Note(
                UUID.randomUUID().toString(),
            "Шестая заметка",
            "Текс шестой заметки. Не очень длинный, но очень интересный",
                color = Note.Color.WHITE
        )
    )

    private set

    init {
        noteLiveData.value = notes
    }


    fun getNotes(): LiveData<List<Note>> {
        return noteLiveData
    }

    fun saveNote(note : Note){
        addOrReplace(note)
        noteLiveData.value = notes

    }

    private fun addOrReplace(note : Note){
        for (i in 0 until notes.size){
            if (notes[i] == note){
                notes[i] = note
                return
            }

        }

        notes.add(note)
    }
}
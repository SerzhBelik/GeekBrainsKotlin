package com.example.myapplication.data

import com.example.myapplication.data.entity.Note

object NotesRepository {



    val notes: List<Note> = listOf(
        Note(
            "Первая заметка",
            "Текс первой заметки. Не очень длинный, но очень интересный",
            0xfff06292.toInt()
        ),
        Note(
            "Вторая заметка",
            "Текс второй заметки. Не очень длинный, но очень интересный",
            0xff9575cd.toInt()
        ),
        Note(
            "Третья заметка",
            "Текст третьей заметки. Не очень длинный, но очень интересный",
            0xff64b5f6.toInt()
        ),
        Note(
            "Четвертая заметка",
            "Текс четвертой заметки. Не очень длинный, но очень интересный",
            0xff4db6ac.toInt()
        ),
        Note(
            "Пятая заметка",
            "Текс пятой заметки. Не очень длинный, но очень интересный",
            0xffb2ff59.toInt()
        ),
        Note(
            "Шестая заметка",
            "Текс шестой заметки. Не очень длинный, но очень интересный",
            0xffffeb3b.toInt()
        )
    )


//    fun getNotes(): List<Note> {
//        return notes
//    }
}
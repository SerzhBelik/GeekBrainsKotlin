package com.example.myapplication.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.data.NotesRepository
import com.example.myapplication.data.entity.Note
import com.example.myapplication.data.model.NoteResult
import com.example.myapplication.ui.note.NoteViewModel
import com.example.myapplication.ui.note.NoteViewState
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.reset
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class NoteViewModelTest {

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private val mockRepository = mock<NotesRepository>()
    private val notesLiveData = MutableLiveData<NoteResult>()

    private val testNote = Note("1", "title1", "text1")

    private lateinit var viewModel: NoteViewModel

    @Before
    fun setup() {
        reset(mockRepository)
        whenever(mockRepository.getNoteById(testNote.id)).thenReturn(notesLiveData)
        whenever(mockRepository.deleteNote(testNote.id)).thenReturn(notesLiveData)
        viewModel = NoteViewModel(mockRepository)
    }

    @Test
    fun `loadNote should return NoteData`() {
        var result: NoteViewState.Data? = null
        val testData = NoteViewState.Data(false, testNote)
        viewModel.getViewState().observeForever {
            result = it?.data
        }
        viewModel.loadNote(testNote.id)
        notesLiveData.value = NoteResult.Success(testNote)
        assertEquals(testData, result)
    }

    @Test
    fun `loadNote should return error`() {
        var result: Throwable? = null
        val testData = Throwable("error")
        viewModel.getViewState().observeForever {
            result = it?.error
        }
        viewModel.loadNote(testNote.id)
        notesLiveData.value = NoteResult.Error(error = testData)
        assertEquals(testData, result)
    }

    @Test
    fun `should save changes`() {
        viewModel.save(testNote)
        viewModel.onCleared()
        verify(mockRepository, times(1)).saveNote(testNote)
    }

    @Test
    fun `deleteNote should return NoteData with isDeleted`() {
        var result: NoteViewState.Data? = null
        val testData = NoteViewState.Data(true, null)
        viewModel.getViewState().observeForever {
            result = it?.data
        }
        viewModel.save(testNote)
        viewModel.deleteNote()
        notesLiveData.value = NoteResult.Success(null)
        assertEquals(testData, result)
    }

    @Test
    fun `deleteNote should return error`() {
        var result: Throwable? = null
        val testData = Throwable("error")
        viewModel.getViewState().observeForever {
            result = it?.error
        }
        viewModel.save(testNote)
        viewModel.deleteNote()
        notesLiveData.value = NoteResult.Error(error = testData)
        assertEquals(testData, result)
    }
}
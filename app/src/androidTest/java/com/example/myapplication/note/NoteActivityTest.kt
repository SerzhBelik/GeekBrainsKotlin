package com.example.myapplication.note

import android.content.Intent
import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.rule.ActivityTestRule
import com.example.myapplication.R
import com.example.myapplication.data.entity.Note
import com.example.myapplication.ui.main.MainActivity
import com.example.myapplication.ui.note.NoteActivity
import com.example.myapplication.ui.note.NoteViewModel
import com.example.myapplication.ui.note.NoteViewState
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import org.koin.standalone.StandAloneContext.loadKoinModules
import org.koin.standalone.StandAloneContext.stopKoin
import org.mockito.Mockito
import org.mockito.Mockito.doNothing
import org.mockito.Mockito.doReturn

class NoteActivityTest {

    @get:Rule
    val activityTestRule = ActivityTestRule(MainActivity::class.java, true, false)

    private val model: NoteViewModel = Mockito.mock(NoteViewModel::class.java)
    private val viewStateLiveData = MutableLiveData<NoteViewState>()

    private val testNote = Note("aaa", "title1", "body1")

    @Before
    fun setup() {
        loadKoinModules(
                listOf(
                        module {
                            viewModel(override = true) { model }
                        }
                )
        )

        doReturn(viewStateLiveData).`when`(model).getViewState()
        doNothing().`when`(model).loadNote(testNote.id)
        doNothing().`when`(model).save(Mockito.any())
        doNothing().`when`(model).deleteNote()

        Intent().apply {
            putExtra(NoteActivity::class.java.name + "extra.NOTE", testNote.id)
        }.let {
            activityTestRule.launchActivity(it)
        }
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun should_show_color_picker() {
        onView(withId(R.id.pallete)).perform(click())
        onView(withId(R.id.colorPicker)).check(matches(isCompletelyDisplayed()))
    }

}
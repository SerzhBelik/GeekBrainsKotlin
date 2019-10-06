package com.example.myapplication.ui.note

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.example.myapplication.R
import com.example.myapplication.common.format
import com.example.myapplication.common.getColorInt
import com.example.myapplication.data.entity.Note
import com.example.myapplication.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.toolbar
import kotlinx.android.synthetic.main.activity_note.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.contracts.ExperimentalContracts


class NoteActivity : BaseActivity<Note?, NoteViewState>() {

    companion object {
        private val EXTRA_NOTE = NoteActivity::class.java.name + "extra.NOTE"
        private const val DATE_TIME_FORMAT = "dd.MM.yy HH:mm"

        fun start(context: Context, note: Note? = null) {
            val intent = Intent(context, NoteActivity::class.java).apply {
                note?.let {
                    putExtra(EXTRA_NOTE, it.id)
                }
            }
            context.startActivity(intent)
        }
    }

    private var color: Note.Color = Note.Color.WHITE
    private var note: Note? = null
    override val layoutRes = R.layout.activity_note
    override val model: NoteViewModel by viewModel()

    val textChangeListener = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            saveNote()
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val noteId = intent.getStringExtra(EXTRA_NOTE)
        noteId?.let {
            model.loadNote(it)
        } ?: let {
            getString(R.string.new_note_title)
        }
    }

    @ExperimentalContracts
    override fun renderData(data: Note?) {
        this.note = data
        supportActionBar?.title = note?.let { note ->
            note.lastChanged.format(DATE_TIME_FORMAT)
        } ?: getString(R.string.new_note_title)
        initView()
    }

    private fun initView() {
        et_title.removeTextChangedListener(textChangeListener)
        et_body.removeTextChangedListener(textChangeListener)

        note?.let { note ->
            et_title.setText(note.title)
            et_body.setText(note.text)
            toolbar.setBackgroundColor(note.color.getColorInt(this))
        }

        et_title.addTextChangedListener(textChangeListener)
        et_body.addTextChangedListener(textChangeListener)


        colorPicker.onColorClickListener = { color ->
            this.color = color
            toolbar.setBackgroundColor(color.getColorInt(this))
            saveNote()
        }
    }

    private fun saveNote() {
        if (et_title.text == null || (et_title.text?.length ?: 0) < 3)
            return

        note = note?.copy(
                title = et_title.text.toString(),
                text = et_body.text.toString(),
                lastChanged = Date(),
                color = color
        ) ?: Note(
                UUID.randomUUID().toString(),
                et_title.text.toString(),
                et_body.text.toString()
        )

        note?.let { model.save(it) }
    }

    override fun onCreateOptionsMenu(menu: Menu?) = menuInflater.inflate(R.menu.note, menu).let { true }


    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        android.R.id.home -> onBackPressed().let { true }
        R.id.pallete -> togglePallete().let { true }
        R.id.delete -> deleteNote().let { true }
        else -> super.onOptionsItemSelected(item)
    }


    private fun togglePallete() {
        if (colorPicker.isOpen) {
            colorPicker.close()
        } else {
            colorPicker.open()
        }
    }

    private fun deleteNote() {

    }
}

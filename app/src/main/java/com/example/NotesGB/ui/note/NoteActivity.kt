package com.example.NotesGB.ui.note

import android.R.id.home
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.example.NotesGB.R
import com.example.NotesGB.data.model.Note
import com.example.NotesGB.extensions.DATE_TIME_FORMAT
import kotlinx.android.synthetic.main.activity_note.*
import java.text.SimpleDateFormat
import java.util.*

private const val SAVE_DELAY = 2000L

class NoteActivity : AppCompatActivity() {

    companion object {

        private val EXTRA_NOTE = NoteActivity::class.java.name + "extra.NOTE"

        fun getStartIntent(context: Context, note: Note?): Intent {
            val intent = Intent(context, NoteActivity::class.java)
            intent.putExtra(EXTRA_NOTE, note)
            return intent
        }

    }

    private var note: Note? = null
    private lateinit var viewModel: NoteViewModel

    private val textChangeListener = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }

        @RequiresApi(Build.VERSION_CODES.Q)
        override fun afterTextChanged(s: Editable?) {
            Log.d(com.example.NotesGB.extensions.TAG, "afterTextChanged(), s = " + s.toString())
            triggerSaveNote()
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun triggerSaveNote() {

        val runnable = Runnable(function = {
            note = note?.copy(
                    title = txtNoteTitle.text.toString(),
                    body = txtNoteBody.text.toString(),
                    lastChanged = Date())
                    ?: createNewNote()

            Log.d(com.example.NotesGB.extensions.TAG, "note = " + note.toString())

            viewModel.saveChanges(note!!)
        })

        if (Handler().hasCallbacks(runnable)) Handler().removeCallbacks(runnable)

        Handler().postDelayed(runnable, SAVE_DELAY)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)

        note = intent.getParcelableExtra(EXTRA_NOTE)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = if (note == null) {
            getString(R.string.new_note_title)
        } else {
            SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault()).format(note!!.lastChanged)
        }

        initView()

        viewModel = ViewModelProviders.of(this).get(NoteViewModel::class.java)
    }

    private fun initView() {
        if (note == null) return

        txtNoteTitle.setText(note!!.title)
        txtNoteTitle.addTextChangedListener(textChangeListener)
        txtNoteBody.setText(note!!.body)
        txtNoteBody.addTextChangedListener(textChangeListener)

        @Suppress("DEPRECATION") // minSDK for this app is 21
        toolbar.setBackgroundColor(resources.getColor(note!!.color.id))
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        home -> {
            onBackPressed()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun createNewNote(): Note = Note(UUID.randomUUID().toString(),
            txtNoteTitle.text.toString(),
            txtNoteBody.text.toString())

}

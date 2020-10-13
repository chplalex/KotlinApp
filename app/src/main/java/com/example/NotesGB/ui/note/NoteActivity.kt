package com.example.NotesGB.ui.note

import android.R.id.home
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.NotesGB.R
import com.example.NotesGB.data.model.Note
import com.example.NotesGB.extensions.DATE_TIME_FORMAT
import kotlinx.android.synthetic.main.activity_note.*
import java.text.SimpleDateFormat
import java.util.*

private const val SAVE_DELAY = 2000L

class NoteActivity : AppCompatActivity() {

    companion object {

        private val NOTE_KEY = Note::class.java.name

        fun start(context: Context, note: Note? = null) = Intent(context, NoteActivity::class.java).apply {
            putExtra(NOTE_KEY, note)
            context.startActivity(this)
        }

    }

    private var note: Note? = null
    private lateinit var viewModel: NoteViewModel
    private val handler: Handler = Handler(Looper.getMainLooper())

    private val textChangeListener = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable?): Unit = with(handler) {
            Log.d(com.example.NotesGB.extensions.TAG, "afterTextChanged(), s = $s")
            removeCallbacks(saveNoteIntoViewModel)
            postDelayed(saveNoteIntoViewModel, SAVE_DELAY)
        }
    }

    private val saveNoteIntoViewModel = Runnable(function = {
        note = note?.copy(
                title = txtNoteTitle.text.toString(),
                body = txtNoteBody.text.toString(),
                lastChanged = Date())
                ?: newNote()

        note?.let { viewModel.saveChanges(it) }

        Log.d(com.example.NotesGB.extensions.TAG, "saveNoteIntoViewModel(), title = ${note?.title}; body = ${note?.body}")
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)

        viewModel = ViewModelProvider(this).get(NoteViewModel::class.java)
        note = intent.getParcelableExtra(NOTE_KEY)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = note?.lastChanged?.let {
            SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault()).format(it)
        } ?: getString(R.string.new_note_title)

        initView()
    }

    private fun initView() {
        note?.let {
            txtNoteTitle.setText(it.title)
            txtNoteBody.setText(it.body)
            @Suppress("DEPRECATION")
            toolbar.setBackgroundColor(resources.getColor(it.color.id))
        }
        txtNoteTitle.addTextChangedListener(textChangeListener)
        txtNoteBody.addTextChangedListener(textChangeListener)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        home -> {
            saveNoteIntoViewModel.run()
            onBackPressed()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun newNote(): Note = Note(UUID.randomUUID().toString(),
            txtNoteTitle.text.toString(),
            txtNoteBody.text.toString())

}

package com.example.NotesGB.ui.note

import android.R.id.home
import android.content.Context
import android.content.Intent
import android.os.*
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import com.example.NotesGB.R
import com.example.NotesGB.data.model.Note
import com.example.NotesGB.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_note.*
import java.util.*

private const val SAVE_DELAY = 2000L

class NoteActivity : BaseActivity<Note?, NoteViewState>() {

    companion object {

        private val NOTE_KEY = Note::class.java.name

        fun start(context: Context, id: String? = null) = Intent(context, NoteActivity::class.java).apply {
            putExtra(NOTE_KEY, id)
            context.startActivity(this)
        }

    }

    override val layoutRes = R.layout.activity_note

    override val viewModel: NoteViewModel by lazy {
        ViewModelProvider(this).get(NoteViewModel::class.java)
    }

    private var note: Note? = null

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

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val id = intent.getStringExtra(NOTE_KEY)
        id?.let { viewModel.loadNote(it) }

        if (id == null) supportActionBar?.title = getString(R.string.new_note_title)

        txtNoteTitle.addTextChangedListener(textChangeListener)
        txtNoteBody.addTextChangedListener(textChangeListener)
    }

    private fun initView() = note?.let {
        txtNoteTitle.setText(it.title)
        txtNoteBody.setText(it.body)
        @Suppress("DEPRECATION")
        toolbar.setBackgroundColor(resources.getColor(it.color.id))
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

    override fun renderData(data: Note?) {
        note = data
        initView()
    }
}

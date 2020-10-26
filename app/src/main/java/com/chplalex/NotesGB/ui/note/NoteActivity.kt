package com.chplalex.NotesGB.ui.note

import android.R.id.home
import android.content.Context
import android.content.Intent
import android.os.*
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chplalex.NotesGB.R
import com.chplalex.NotesGB.data.model.Color
import com.chplalex.NotesGB.data.model.Note
import com.chplalex.NotesGB.extensions.format
import com.chplalex.NotesGB.extensions.getColorInt
import com.chplalex.NotesGB.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_note.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.startActivity
import java.util.*

private const val SAVE_DELAY = 2000L

class NoteActivity : BaseActivity<Note?, NoteViewState>() {

    companion object {

        private val NOTE_KEY = Note::class.java.name

        fun start(context: Context, id: String? = null) =
                context.startActivity<NoteActivity>(NOTE_KEY to id)

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
            Log.d(com.chplalex.NotesGB.extensions.TAG, "afterTextChanged(), s = $s")
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

        Log.d(com.chplalex.NotesGB.extensions.TAG, "saveNoteIntoViewModel(), title = ${note?.title}; body = ${note?.body}")
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

    private fun initView() = note?.run {
        supportActionBar?.title = lastChanged.format()
        txtNoteTitle.setText(title)
        txtNoteBody.setText(body)
        toolbar.setBackgroundColor(color.getColorInt(this@NoteActivity))
    }

    override fun onCreateOptionsMenu(menu: Menu?) =
            menuInflater.inflate(R.menu.menu_note, menu)?.let { true }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        home -> {
            saveNoteIntoViewModel.run()
            onBackPressed()
            true
        }
        R.id.menu_note_palette -> togglePalette().let { true }
        R.id.menu_note_delete -> deleteNote().let { true }
        else -> super.onOptionsItemSelected(item)
    }

    private fun newNote(): Note = Note(UUID.randomUUID().toString(),
            txtNoteTitle.text.toString(),
            txtNoteBody.text.toString())

    private fun deleteNote() {
        alert {
            titleResource = R.string.delete_note_title
            messageResource = R.string.delete_note_message
            positiveButton(R.string.ok_btn_title) { ViewModel.deleteNote() }
            negativeButton(R.string.cancel_btn_title) { dialog -> dialog.dismiss() }
        }.show()
    }

    private fun togglePalette() {

    }

    override fun renderData(data: Note?) {
        note = data
        initView()
    }
}
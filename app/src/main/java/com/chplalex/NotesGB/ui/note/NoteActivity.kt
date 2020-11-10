package com.chplalex.notesgb.ui.note

import android.content.Context
import android.content.Intent
import android.os.*
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import com.chplalex.notesgb.R
import com.chplalex.notesgb.data.model.Color.*
import com.chplalex.notesgb.data.model.Note
import com.chplalex.notesgb.common.DATE_TIME_FORMAT
import com.chplalex.notesgb.common.getColorInt
import com.chplalex.notesgb.ui.base.BaseActivity
import com.chplalex.ui.note.NoteData
import kotlinx.android.synthetic.main.activity_note.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

const val SAVE_DELAY = 2000L

class NoteActivity : BaseActivity<NoteData>() {

    companion object {

        val NOTE_KEY = NoteActivity::class.java.name

        fun start(context: Context, id: String? = null) =
                Intent(context, NoteActivity::class.java).apply {
                    putExtra(NOTE_KEY, id)
                    context.startActivity(this)
                }

    }

    override val layoutRes = R.layout.activity_note
    override val viewModel: NoteViewModel by viewModel()

    private var note: Note? = null
    private var color = WHITE
    private val handler: Handler = Handler(Looper.getMainLooper())

    private val textChangeListener = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable?) {
            if (txtNoteTitle.text == null ||
                    (txtNoteTitle.text!!.length < 3 && txtNoteBody.text.length < 3)) return

            launch {
                delay(SAVE_DELAY)
                saveNoteIntoViewModel()
            }
        }
    }

    private fun saveNoteIntoViewModel() {
        note = note?.copy(
                title = txtNoteTitle.text.toString(),
                body = txtNoteBody.text.toString(),
                lastChanged = Date(),
                color = color)
                ?: newNote()

        note?.let { viewModel.saveChanges(it) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val noteId = intent.getStringExtra(NOTE_KEY)
        noteId?.let {
            viewModel.loadNote(it)
        } ?: let {
            supportActionBar?.title = getString(R.string.new_note_title)
        }

        initView()
    }

    private fun initView() {
        txtNoteTitle.removeTextChangedListener(textChangeListener)
        txtNoteBody.removeTextChangedListener(textChangeListener)

        note?.let {
            txtNoteTitle.setText(it.title)
            txtNoteBody.setText(it.body)
            color = it.color
            toolbar.setBackgroundColor(it.color.getColorInt(this))
        }

        colorPicker.onColorClickListener = {
            toolbar.setBackgroundColor(it.getColorInt(this))
            color = it
            saveNoteIntoViewModel()
        }

        txtNoteTitle.addTextChangedListener(textChangeListener)
        txtNoteBody.addTextChangedListener(textChangeListener)
    }

    override fun onCreateOptionsMenu(menu: Menu?) =
            menuInflater.inflate(R.menu.menu_note, menu).let { true }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                saveNoteIntoViewModel()
                onBackPressed()
                true
            }
            R.id.menu_note_palette -> togglePalette().let { true }
            R.id.menu_note_delete -> deleteNote().let { true }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun newNote(): Note = Note(UUID.randomUUID().toString(),
            txtNoteTitle.text.toString(),
            txtNoteBody.text.toString(),
            color)

    private fun deleteNote() {
        AlertDialog.Builder(this)
                .setTitle(R.string.delete_note_title)
                .setMessage(R.string.delete_note_message)
                .setPositiveButton(R.string.ok_btn_title) { _, _ ->
                    run {
                        viewModel.deleteNote()
                        finish()
                    }
                }
                .setNegativeButton(R.string.cancel_btn_title) { dialog, _ -> dialog.dismiss() }
                .show()
    }

    private fun togglePalette() {
        if (colorPicker.isOpen) {
            colorPicker.close()
        } else {
            colorPicker.open()
        }
    }

    override fun renderData(data: NoteData) {
        if (data.isDeleted) finish()

        note = data.note
        supportActionBar?.title = note?.lastChanged?.let {
            SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault()).format(it)
        } ?: getString(R.string.new_note_title)

        initView()
    }
}
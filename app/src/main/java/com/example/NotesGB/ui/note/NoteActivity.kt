package com.example.NotesGB.ui.note

import android.R.id.home
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.NotesGB.R
import com.example.NotesGB.data.model.Note
import com.example.NotesGB.extensions.DATE_TIME_FORMAT
import kotlinx.android.synthetic.main.activity_note.*
import java.text.SimpleDateFormat
import java.util.*

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
    }

    private fun initView() {
        if (note == null) return

        txtNoteTitle.setText(note!!.title)
        txtNoteBody.setText(note!!.body)

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

}

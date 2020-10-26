package com.chplalex.NotesGB.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.ViewModelProvider
import com.chplalex.NotesGB.R
import com.chplalex.NotesGB.data.model.Note
import com.chplalex.NotesGB.ui.base.BaseActivity
import com.chplalex.NotesGB.ui.note.NoteActivity
import com.chplalex.NotesGB.ui.splash.SplashActivity
import com.firebase.ui.auth.AuthUI
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.alert

class MainActivity : BaseActivity<List<Note>?, MainViewState>() {

    companion object {
        fun getStartIntent(context: Context) = Intent(context, MainActivity::class.java)
        fun start(context: Context) = Intent(context, MainActivity::class.java).apply {
            context.startActivity(this)
        }
    }

    override val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }
    override val layoutRes: Int = R.layout.activity_main
    private lateinit var adapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)

        adapter = MainAdapter { NoteActivity.start(context = this, id = it.id) }

        mainRecycler.adapter = adapter

        fab.setOnClickListener { NoteActivity.start(context = this) }
    }

    override fun renderData(data: List<Note>?) {
        data?.let { adapter.notes = it }
    }

    override fun onCreateOptionsMenu(menu: Menu?) =
            MenuInflater(this).inflate(R.menu.menu_main, menu).let { true }

    override fun onOptionsItemSelected(item: MenuItem) =
            when (item.itemId) {
                R.id.menu_logout -> showLogoutDialog().let { true }
                else -> super.onOptionsItemSelected(item)
            }

    private fun showLogoutDialog() {
        alert {
            titleResource = R.string.logout_dialog_title
            messageResource = R.string.logout_dialog_message
            positiveButton(R.string.ok_btn_title) { onLogout() }
            negativeButton(R.string.cancel_btn_title) { dialog -> dialog.dismiss() }
        }.show()
    }

    fun onLogout() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener {
                    startActivity(Intent(this, SplashActivity::class.java))
                    finish()
                }
    }
}
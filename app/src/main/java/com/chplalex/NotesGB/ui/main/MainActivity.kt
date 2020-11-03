package com.chplalex.notesgb.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.chplalex.notesgb.R
import com.chplalex.notesgb.data.model.Note
import com.chplalex.notesgb.ui.base.BaseActivity
import com.chplalex.notesgb.ui.note.NoteActivity
import com.chplalex.notesgb.ui.splash.SplashActivity
import com.firebase.ui.auth.AuthUI
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity<List<Note>?, MainViewState>() {

    companion object {
        fun start(context: Context) = Intent(context, MainActivity::class.java).apply {
            context.startActivity(this)
        }
    }

    override val viewModel: MainViewModel by viewModel()
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
        AlertDialog.Builder(this)
                .setTitle(R.string.logout_dialog_title)
                .setMessage(R.string.logout_dialog_message)
                .setPositiveButton(R.string.ok_btn_title) { _, _ -> onLogout() }
                .setNegativeButton(R.string.cancel_btn_title) { dialog, _ -> dialog.dismiss() }
                .show()
    }

    fun onLogout() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener {
                    SplashActivity.start(this)
                    finish()
                }
    }
}
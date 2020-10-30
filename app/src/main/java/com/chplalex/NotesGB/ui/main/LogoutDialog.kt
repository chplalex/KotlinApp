package com.chplalex.NotesGB.ui.main

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.chplalex.NotesGB.R

class LogoutDialog : DialogFragment() {

    interface LogoutListener {
        fun onLogout()
    }

    companion object {
        val TAG = LogoutDialog::class.java.name + "_TAG"
        fun createInstance() = LogoutDialog()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
            AlertDialog.Builder(context!!)
                    .setTitle(R.string.logout_dialog_title)
                    .setMessage(R.string.logout_dialog_message)
                    .setPositiveButton(R.string.ok_btn_title) {  _, _ -> (activity as LogoutListener).onLogout() }
                    .setNegativeButton(R.string.logout_dialog_cancel) { _, _ -> dismiss() }
                    .create()
}
package com.chplalex.notesgb.ui.base

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.chplalex.notesgb.R
import com.chplalex.notesgb.data.errors.NoAuthException
import com.firebase.ui.auth.AuthUI


abstract class BaseActivity<T, S : BaseViewState<T>> : AppCompatActivity() {

    companion object {
        private const val RC_SIGN_IN = 4242
    }

    abstract val viewModel: BaseViewModel<T, S>
    abstract val layoutRes: Int?

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutRes?.let { setContentView(it) }

        viewModel.getViewState().observe(this, { t ->
            t?.error?.let { renderError(it) }
            t?.data?.let { renderData(it) }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_SIGN_IN && resultCode != RESULT_OK) {
            finish()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    protected fun renderError(error: Throwable) {
        when (error) {
            is NoAuthException -> startLoginActivity()
            else -> error.message?.let { showError(it) }
        }
    }

    private fun startLoginActivity() {
        val providers = listOf(
                AuthUI.IdpConfig.GoogleBuilder().build()
        )

        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setLogo(R.drawable.ic_notes)
                        .setTheme(R.style.LoginTheme)
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN
        )
    }

    abstract fun renderData(data: T)

    private fun showError(error: String) =
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
}
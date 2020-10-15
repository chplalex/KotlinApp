package com.example.NotesGB.ui.base

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.NotesGB.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*

abstract class BaseActivity<T, S : BaseViewState<T>> : AppCompatActivity() {

    abstract val viewModel: BaseViewModel<T, S>
    abstract val layoutRes: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutRes)

        viewModel.getViewState().observe(this, { t ->
            t?.data?.let { renderData(it) }
            t?.error?.let { renderError(it) }
        })
    }

    protected fun renderError(error: Throwable) = error.message?.let { showError(it) }

    abstract fun renderData(data: T)

    private fun showError(error: String) {
        val snackbar = Snackbar.make(mainRecycler, error, Snackbar.LENGTH_INDEFINITE)
        snackbar.setAction(R.string.ok_btn_title, View.OnClickListener { snackbar.dismiss() })
        snackbar.show()
    }
}
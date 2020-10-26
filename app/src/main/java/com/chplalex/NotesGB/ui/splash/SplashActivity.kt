package com.chplalex.NotesGB.ui.splash

import android.os.Handler
import androidx.lifecycle.ViewModelProvider
import com.chplalex.NotesGB.R
import com.chplalex.NotesGB.ui.base.BaseActivity
import com.chplalex.NotesGB.ui.base.BaseViewModel
import com.chplalex.NotesGB.ui.main.MainActivity

private const val START_DELAY = 1000L

class SplashActivity : BaseActivity<Boolean?, SplashViewState>() {

    override val viewModel by lazy {
        ViewModelProvider(this).get(SplashViewModel::class.java)
    }

    override val layoutRes: Int? = null

    override fun renderData(data: Boolean?) {
        data?.takeIf { it }.let {
            MainActivity.start(this)
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.requestUser()
    }
}
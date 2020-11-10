package com.chplalex.notesgb.ui.splash

import com.chplalex.notesgb.data.Repository
import com.chplalex.notesgb.data.errors.NoAuthException
import com.chplalex.notesgb.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class SplashViewModel(private val repository: Repository) : BaseViewModel<Boolean>() {

    fun requestUser() = launch {
        repository.getCurrentUser()?.let { setData(true) } ?: setError(NoAuthException())
    }

}
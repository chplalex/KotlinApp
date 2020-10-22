package com.chplalex.NotesGB.ui.splash

import com.chplalex.NotesGB.data.Repository
import com.chplalex.NotesGB.data.errors.NoAuthException
import com.chplalex.NotesGB.ui.base.BaseViewModel

class SplashViewModel(private val repository: Repository = Repository) :
        BaseViewModel<Boolean?, SplashViewState>() {

    fun requestUser() = repository.getCurrentUser().observeForever {
        viewStateLiveData.value =
                it?.let { SplashViewState(isAuth = true) } ?: SplashViewState(error = NoAuthException())
    }
}
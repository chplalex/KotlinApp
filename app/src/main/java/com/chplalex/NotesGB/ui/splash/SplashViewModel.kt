package com.chplalex.NotesGB.ui.splash

import androidx.lifecycle.Observer
import com.chplalex.NotesGB.data.Repository
import com.chplalex.NotesGB.data.errors.NoAuthException
import com.chplalex.NotesGB.ui.base.BaseViewModel
import com.firebase.ui.auth.data.model.User

class SplashViewModel(private val repository: Repository = Repository) :
        BaseViewModel<Boolean?, SplashViewState>() {

    private val splashObserver = Observer<User?> { t ->
        viewStateLiveData.value =
                t?.let { SplashViewState(isAuth = true) } ?: SplashViewState(error = NoAuthException())
    }

    // TODO: доделать removeObserver()
    fun requestUser() = repository.getCurrentUser().observeForever {
        viewStateLiveData.value =
                it?.let { SplashViewState(isAuth = true) } ?: SplashViewState(error = NoAuthException())

    }
}
package com.chplalex.NotesGB.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.chplalex.NotesGB.data.Repository
import com.chplalex.NotesGB.data.errors.NoAuthException
import com.chplalex.NotesGB.data.model.User
import com.chplalex.NotesGB.ui.base.BaseViewModel

class SplashViewModel(private val repository: Repository = Repository) :
        BaseViewModel<Boolean?, SplashViewState>() {

    private var splashLiveData: LiveData<User?>? = null

    private val splashObserver = Observer<User?> { t ->
        viewStateLiveData.value =
                t?.let { SplashViewState(isAuth = true) } ?: SplashViewState(error = NoAuthException())
    }

    fun requestUser() {
        splashLiveData = repository.getCurrentUser()
        splashLiveData?.observeForever(splashObserver)
    }

    override fun onCleared() {
        splashLiveData?.removeObserver(splashObserver)
        super.onCleared()
    }
}
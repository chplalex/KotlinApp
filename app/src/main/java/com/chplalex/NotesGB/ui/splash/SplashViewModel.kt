package com.chplalex.notesgb.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.chplalex.notesgb.data.Repository
import com.chplalex.notesgb.data.errors.NoAuthException
import com.chplalex.notesgb.data.model.User
import com.chplalex.notesgb.ui.base.BaseViewModel

class SplashViewModel(private val repository: Repository) : BaseViewModel<Boolean?, SplashViewState>() {

    private var splashLiveData: LiveData<User?>? = null

    private val splashObserver = Observer<User?> { t ->
        viewStateLiveData.value =
                t?.let { SplashViewState(isAuth = true) } ?: SplashViewState(error = NoAuthException())
    }

    fun requestUser() {
        splashLiveData = repository.getCurrentUser()
        splashLiveData?.observeForever(splashObserver)
    }

    public override fun onCleared() {
        splashLiveData?.removeObserver(splashObserver)
        super.onCleared()
    }
}
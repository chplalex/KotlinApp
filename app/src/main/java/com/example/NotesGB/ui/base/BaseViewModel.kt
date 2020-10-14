package com.example.NotesGB.ui.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class BaseViewModel<T, S: BaseViewState<T>> : ViewModel() {

    open val viewStateLivedata = MutableLiveData<S>()
    open fun getViewState(): LiveData<S> = viewStateLivedata

}
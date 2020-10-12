package com.example.NotesGB.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.NotesGB.data.Repository

class MainViewModel : ViewModel() {

    private val viewStateLiveData: MutableLiveData<MainViewState> = MutableLiveData()

    init {
        viewStateLiveData.value = MainViewState(Repository.notes)

        Repository.getNotes().observeForever {
            viewStateLiveData.value =
                viewStateLiveData.value?.copy(notes = it!!) ?: MainViewState(it!!)
        }
    }

    fun viewState(): LiveData<MainViewState> = viewStateLiveData
}
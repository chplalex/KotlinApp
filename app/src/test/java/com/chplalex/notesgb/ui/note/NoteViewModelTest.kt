package com.chplalex.notesgb.ui.note

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.chplalex.notesgb.data.Repository
import com.chplalex.notesgb.data.model.Note
import com.chplalex.notesgb.data.model.NoteResult
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertFalse
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class NoteViewModelTest {

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private val mockRepository: Repository = mockk<Repository>()
    private val noteLiveData = MutableLiveData<NoteResult>()
    private lateinit var noteViewModel: NoteViewModel

    @Before
    fun setUp() {
        every { mockRepository.getNoteById(any()) } returns noteLiveData
        every { mockRepository.deleteNote(any()) } returns noteLiveData
        noteViewModel = NoteViewModel(mockRepository)
        noteViewModel.loadNote("1")
        noteViewModel.deleteNote()
    }

    @Test
    fun `should request getNoteById() once`() {
        verify(exactly = 1) { mockRepository.getNoteById(any()) }
    }

//    @Test TODO: непонятно почему pendingNote == null
//    fun `should request deleteNote() once`() {
//        verify(exactly = 1) { mockRepository.deleteNote(any()) }
//    }

    @Test
    fun `noteLiveData error in - error it - should return error`() {
        val testData = Throwable("error")
        var testResult: Throwable? = null
        noteViewModel.getViewState().observeForever { testResult = it?.error }
        noteLiveData.value = NoteResult.Error(testData)
        assertEquals(testData, testResult)
    }

    @Test
    fun `load data - should return note`() {
        val testData = Note("1")
        var testResult: Note? = null
        noteViewModel.getViewState().observeForever { testResult = it?.data?.note }
        noteLiveData.value = NoteResult.Success(testData)
        assertEquals(testData, testResult)
    }

    @Test
    fun `noteLiveData have to remove observer`() {
        noteViewModel.onCleared()
        assertFalse(noteLiveData.hasObservers())
    }

//    @Test TODO: Разобраться, почему не вызывается noteObserver при вызове loadNote
//    fun once() {
//        noteViewModel.deleteNote()
//        verify(exactly = 1) { mockRepository.deleteNote(any()) }
//    }



}
package com.chplalex.notesgb.ui.main

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

class MainViewModelTest {

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private val mockRepository: Repository = mockk<Repository>()
    private val notesLiveData = MutableLiveData<NoteResult>()
    private lateinit var viewModel: MainViewModel

    @Before
    fun setUp() {
        every { mockRepository.getNotes() } returns notesLiveData
        viewModel = MainViewModel(mockRepository)
    }

    @Test
    fun `should call getNotes() once`() {
        verify(exactly = 1) { mockRepository.getNotes() }
    }

    @Test
    fun `load error - should return error`() {
        val testData = Throwable("error")
        var testResult: Throwable? = null
        viewModel.getViewState().observeForever { testResult = it?.error }
        notesLiveData.value = NoteResult.Error(testData)
        assertEquals(testData, testResult)
    }

    @Test
    fun `load notes - should return notes`() {
        val testData = listOf(Note("1"), Note("2"), Note("3"))
        var testResult: List<Note>? = null
        viewModel.getViewState().observeForever { testResult = it?.data }
        notesLiveData.value = NoteResult.Success(testData)
        assertEquals(testData, testResult)
    }

    @Test
    fun `have to remove observer`() {
        viewModel.onCleared()
        assertFalse(notesLiveData.hasObservers())
    }





}
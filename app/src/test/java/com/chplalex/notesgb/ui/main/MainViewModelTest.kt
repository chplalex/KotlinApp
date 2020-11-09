package com.chplalex.notesgb.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.chplalex.notesgb.data.Repository
import com.chplalex.notesgb.data.model.Note
import com.chplalex.notesgb.data.model.NoteResult
import com.google.firebase.FirebaseException
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MainViewModelTest {

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private val mockError = mockk<FirebaseException>()
    private val mockRepository: Repository = mockk<Repository>()
    private val dataChannel = Channel<NoteResult>(Channel.CONFLATED)
    @ExperimentalCoroutinesApi
    private lateinit var viewModel: MainViewModel

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        every { mockRepository.getNotes() } returns dataChannel
        viewModel = MainViewModel(mockRepository)
    }

    @Test
    fun `should call getNotes() once`() {
        verify(exactly = 1) { mockRepository.getNotes() }
    }

    @Test
    fun `load error - should return error`() = runBlocking {
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `load notes - should return notes`() = runBlocking {
        val testData = listOf(Note("1"), Note("2"), Note("3"))
        val deferred = async (Dispatchers.IO) {
            viewModel.getDataChannel().receive()
        }
        dataChannel.send(NoteResult.Success(testData))
        val resultData = deferred.await()
        assertEquals(testData, resultData)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `onCleared() have to close channels`() {
        viewModel.onCleared()
        assertTrue(viewModel.getDataChannel().isClosedForReceive)
        assertTrue(viewModel.getErrorChannel().isClosedForReceive)
    }

}
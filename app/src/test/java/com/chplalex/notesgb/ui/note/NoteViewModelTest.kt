package com.chplalex.notesgb.ui.note

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.chplalex.notesgb.data.Repository
import com.chplalex.notesgb.data.model.Note
import com.chplalex.notesgb.data.model.NoteResult
import com.google.firebase.firestore.FirebaseFirestoreException
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.Assert.*
import kotlinx.coroutines.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class NoteViewModelTest {

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private val mockError = mockk<FirebaseFirestoreException>()
    private val mockRepository: Repository = mockk<Repository>()
    private lateinit var noteViewModel: NoteViewModel

    @Before
    fun setUp() {
        noteViewModel = NoteViewModel(mockRepository)
    }

    @Test
    fun `should request getNoteById() once`() {
//        verify(exactly = 1) { mockRepository.getNoteById(any()) }
    }

    @Test
    fun `when data loading throws error - should return error`() = runBlocking {
        val id = "1"
        coEvery { mockRepository.getNoteById(id) } throws (mockError)

        val deferred = async (Dispatchers.IO) {
            noteViewModel.getErrorChannel().receive()
        }

        noteViewModel.loadNote(id)

        assertEquals(mockError, deferred.await())
    }

    @Test
    fun `when note loaded - should return note`() = runBlocking {
        val id = "1"
        val testData = Note(id)

        coEvery { mockRepository.getNoteById(id) } returns testData

        val deferred = async (Dispatchers.IO) {
            noteViewModel.getDataChannel().receive()
        }

        noteViewModel.loadNote(id)

        val testResult = deferred.await().note

        assertEquals(testData, testResult)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `onCleared have to close channels`() = runBlocking {
        noteViewModel.onCleared()

        while (!noteViewModel.getDataChannel().isClosedForReceive && !noteViewModel.getErrorChannel().isClosedForReceive) {
            delay(10)
        }

        assertTrue(noteViewModel.getDataChannel().isClosedForReceive)
        assertTrue(noteViewModel.getErrorChannel().isClosedForReceive)
    }

}
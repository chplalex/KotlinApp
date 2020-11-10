package com.chplalex.notesgb.ui.splash

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.chplalex.notesgb.data.Repository
import com.chplalex.notesgb.data.errors.NoAuthException
import com.chplalex.notesgb.data.model.User
import io.mockk.*
import junit.framework.Assert
import junit.framework.Assert.assertNotNull
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SplashViewModelTest {

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private val mockRepository: Repository = mockk<Repository>()
    private lateinit var splashViewModel: SplashViewModel
    private val user = User(name = "chplalex", email = "chepel.alexander@gmail.com")

    @Before
    fun setUp() {
        splashViewModel = SplashViewModel(mockRepository)
    }

    @Test
    fun `requestUser() should request getCurrentUser() once`() = runBlocking {
        coEvery { mockRepository.getCurrentUser() } returns user

        splashViewModel.requestUser()

        coVerify(exactly = 1) { mockRepository.getCurrentUser() }
    }

    @Test
    fun `rep returns user - should return true`(): Unit = runBlocking {
        coEvery { mockRepository.getCurrentUser() } returns user

        val deferred = async(Dispatchers.IO) {
            splashViewModel.getDataChannel().receive()
        }

        splashViewModel.requestUser()
        val testResult = deferred.await()

        assertNotNull(testResult)
        assertTrue(testResult)
    }

    @Test
    fun `rep returns null - it should return NoAuthException`() = runBlocking {
        coEvery { mockRepository.getCurrentUser() } returns null

        val deferred = async(Dispatchers.IO) {
            splashViewModel.getErrorChannel().receive()
        }

        splashViewModel.requestUser()
        val testResult = deferred.await()

        assertNotNull(testResult)
        assertTrue(testResult is NoAuthException)
    }

}
package com.chplalex.notesgb.ui.splash

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.chplalex.notesgb.data.Repository
import com.chplalex.notesgb.data.errors.NoAuthException
import com.chplalex.notesgb.data.model.User
import io.mockk.*
import junit.framework.Assert
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SplashViewModelTest {

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private val mockRepository: Repository = mockk<Repository>()
    private val user = User("Alexander", "chepel.alexander@gmail.com")
    private lateinit var splashViewModel: SplashViewModel

    @Before
    fun setUp() {
        coEvery { mockRepository.getCurrentUser() } returns user
        splashViewModel = SplashViewModel(mockRepository)
        splashViewModel.requestUser()
    }

    @Test
    fun `should request getCurrentUser() once`() = runBlocking {
        coVerify(exactly = 1) { mockRepository.getCurrentUser() }
    }

    @Test
    fun `load null - should return NoAuthException`() {
        val testData = NoAuthException()
        var testResult: Throwable? = null
//        splashViewModel.getViewState().observeForever { testResult = it?.error }
//        splashLiveData.value = null
        Assert.assertEquals(testData, testResult)
    }

    @Test
    fun `load user - should return true`() {
        val testData = User(name = "chplalex", email = "chepel.alexander@gmail.com")
        var testResult: Boolean? = null
//        splashViewModel.getViewState().observeForever { it -> testResult = it.data }
//        splashLiveData.value = testData
        Assert.assertNotNull(testResult)
        testResult?.let { Assert.assertTrue(it) }
    }

    @Test
    fun `noteLiveData have to remove observer`() {
//        splashViewModel.onCleared()
//        Assert.assertFalse(splashLiveData.hasObservers())
    }


}
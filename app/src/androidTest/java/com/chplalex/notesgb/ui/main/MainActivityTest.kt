package com.chplalex.notesgb.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import io.mockk.every
import io.mockk.mockk
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import org.koin.standalone.StandAloneContext.loadKoinModules
import org.koin.standalone.StandAloneContext.stopKoin

import com.chplalex.notesgb.R
import com.chplalex.notesgb.data.model.Note
import com.chplalex.notesgb.ui.note.NoteActivity
import com.chplalex.notesgb.ui.note.NoteActivity.Companion.NOTE_KEY
import com.chplalex.notesgb.ui.note.NoteViewModel
import com.chplalex.notesgb.ui.splash.SplashViewModel
import org.hamcrest.Matchers.allOf

class MainActivityTest {

    @get:Rule
    val mainActivityTestRule = IntentsTestRule(MainActivity::class.java, true, false)

    private val viewModel: MainViewModel = mockk(relaxed = true)
    private val viewStateLiveData = MutableLiveData<MainViewState>()

    private val testNotes = listOf(
            Note("1", "title1", "text1"),
            Note("2", "title2", "text2"),
            Note("3", "title3", "text3"),
    )

    @Before
    fun setUp() {
        loadKoinModules(
                listOf(
                        module {
                            viewModel { viewModel }
                            viewModel { mockk<NoteViewModel>(relaxed = true) }
                            viewModel { mockk<SplashViewModel>(relaxed = true) }
                        }
                )
        )

        every { viewModel.getDataChannel() } returns viewStateLiveData

        mainActivityTestRule.launchActivity(null)
        viewStateLiveData.postValue(MainViewState(notes = testNotes))
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun check_data_is_displayed() {
        onView(withId(R.id.mainRecycler))
                .perform(RecyclerViewActions
                        .scrollToPosition<MainAdapter.NoteViewHolder>(0))
        for (note in testNotes) onView(withText(note.body)).check(matches(isDisplayed()))
    }

    @Test
    fun check_activity_launch_with_clicked_note() {
        onView(withId(R.id.mainRecycler))
                .perform(RecyclerViewActions
                        .actionOnItemAtPosition<MainAdapter.NoteViewHolder>(0, click()))
        intended(allOf(hasComponent(NoteActivity::class.java.name), hasExtra(NOTE_KEY, testNotes[0].id)))
    }

    @Test
    fun check_launch_activity_with_new_note() {
        onView(withId(R.id.fab)).perform(click())
        intended(allOf(hasComponent(NoteActivity::class.java.name), hasExtra(NOTE_KEY, null)))
    }

}
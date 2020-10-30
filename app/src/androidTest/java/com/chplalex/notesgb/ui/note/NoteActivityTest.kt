package com.chplalex.notesgb.ui.note

import android.content.Intent
import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import com.chplalex.notesgb.R
import com.chplalex.notesgb.data.model.Note
import io.mockk.*
import junit.framework.TestCase
import org.hamcrest.core.IsNot.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.android.viewmodel.experimental.builder.viewModel
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import org.koin.standalone.StandAloneContext.loadKoinModules
import org.koin.standalone.StandAloneContext.stopKoin

class NoteActivityTest {

    @get:Rule
    val activityTestRule = ActivityTestRule(NoteActivity::class.java, true, false)

    private val viewModel: NoteViewModel = spyk(NoteViewModel(mockk()))
    private val viewStateLiveData = MutableLiveData<NoteViewState>()
    private val testNote = Note("123", "title", "body")

    @Before
    fun setUp() {
        loadKoinModules(listOf(module { viewModel { viewModel } }))

        every { viewModel.getViewState() } returns viewStateLiveData
        every { viewModel.loadNote(any()) } just runs
        every { viewModel.saveChanges(any()) } just runs
        every { viewModel.deleteNote() } just runs

        Intent().apply {
            putExtra(NoteActivity::class.java.name + "_extra.NOTE_ID", testNote.id)
        }.let {
            activityTestRule.launchActivity(it)
        }

    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun should_show_color_picker() {
        onView(withId(R.id.menu_note_palette)).perform(click())
        onView(withId(R.id.colorPicker)).check(matches(isCompletelyDisplayed()))
    }

    @Test
    fun should_hide_color_picker() {
        onView(withId(R.id.menu_note_palette)).perform(click()).perform(click())
        onView(withId(R.id.colorPicker)).check(matches(not(isDisplayed())))
    }




}
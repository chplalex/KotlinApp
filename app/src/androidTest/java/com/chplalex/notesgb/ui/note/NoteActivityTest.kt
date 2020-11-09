package com.chplalex.notesgb.ui.note

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.chplalex.notesgb.R
import com.chplalex.notesgb.common.getColorInt
import com.chplalex.notesgb.data.model.Color
import com.chplalex.notesgb.data.model.Note
import com.chplalex.notesgb.ui.note.NoteActivity.Companion.NOTE_KEY
import io.mockk.*
import junit.framework.Assert.assertTrue
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.core.IsNot.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import org.koin.standalone.StandAloneContext.loadKoinModules
import org.koin.standalone.StandAloneContext.stopKoin

class NoteActivityTest {

    @get:Rule
    val noteActivityTestRule = ActivityTestRule(NoteActivity::class.java, true, false)

    private val viewModel: NoteViewModel = spyk(NoteViewModel(mockk()))
    private val viewStateLiveData = MutableLiveData<NoteViewState>()
    private val testNote = Note("123", "title", "body")

    @Before
    fun setUp() {
        loadKoinModules(listOf(module { viewModel { viewModel } }))

        every { viewModel.getDataChannel() } returns viewStateLiveData
        every { viewModel.loadNote(any()) } just runs
        every { viewModel.saveChanges(any()) } just runs
        every { viewModel.deleteNote() } just runs

        Intent().apply {
            putExtra(NOTE_KEY, testNote.id)
            noteActivityTestRule.launchActivity(this)
        }
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun should_show_color_picker() {
        openActionBarOverflowOrOptionsMenu(noteActivityTestRule.activity)
        onView(withText(R.string.note_palette)).perform(click())  //не работает матчер withId
        onView(withId(R.id.colorPicker)).check(matches(isCompletelyDisplayed()))
    }

    @Test
    fun should_hide_color_picker() {
        openActionBarOverflowOrOptionsMenu(noteActivityTestRule.activity)
        onView(withText(R.string.note_palette)).perform(click())  //не работает матчер withId
        openActionBarOverflowOrOptionsMenu(noteActivityTestRule.activity)
        onView(withText(R.string.note_palette)).perform(click())  //не работает матчер withId
        onView(withId(R.id.colorPicker)).check(matches(not(isDisplayed())))
    }

    @Test
    fun should_set_toolbar_color() {
        openActionBarOverflowOrOptionsMenu(noteActivityTestRule.activity)
        onView(withText(R.string.note_palette)).perform(click())  //не работает матчер withId
        onView(withTagValue(`is`(Color.BLUE))).perform(click())

        val colorInt = Color.BLUE.getColorInt(noteActivityTestRule.activity)

        onView(withId(R.id.toolbar)).check { view, _ ->
            assertTrue("toolbar background color does not match",
                    (view.background as? ColorDrawable)?.color == colorInt)
        }
    }

    @Test
    fun should_call_viewModel_loadNote() {
        verify(exactly = 1) { viewModel.loadNote(testNote.id) }
    }

    @Test
    fun should_show_note() {
        noteActivityTestRule.launchActivity(null)
        viewStateLiveData.postValue(NoteViewState(NoteViewState.Data(note = testNote)))

        onView(withId(R.id.txtNoteTitle)).check(matches(withText(testNote.title)))
        onView(withId(R.id.txtNoteBody)).check(matches(withText(testNote.body)))
    }

    @Test
    fun should_call_saveNote() {
        onView(withId(R.id.txtNoteTitle)).perform(typeText(testNote.title))
        verify(timeout = SAVE_DELAY) { viewModel.saveChanges(any()) }
    }

    @Test
    fun should_call_deleteNote() {
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().context)
        onView(withText(R.string.note_delete)).perform(click())
        onView(withText(R.string.ok_btn_title)).perform(click())
        verify { viewModel.deleteNote() }
    }

}
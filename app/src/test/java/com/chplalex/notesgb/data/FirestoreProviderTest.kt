package com.chplalex.notesgb.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.chplalex.notesgb.data.errors.NoAuthException
import com.chplalex.notesgb.data.model.Note
import com.chplalex.notesgb.data.model.NoteResult
import com.chplalex.notesgb.data.provider.FirestoreProvider
import com.firebase.ui.auth.data.model.User
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*
import io.mockk.*
import junit.framework.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FirestoreProviderTest {

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private val mockDb = mockk<FirebaseFirestore>()
    private val mockAuth = mockk<FirebaseAuth>()
    private val mockCollection = mockk<CollectionReference>()
    private val mockUser = mockk<FirebaseUser>()
    private val mockDocument1 = mockk<DocumentSnapshot>()
    private val mockDocument2 = mockk<DocumentSnapshot>()
    private val mockDocument3 = mockk<DocumentSnapshot>()
    private val testNotes = listOf(Note(id = "1"), Note(id = "2"), Note(id = "3"))

    private val provider: FirestoreProvider = FirestoreProvider(mockDb, mockAuth)

    @Before
    fun setUp() {

        clearMocks(mockCollection, mockDocument1, mockDocument2, mockDocument3)

        every { mockAuth.currentUser } returns mockUser
        every { mockUser.uid } returns ""

        every {
            mockDb.collection(any()).document(any()).collection(any())
        } returns mockCollection

        every { mockDocument1.toObject(Note::class.java) } returns testNotes[0]
        every { mockDocument2.toObject(Note::class.java) } returns testNotes[1]
        every { mockDocument3.toObject(Note::class.java) } returns testNotes[2]

    }

    @Test
    fun `if no auth should throw`() {
        var result: Any? = null
        every { mockAuth.currentUser } returns null
        provider.getNotes().observeForever {
            result = (it as? NoteResult.Error)?.error
        }
        assertTrue(result is NoAuthException)
    }

    @Test
    fun `when db don't return error getNotes() should return notes`() {
        var resultNotes: List<Note>? = null
        val slot = slot<EventListener<QuerySnapshot>>()
        val mockSnapshot = mockk<QuerySnapshot>()

        every { mockSnapshot.documents } returns listOf(mockDocument1, mockDocument2, mockDocument3)
        every { mockCollection.addSnapshotListener(capture(slot)) } returns mockk()

        provider.getNotes().observeForever {
            resultNotes = (it as NoteResult.Success<List<Note>>)?.data
        }

        slot.captured.onEvent(mockSnapshot, null)

        assertEquals(testNotes, resultNotes)
    }

    @Test // TODO: не работает. Непонятно почему
    fun `when db returns error getNotes should return error`() {
        val testData = mockk<FirebaseFirestoreException>()
        var testResult: Throwable? = null
        val slot = slot<EventListener<QuerySnapshot>>()

        every { mockCollection.addSnapshotListener(capture(slot)) } returns mockk()

        provider.getNotes().observeForever {
            testResult = (it as NoteResult.Error).error
        }

        slot.captured.onEvent(null, testData)

        assertEquals(testData, testResult)
    }

    @Test
    fun `saveNote calls document set once`() {
        val mockDocumentReference = mockk<DocumentReference>()
        every { mockCollection.document(testNotes[0].id) } returns mockDocumentReference
        provider.saveNote(testNotes[0])
        verify(exactly = 1) { mockDocumentReference.set(testNotes[0]) }
    }

    @Test
    fun `saveNote return Note`() {
        val testData = testNotes[0]
        var testResult: Note? = null
        val mockDocumentReference = mockk<DocumentReference>()
        val slot = slot<OnSuccessListener<in Void>>()

        every { mockCollection.document(testData.id) } returns mockDocumentReference
        every { mockDocumentReference.set(testData).addOnSuccessListener(capture(slot)) } returns mockk()

        provider.saveNote(testNotes[0]).observeForever {
            testResult = (it as? NoteResult.Success<Note>)?.data
        }

        slot.captured.onSuccess(null)

        assertNotNull(testResult)
        assertEquals(testData, testResult)
    }
}
package com.chplalex.notesgb.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.chplalex.notesgb.data.errors.NoAuthException
import com.chplalex.notesgb.data.model.Note
import com.chplalex.notesgb.data.model.NoteResult
import com.chplalex.notesgb.data.provider.FirestoreProvider
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*
import io.mockk.*
import junit.framework.Assert.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
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
    fun `if no auth should throw`() = runBlocking {
        every { mockAuth.currentUser } returns null
        val noteResult = provider.getNotes().receive()
        assertTrue(noteResult is NoteResult.Error && noteResult.error is NoAuthException)
    }

    @Test
    fun `when db return data getNotes() should return notes`() = runBlocking {
        val slot = slot<EventListener<QuerySnapshot>>()
        val mockSnapshot = mockk<QuerySnapshot>()

        every { mockSnapshot.documents } returns listOf(mockDocument1, mockDocument2, mockDocument3)
        every {
            mockCollection.orderBy("lastChanged", Query.Direction.DESCENDING)
                    .addSnapshotListener(capture(slot))
        } returns mockk()

        val container = async(Dispatchers.IO) {
            provider.getNotes().receive()
        }

        delay(100)

        slot.captured.onEvent(mockSnapshot, null)

        when (val noteResult = container.await()) {
            null -> assertTrue(false)
            is NoteResult.Error -> assertTrue(false)
            is NoteResult.Success<*> -> assertEquals(testNotes, noteResult.data)
        }
    }

    @Test
    fun `when db returns error getNotes should return error`() = runBlocking {
        val testError = mockk<FirebaseFirestoreException>()
        val slot = slot<EventListener<QuerySnapshot>>()

        every {
            mockCollection.orderBy("lastChanged", Query.Direction.DESCENDING)
                    .addSnapshotListener(capture(slot))
        } returns mockk()

        val container = async(Dispatchers.IO) {
            provider.getNotes().receive()
        }

        while (!slot.isCaptured) {
            delay(10)
        }

        slot.captured.onEvent(null, testError)

        when (val testResult = container.await()) {
            null -> assertTrue(false)
            is NoteResult.Success<*> -> assertTrue(false)
            is NoteResult.Error -> assertEquals(testError, testResult.error)
        }

    }

    @Test
    fun `saveNote calls document set once`() = runBlocking {
        val testData = testNotes[0]
        val mockDocumentReference = mockk<DocumentReference>()
        val slotSuccess = slot<OnSuccessListener<in Void>>()

        every { mockCollection.document(testData.id) } returns mockDocumentReference
        every {
            mockDocumentReference.set(testData)
                    .addOnSuccessListener(capture(slotSuccess))
                    .addOnFailureListener(any())
        } returns mockk()

        val container = async(Dispatchers.IO) {
            provider.saveNote(testNotes[0])
        }

        while (!slotSuccess.isCaptured) {
            delay(10)
        }

        slotSuccess.captured.onSuccess(null)

        container.await()

        verify(exactly = 1) { mockDocumentReference.set(testNotes[0]) }
    }

    @Test
    fun `saveNote return Note`() = runBlocking {
        val testData = testNotes[0]
        var testResult: Note? = null
        val mockDocumentReference = mockk<DocumentReference>()
        val slotSuccess = slot<OnSuccessListener<in Void>>()

        every { mockCollection.document(testData.id) } returns mockDocumentReference
        every {
            mockDocumentReference.set(testData)
                    .addOnSuccessListener(capture(slotSuccess))
                    .addOnFailureListener(any())
        } returns mockk()

        val container = async(Dispatchers.IO) {
            provider.saveNote(testNotes[0])
        }

        while (!slotSuccess.isCaptured) {
            delay(10)
        }

        slotSuccess.captured.onSuccess(null)

        testResult = container.await()

        assertNotNull(testResult)
        assertEquals(testData, testResult)
    }

    @Test
    fun `saveNote throws exception`() = runBlocking {
        val testData = testNotes[0]
        val testError = mockk<FirebaseFirestoreException>()
        var testResult: Throwable? = null
        val mockDocumentReference = mockk<DocumentReference>()
        val slotFailure = slot<OnFailureListener>()

        every { mockCollection.document(testData.id) } returns mockDocumentReference
        every {
            mockDocumentReference.set(testData)
                    .addOnSuccessListener(any())
                    .addOnFailureListener(capture(slotFailure))
        } returns mockk()

        val container = async(Dispatchers.IO) {
            try {
                provider.saveNote(testNotes[0])
                null
            } catch (error: Throwable) {
                error
            }
        }
        while (!slotFailure.isCaptured) {
            delay(10)
        }
        slotFailure.captured.onFailure(testError)
        testResult = container.await()

        assertNotNull(testResult)
        assertEquals(testError, testResult)
    }
}
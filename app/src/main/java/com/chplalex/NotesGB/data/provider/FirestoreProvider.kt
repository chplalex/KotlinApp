package com.chplalex.notesgb.data.provider

import com.chplalex.notesgb.data.errors.NoAuthException
import com.chplalex.notesgb.data.model.Note
import com.chplalex.notesgb.data.model.NoteResult
import com.chplalex.notesgb.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class FirestoreProvider(val store: FirebaseFirestore, val auth: FirebaseAuth) : DataProvider {

    companion object {
        private const val USERS_COLLECTION = "NotesGB.users"
        private const val NOTES_COLLECTION = "NotesGB.notes"
    }

    private val notesReference
        get() = currentUser?.let { store.collection(USERS_COLLECTION).document(it.uid).collection(NOTES_COLLECTION) }
                ?: throw NoAuthException()

    private val currentUser
        get() = auth.currentUser

    override suspend fun getCurrentUser(): User? = suspendCoroutine { continuation ->
        continuation.resume(currentUser?.let { User(it.displayName ?: "", it.email ?: "") })
    }

    override fun getNotes(): ReceiveChannel<NoteResult> = Channel<NoteResult>(Channel.CONFLATED).apply {
        var registration: ListenerRegistration? = null

        try {
            registration = notesReference.orderBy("lastChanged", Query.Direction.DESCENDING)
                    .addSnapshotListener { snapshot, error ->
                        val value = error?.let {
                            NoteResult.Error(it)
                        } ?: snapshot?.let {
                            val notes = it.documents.map { it.toObject(Note::class.java) }
                            NoteResult.Success(notes)
                        }
                        value?.let { offer(it) }
                    }
        } catch (error: Throwable) {
            offer(NoteResult.Error(error))
        }

        invokeOnClose { registration?.remove() }
    }

    override suspend fun getNoteById(id: String): Note = suspendCoroutine { continuation ->
        try {
            notesReference.document(id).get()
                    .addOnSuccessListener {
                        continuation.resume(it.toObject(Note::class.java) as Note)
                    }
                    .addOnFailureListener {
                        continuation.resumeWithException(it)
                    }
        } catch (error: Throwable) {
            continuation.resumeWithException(error)
        }
    }

    override suspend fun deleteNote(id: String): Unit = suspendCoroutine { continuation ->
        try {
            notesReference.document(id).get()
                    .addOnSuccessListener {
                        continuation.resume(Unit)
                    }
                    .addOnFailureListener {
                        continuation.resumeWithException(it)
                    }
        } catch (error: Throwable) {
            continuation.resumeWithException(error)
        }
    }

    override suspend fun saveNote(note: Note): Note = suspendCoroutine { continuation ->
        try {
            notesReference.document(note.id).set(note)
                    .addOnSuccessListener {
                        continuation.resume(note)
                    }
                    .addOnFailureListener {
                        continuation.resumeWithException(it)
                    }
        } catch (error: Throwable) {
            continuation.resumeWithException(error)
        }
    }
}
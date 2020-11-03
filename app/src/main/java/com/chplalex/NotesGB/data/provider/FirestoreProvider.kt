package com.chplalex.notesgb.data.provider

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chplalex.notesgb.data.errors.NoAuthException
import com.chplalex.notesgb.data.model.Note
import com.chplalex.notesgb.data.model.NoteResult
import com.chplalex.notesgb.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class FirestoreProvider(val store: FirebaseFirestore, val auth: FirebaseAuth) : DataProvider {

    companion object {
        private const val USERS_COLLECTION = "NotesGB.users"
        private const val NOTES_COLLECTION = "NotesGB.notes"
    }

    private val notesReference
        get() = currentUser?.let { store.collection(USERS_COLLECTION).document(it.uid).collection(NOTES_COLLECTION) } ?: throw NoAuthException()

    private val currentUser
        get() = auth.currentUser

    override fun getCurrentUser(): LiveData<User?> = MutableLiveData<User?>().apply {
        value = currentUser?.let { User(it.displayName ?: "", it.email ?: "") }
    }

    override fun getNotes(): LiveData<NoteResult> = MutableLiveData<NoteResult>().apply {
        try {
            notesReference.addSnapshotListener { snapshot, error ->
                value = error?.let {
                    throw it
                } ?: snapshot?.let {
                    val notes = it.documents.map { it.toObject(Note::class.java) }
                    NoteResult.Success(notes)
                }
            }
        } catch (error: Throwable) {
            value = NoteResult.Error(error)
        }
    }

    override fun getNoteById(id: String): LiveData<NoteResult> = MutableLiveData<NoteResult>().apply {
        try {
            notesReference.document(id).get()
                    .addOnSuccessListener {
                        value = NoteResult.Success(it.toObject(Note::class.java))
                    }
                    .addOnFailureListener {
                        throw it
                    }
        } catch (error: Throwable) {
            value = NoteResult.Error(error)
        }
    }

    override fun deleteNote(id: String): LiveData<NoteResult> = MutableLiveData<NoteResult>().apply {
        notesReference.document(id).delete()
                .addOnSuccessListener { value = NoteResult.Success(null) }
                .addOnFailureListener { value = NoteResult.Error(it) }
    }

    override fun saveNote(note: Note): LiveData<NoteResult> = MutableLiveData<NoteResult>().apply {
        try {
            notesReference.document(note.id).set(note)
                    .addOnSuccessListener {
                        value = NoteResult.Success(note)
                    }
                    .addOnFailureListener {
                        throw it
                    }
        } catch (error: Throwable) {
            value = NoteResult.Error(error)
        }
    }
}
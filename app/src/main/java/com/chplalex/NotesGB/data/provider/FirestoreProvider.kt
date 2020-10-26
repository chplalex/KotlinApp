package com.chplalex.NotesGB.data.provider

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chplalex.NotesGB.data.errors.NoAuthException
import com.chplalex.NotesGB.data.model.Note
import com.chplalex.NotesGB.data.model.NoteResult
import com.chplalex.NotesGB.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

private const val NOTES_COLLECTION = "NotesGB.notes"
private const val USERS_COLLECTION = "NotesGB.users"

class FirestoreProvider : RemoteDataProvider {
    private val db = FirebaseFirestore.getInstance()
    private val currentUser
        get() = FirebaseAuth.getInstance().currentUser

    override fun getNotes(): LiveData<NoteResult> = MutableLiveData<NoteResult>().apply {
        try {
            getUserNotesCollection().addSnapshotListener { snapshot, error ->
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
            getUserNotesCollection().document(id).get()
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

    override fun saveNote(note: Note): LiveData<NoteResult> = MutableLiveData<NoteResult>().apply {
        try {
            getUserNotesCollection().document(note.id).set(note)
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

    override fun getCurrentUser(): LiveData<User?> = MutableLiveData<User?>().apply {
        value = currentUser?.let { User(it.displayName ?: "", it.email ?: "") }
    }

    private fun getUserNotesCollection() = currentUser?.let {
        db.collection(USERS_COLLECTION).document(it.uid).collection(NOTES_COLLECTION)
    } ?: throw NoAuthException()
}
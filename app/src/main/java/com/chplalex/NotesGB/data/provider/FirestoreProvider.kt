package com.chplalex.NotesGB.data.provider

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chplalex.NotesGB.data.model.Note
import com.chplalex.NotesGB.data.model.NoteResult
import com.chplalex.NotesGB.extensions.TAG
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot

private const val NOTES_COLLECTION = "NotesGB.notes"

class FirestoreProvider : RemoteDataProvider {
    private val db = FirebaseFirestore.getInstance()
    private val notesReference = db.collection(NOTES_COLLECTION)

    override fun getNotes(): LiveData<NoteResult> = MutableLiveData<NoteResult>().apply {
        notesReference.addSnapshotListener { snapshot, error ->
            Log.d(TAG, "getNotes(), addSnapshotListener(), error = $error")
            value = error?.let {
                NoteResult.Error(it)
            } ?: snapshot?.let {
                val notes = it.documents.map { it.toObject(Note::class.java) }
                Log.d(TAG,"notes.size = ${notes.size}")
                NoteResult.Success(notes)
            }
        }
    }

    override fun getNoteById(id: String): LiveData<NoteResult> = MutableLiveData<NoteResult>().apply {
        notesReference.document(id)
                .get()
                .addOnSuccessListener {
                    val note = it.toObject(Note::class.java)
                    Log.d(TAG, "getNoteById(), note.title = ${note?.title} note.body = ${note?.body}")
                    value = NoteResult.Success(note)
                }
                .addOnFailureListener {
                    Log.d(TAG, "getNoteById(), error = $it")
                    value = NoteResult.Error(it)
                }
    }

    override fun saveNote(note: Note): LiveData<NoteResult> = MutableLiveData<NoteResult>().apply {
        notesReference.document(note.id)
                .set(note)
                .addOnSuccessListener {
                    Log.d(TAG, "saveNote() success")
                    value = NoteResult.Success(note)
                }
                .addOnFailureListener {
                    Log.d(TAG, "saveNote() error = $it")
                    value = NoteResult.Error(it)
                }
    }
}
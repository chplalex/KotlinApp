package com.example.NotesGB.data.provider

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.NotesGB.data.model.Note
import com.example.NotesGB.data.model.NoteResult
import com.example.NotesGB.extensions.TAG
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot

private const val NOTES_COLLECTION = "NotesGB.notes"

class FirestoreProvider : RemoteDataProvider {
    private val db = FirebaseFirestore.getInstance()
    private val notesReference = db.collection(NOTES_COLLECTION)

    override fun subscribeToAllNotes(): LiveData<NoteResult> {
        val result = MutableLiveData<NoteResult>()

        notesReference.addSnapshotListener { snapshot, error ->
            Log.d(TAG, "addSnapshotListener(), error = $error")

            if (error == null) {

                if (snapshot != null) {
                    val notes = mutableListOf<Note>()
                    for (doc: QueryDocumentSnapshot in snapshot) {
                        val note = doc.toObject(Note::class.java)
                        Log.d(TAG, "subscribeToAllNotes(), note.title = ${note.title} note.body = ${note.body}")
                        notes.add(note)
                    }
                    Log.d(TAG, "notes.size = ${notes.size}")
                    result.value = NoteResult.Success(notes)
                }

            } else {
                result.value = NoteResult.Error(error)
            }
        }

        return result
    }

    override fun getNoteById(id: String): LiveData<NoteResult> {
        val result = MutableLiveData<NoteResult>()

        notesReference.document(id)
                .get()
                .addOnSuccessListener {
                    val note = it.toObject(Note::class.java)
                    Log.d(TAG, "getNoteById(), note.title = ${note?.title} note.body = ${note?.body}")
                    result.value = NoteResult.Success(note)
                }
                .addOnFailureListener {
                    Log.d(TAG, "getNoteById(), error = $it")
                    result.value = NoteResult.Error(it)
                }

        return result
    }

    override fun saveNote(note: Note): LiveData<NoteResult> {
        val result = MutableLiveData<NoteResult>()

        notesReference.document(note.id)
                .set(note)
                .addOnSuccessListener {
                    Log.d(TAG, "saveNote() success")
                    result.value = NoteResult.Success(note)
                }
                .addOnFailureListener {
                    Log.d(TAG, "saveNote() error = $it")
                    result.value = NoteResult.Error(it)
                }

        return result
    }
}
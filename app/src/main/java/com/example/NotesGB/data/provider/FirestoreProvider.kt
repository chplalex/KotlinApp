package com.example.NotesGB.data.provider

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.NotesGB.data.model.Note
import com.example.NotesGB.data.model.NoteResult
import com.example.NotesGB.extensions.TAG
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot

private const val NOTES_COLLECTION = "NotesGB.notes"

class FirestoreProvider : RemoteDataProvider {
    private val db = FirebaseFirestore.getInstance()
    private val notesReference = db.collection(NOTES_COLLECTION)

    override fun subscribeToAllNotes(): LiveData<NoteResult> {
        val result = MutableLiveData<NoteResult>()

        notesReference.addSnapshotListener { value, error ->
            if (error == null) {

                if (value != null) {
                    val notes = mutableListOf<Note>()
                    for (doc: QueryDocumentSnapshot in value) notes.add(doc.toObject(Note::class.java))
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
                .addOnSuccessListener { result.value = NoteResult.Success(it.toObject(Note::class.java)) }
                .addOnFailureListener { result.value = NoteResult.Error(it) }

        return result
    }

    override fun saveNote(note: Note): LiveData<NoteResult> {
        val result = MutableLiveData<NoteResult>()

        notesReference.document(note.id)
                .set(note)
                .addOnSuccessListener { result.value = NoteResult.Success(note) }
                .addOnFailureListener { result.value = NoteResult.Error(it) }

        return result
    }
}
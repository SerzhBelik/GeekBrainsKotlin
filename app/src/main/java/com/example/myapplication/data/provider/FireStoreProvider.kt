package com.example.myapplication.data.provider

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.data.entity.Note
import com.example.myapplication.data.model.NoteResult
import com.github.ajalt.timberkt.Timber
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot

class FireStoreProvider: RemoteDataProvider {

    companion object{
        private const val NOTES_COLECTION = "notes"
    }

    private val store = FirebaseFirestore.getInstance()
    private val notesReference = store.collection(NOTES_COLECTION)

    override fun subscribeToAllNotes(): LiveData<NoteResult> {
        val result = MutableLiveData<NoteResult>()
        notesReference.addSnapshotListener { snapshot, e ->
            if (e != null){
                result.value = NoteResult.Error(e)
            } else if (snapshot != null){
                val notes = mutableListOf<Note>()
                for (doc: QueryDocumentSnapshot in snapshot){
                    notes.add(doc.toObject(Note::class.java))
                }
                result.value = NoteResult.Success(notes)
            }
        }
        return result
    }

    override fun getNoteById(id: String): LiveData<NoteResult> {
        val result = MutableLiveData<NoteResult>()
        notesReference.document(id).get().
                addOnSuccessListener { snapshot ->
                    result.value = NoteResult.Success(snapshot.toObject(Note::class.java))
                }.addOnFailureListener {
            result.value = NoteResult.Error(it)
        }
        return result
    }

    override fun saveNote(note: Note): LiveData<NoteResult> {
        val result = MutableLiveData<NoteResult>()
        notesReference.document(note.id).set(note).addOnSuccessListener {
            Timber.d { "Note $note is saved!" }
            result.value = NoteResult.Success(note)
        }.addOnFailureListener {
            Timber.e(it) { "Failure save note $note with message ${it.message}" }
            result.value = NoteResult.Error(it)
        }
        return result
    }


}
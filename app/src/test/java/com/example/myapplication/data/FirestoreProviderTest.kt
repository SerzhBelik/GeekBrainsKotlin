package com.example.myapplication.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.myapplication.data.entity.Note
import com.example.myapplication.data.errors.NoAuthException
import com.example.myapplication.data.model.NoteResult
import com.example.myapplication.data.provider.FireStoreProvider
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.reset
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FirestoreProviderTest {
    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()
    private val mockAuth = mock<FirebaseAuth>()
    private val mockDb = mock<FirebaseFirestore>()

    private val mockUsersCollection = mock<CollectionReference>()
    private val mockUserDocument = mock<DocumentReference>()
    private val mockResultCollection = mock<CollectionReference>()
    private val mockUser = mock<FirebaseUser>()

    private val mockDocument1 = mock<DocumentSnapshot>()
    private val mockDocument2 = mock<DocumentSnapshot>()
    private val mockDocument3 = mock<DocumentSnapshot>()


    private val testNotes = listOf(
            Note(id = "1"),
            Note(id = "2"),
            Note(id = "3")
    )

    private val provider: FireStoreProvider = FireStoreProvider(mockAuth, mockDb)

    @Before
    fun setup() {
        reset(mockUsersCollection, mockUserDocument, mockResultCollection, mockDocument1, mockDocument2, mockDocument3)
        whenever(mockAuth.currentUser).thenReturn(mockUser)
        whenever(mockUser.uid).thenReturn("")

        whenever(mockDb.collection((any()))).thenReturn(mockUsersCollection)
        whenever(mockUsersCollection.document(any())).thenReturn(mockUserDocument)
        whenever(mockUserDocument.collection(any())).thenReturn(mockResultCollection)

        whenever(mockDocument1.toObject(Note::class.java)).thenReturn(testNotes[0])
        whenever(mockDocument2.toObject(Note::class.java)).thenReturn(testNotes[1])
        whenever(mockDocument3.toObject(Note::class.java)).thenReturn(testNotes[2])
    }

    @Test
    fun `should throw NoAuthException if no auth`() {
        var result: Any? = null
        whenever(mockAuth.currentUser).thenReturn(null)
        provider.subscribeToAllNotes().observeForever {
            result = (it as? NoteResult.Error)?.error
        }
        assertTrue(result is NoAuthException)
    }

    @Test
    fun `subscribeToAllNotese returs notes`() {
        var result: Any? = null
        val mockShapshot = mock<QuerySnapshot>()
        val captor = argumentCaptor<EventListener<QuerySnapshot>>()

        whenever(mockShapshot.documents).thenReturn(listOf(mockDocument1, mockDocument2, mockDocument3))
        whenever(mockResultCollection.addSnapshotListener(captor.capture())).thenReturn(mock())
        provider.subscribeToAllNotes().observeForever {
            result = (it as? NoteResult.Success<List<Note>>)?.data
        }
        captor.firstValue.onEvent(mockShapshot, null)
        assertEquals(testNotes, result)
    }

    @Test
    fun `subscribeToAllNotese returs error`() {
        var result: Throwable? = null
        val testError = mock<FirebaseFirestoreException>()
        val captor = argumentCaptor<EventListener<QuerySnapshot>>()

        whenever(mockResultCollection.addSnapshotListener(captor.capture())).thenReturn(mock())
        provider.subscribeToAllNotes().observeForever {
            result = (it as? NoteResult.Error)?.error
        }
        captor.firstValue.onEvent(null, testError)
        assertEquals(testError, result)
    }

    @Test
    fun `saveNote calls document set`() {
        val mockDocumentReference = mock<DocumentReference>()
        whenever(mockResultCollection.document(testNotes[0].id)).thenReturn(mockDocumentReference)
        provider.saveNote(testNotes[0])
        verify(mockDocumentReference, times(1)).set(testNotes[0])
    }

    @Test
    fun `saveNote returns note`() {
        val mockDocumentReference = mock<DocumentReference>()
        var result: Note? = null
        val captor = argumentCaptor<OnSuccessListener<in Void>>()

        val mockTask = mock<Task<Void>>()
        whenever(mockTask.addOnSuccessListener(captor.capture())).thenReturn(mockTask)
        whenever(mockDocumentReference.set(testNotes[0])).thenReturn(mockTask)
        whenever(mockResultCollection.document(testNotes[0].id)).thenReturn(mockDocumentReference)

        provider.saveNote(testNotes[0]).observeForever {
            result = (it as? NoteResult.Success<Note>)?.data
        }
        captor.firstValue.onSuccess(null)

        assertNotNull(result)
        assertEquals(testNotes[0], result)
    }

    @Test
    fun `deleteNote calls document delete`(){

    }
}
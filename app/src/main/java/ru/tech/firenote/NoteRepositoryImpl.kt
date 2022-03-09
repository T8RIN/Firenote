package ru.tech.firenote

import android.net.Uri
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import ru.tech.firenote.model.ImageUri
import ru.tech.firenote.model.Note
import javax.inject.Inject


@ActivityScoped
class NoteRepositoryImpl @Inject constructor(
    private val database: DatabaseReference,
    private val storage: StorageReference
) : NoteRepository {

    override val auth = Firebase.auth

    private val path get() = Firebase.auth.uid.toString() + "/"
    private val notesChild = "notes/"
    private val imageChild = "image/"

    override suspend fun getNotes(): Flow<Result<List<Note>>> {
        return callbackFlow {
            val postListener = object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    this@callbackFlow.trySendBlocking(Result.failure(error.toException()))
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val items = dataSnapshot.children.map { ds ->
                        ds.getValue(Note::class.java)
                    }
                    this@callbackFlow.trySendBlocking(Result.success(items.filterNotNull()))
                }
            }
            database.child(path).child(notesChild).addValueEventListener(postListener)

            awaitClose {
                database.child(path).child(notesChild).removeEventListener(postListener)
            }
        }
    }

    override suspend fun insertNote(note: Note) {
        if (note.id == null) {
            val id = database.child(path).child(notesChild).push().key
            note.id = id
        }
        database.child(path).child(notesChild + note.id).setValue(note)
    }

    override suspend fun updateNote(note: Note) {
        database.child(path).child(notesChild + note.id).setValue(note)
    }

    override suspend fun deleteNote(note: Note) {
        note.id?.let { database.child(path).child(notesChild + it).removeValue() }
    }

    override suspend fun getProfileUri(): Flow<Result<Uri?>> {
        return callbackFlow {
            val postListener = object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    this@callbackFlow.trySendBlocking(Result.failure(error.toException()))
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val imageUri = dataSnapshot.getValue(ImageUri::class.java)?.uri
                    val uri = if (imageUri == null) null else Uri.parse(imageUri)
                    this@callbackFlow.trySendBlocking(Result.success(uri))
                }
            }
            database.child(path).child(imageChild).addValueEventListener(postListener)

            awaitClose {
                database.child(path).child(imageChild).removeEventListener(postListener)
            }
        }
    }

    override suspend fun setProfileUri(uri: Uri) {
        storage.child(path).child(imageChild).child("profileImage").putFile(uri)
            .addOnSuccessListener { taskSnapshot ->
                taskSnapshot.metadata?.reference?.downloadUrl?.addOnSuccessListener { uri ->
                    database.child(path).child(imageChild).setValue(ImageUri(uri.toString()))
                }
            }
    }

}
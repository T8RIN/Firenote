package ru.tech.firenote

import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import ru.tech.firenote.model.Note
import javax.inject.Inject

@ActivityScoped
class NoteRepositoryImpl @Inject constructor(
    private val database: DatabaseReference
) : NoteRepository {

    private val path get() = Firebase.auth.uid.toString() + "/"

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
            database.child(path).addValueEventListener(postListener)

            awaitClose {
                database.child(path).removeEventListener(postListener)
            }
        }
    }

    override suspend fun insertNote(note: Note) {
        val id = database.child(path).push().key
        note.id = id
        database.child(path + id).setValue(note)
    }

    override suspend fun deleteNote(note: Note) {
        note.id?.let { database.child(path + it).removeValue() }
    }

}
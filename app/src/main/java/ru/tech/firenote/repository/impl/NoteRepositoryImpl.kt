package ru.tech.firenote.repository.impl

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
import ru.tech.firenote.model.Goal
import ru.tech.firenote.model.ImageUri
import ru.tech.firenote.model.Note
import ru.tech.firenote.model.Username
import ru.tech.firenote.repository.NoteRepository
import javax.inject.Inject


@ActivityScoped
class NoteRepositoryImpl @Inject constructor(
    private val database: DatabaseReference,
    private val storage: StorageReference
) : NoteRepository {

    override val auth get() = Firebase.auth

    private val path get() = Firebase.auth.uid.toString() + "/"
    private val notesChild = "notes/"
    private val imageChild = "image/"
    private val usernameChild = "username/"
    private val goalsChild = "goals/"

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

    override suspend fun getUsername(): Flow<Result<String>> {
        return callbackFlow {
            val postListener = object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    this@callbackFlow.trySendBlocking(Result.failure(error.toException()))
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val tempUsername =
                        dataSnapshot.getValue(Username::class.java)?.username
                            ?: (auth.currentUser?.email ?: "").split("@")[0]
                    this@callbackFlow.trySendBlocking(Result.success(tempUsername))
                }
            }
            database.child(path).child(usernameChild).addValueEventListener(postListener)

            awaitClose {
                database.child(path).child(usernameChild).removeEventListener(postListener)
            }
        }
    }

    override suspend fun setUsername(username: String) {
        database.child(path).child(usernameChild).setValue(Username(username))
    }

    override suspend fun getGoals(): Flow<Result<List<Goal>>> {
        return callbackFlow {
            val postListener = object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    this@callbackFlow.trySendBlocking(Result.failure(error.toException()))
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val items = dataSnapshot.children.map { ds ->
                        ds.getValue(Goal::class.java)
                    }
                    this@callbackFlow.trySendBlocking(Result.success(items.filterNotNull()))
                }
            }
            database.child(path).child(goalsChild).addValueEventListener(postListener)

            awaitClose {
                database.child(path).child(goalsChild).removeEventListener(postListener)
            }
        }
    }

    override suspend fun insertGoal(goal: Goal) {
        if (goal.id == null) {
            val id = database.child(path).child(goalsChild).push().key
            goal.id = id
        }
        database.child(path).child(goalsChild + goal.id).setValue(goal)
    }

    override suspend fun deleteGoal(goal: Goal) {
        goal.id?.let { database.child(path).child(goalsChild + it).removeValue() }
    }

}
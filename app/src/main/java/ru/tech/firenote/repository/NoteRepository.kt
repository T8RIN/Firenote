package ru.tech.firenote.repository

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import ru.tech.firenote.model.Note

interface NoteRepository {

    val auth: FirebaseAuth

    suspend fun getNotes(): Flow<Result<List<Note>>>

    suspend fun insertNote(note: Note)

    suspend fun updateNote(note: Note)

    suspend fun deleteNote(note: Note)

    suspend fun getProfileUri(): Flow<Result<Uri?>>

    suspend fun setProfileUri(uri: Uri)
}
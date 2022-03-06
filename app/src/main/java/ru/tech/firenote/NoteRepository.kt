package ru.tech.firenote

import kotlinx.coroutines.flow.Flow
import ru.tech.firenote.model.Note

interface NoteRepository {

    suspend fun getNotes(): Flow<Result<List<Note>>>

    suspend fun insertNote(note: Note)

    suspend fun deleteNote(note: Note)
}
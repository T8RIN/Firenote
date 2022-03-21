package ru.tech.firenote.di

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.tech.firenote.repository.NoteRepository
import ru.tech.firenote.repository.impl.NoteRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseDatabase() = FirebaseDatabase.getInstance().getReference("Users")

    @Provides
    @Singleton
    fun provideFirebaseStorage() = FirebaseStorage.getInstance().getReference("Users")

    @Provides
    @Singleton
    fun provideNoteRepository(
        database: DatabaseReference,
        storage: StorageReference
    ): NoteRepository =
        NoteRepositoryImpl(database, storage)

}
package ru.tech.firenote.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.tech.firenote.NoteRepository
import ru.tech.firenote.NoteRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> =
        context.dataStore

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "fireNotePrefs")

    @Provides
    @Singleton
    fun provideFirebaseDatabase() = FirebaseDatabase.getInstance().getReference("Users")

    @Provides
    @Singleton
    fun provideNoteRepository(database: DatabaseReference): NoteRepository =
        NoteRepositoryImpl(database)

}
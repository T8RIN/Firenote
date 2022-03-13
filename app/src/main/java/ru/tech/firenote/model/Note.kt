package ru.tech.firenote.model

import androidx.annotation.Keep
import com.google.firebase.database.IgnoreExtraProperties

@Keep
@IgnoreExtraProperties
data class Note(
    val title: String? = null,
    val content: String? = null,
    val timestamp: Long? = null,
    val color: Int? = null,
    val appBarColor: Int? = null,
    var id: String? = null
)
package ru.tech.firenote.model

import androidx.annotation.Keep
import com.google.firebase.database.IgnoreExtraProperties

@Keep
@IgnoreExtraProperties
data class Type(
    val color: Int? = null,
    val type: String? = null
)
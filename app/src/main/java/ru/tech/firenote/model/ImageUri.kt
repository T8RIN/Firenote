package ru.tech.firenote.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class ImageUri(
    val uri: String? = null
)
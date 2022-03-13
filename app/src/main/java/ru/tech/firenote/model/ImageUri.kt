package ru.tech.firenote.model

import androidx.annotation.Keep
import com.google.firebase.database.IgnoreExtraProperties

@Keep
@IgnoreExtraProperties
data class ImageUri(val uri: String? = null)
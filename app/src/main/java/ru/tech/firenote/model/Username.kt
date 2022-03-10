package ru.tech.firenote.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Username(val username: String? = null)
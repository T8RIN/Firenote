package ru.tech.firenote.model

import androidx.annotation.Keep
import com.google.firebase.database.IgnoreExtraProperties

@Keep
@IgnoreExtraProperties
data class Goal(
    val title: String? = null,
    val content: List<GoalData>? = null,
    val timestamp: Long? = null,
    val color: Int? = null,
    val appBarColor: Int? = null,
    var id: String? = null
)
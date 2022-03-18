package ru.tech.firenote.model

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.Keep
import com.google.firebase.database.IgnoreExtraProperties

@Keep
@IgnoreExtraProperties
data class GoalData(
    val content: String? = null,
    val done: Boolean? = null
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(content)
        parcel.writeValue(done)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GoalData> {
        override fun createFromParcel(parcel: Parcel): GoalData {
            return GoalData(parcel)
        }

        override fun newArray(size: Int): Array<GoalData?> {
            return arrayOfNulls(size)
        }
    }

}

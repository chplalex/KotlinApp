package com.chplalex.notesgb.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.item_note.*
import java.util.*

@Parcelize
data class Note(
        val id : String = "",
        val title: String = "",
        val body: String = "",
        val color: Color = Color.WHITE,
        val lastChanged: Date = Date()) : Parcelable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        return id == (other as Note).id
    }

    override fun hashCode(): Int = id.hashCode()
}

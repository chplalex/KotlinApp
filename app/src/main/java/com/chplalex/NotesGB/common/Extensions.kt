package com.chplalex.notesgb.common

import android.content.Context
import android.view.View
import androidx.core.content.ContextCompat
import com.chplalex.notesgb.R
import com.chplalex.notesgb.data.model.Color
import com.chplalex.notesgb.data.model.Color.*
import java.text.SimpleDateFormat
import java.util.*

const val DATE_TIME_FORMAT = "dd.MMM.yy HH:mm"
const val TAG = "NotesGB_debug"

fun Date.format(): String = SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault()).format(this)

fun Color.getColorInt(context: Context): Int = ContextCompat.getColor(context, getColorRes())

fun Color.getColorRes() = when (this) {
    WHITE -> R.color.color_white
    VIOLET -> R.color.color_violet
    YELLOW -> R.color.color_yellow
    RED -> R.color.color_red
    PINK -> R.color.color_pink
    GREEN -> R.color.color_green
    BLUE -> R.color.color_blue
}

fun Context.dip(value: Int): Int = (value * resources.displayMetrics.density).toInt()
fun Context.dip(value: Float): Int = (value * resources.displayMetrics.density).toInt()

fun View.dip(value: Int): Int = context.dip(value)
fun View.dip(value: Float): Int = context.dip(value)


package com.chplalex.NotesGB.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.chplalex.NotesGB.R
import com.chplalex.NotesGB.data.model.Color
import com.chplalex.NotesGB.data.model.Color.*
import com.chplalex.NotesGB.data.model.Note

class MainAdapter(private val onClickListener: ((Note) -> Unit)? = null) :
        RecyclerView.Adapter<MainAdapter.NoteViewHolder>() {

    var notes: List<Note> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = NoteViewHolder(
            LayoutInflater.from(parent.context).inflate(
                    R.layout.item_note,
                    parent,
                    false))

    override fun getItemCount() = notes.size

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) = holder.bind(notes[position])

    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val txtTitle = itemView.findViewById<TextView>(R.id.txtTitle)
        private val txtBody = itemView.findViewById<TextView>(R.id.txtBody)

        fun bind(note: Note) {
            with(note) {
                txtTitle.text = title
                txtBody.text = body
                @Suppress("DEPRECATION") // minSDK for this app is 21
                itemView.setBackgroundColor(itemView.context.resources.getColor(colorId(color)))
            }
            itemView.setOnClickListener { onClickListener?.invoke(note) }
        }
    }
}

private fun colorId(color: Color) = when (color) {
    WHITE -> R.color.color_white
    YELLOW -> R.color.color_yellow
    GREEN -> R.color.color_green
    BLUE -> R.color.color_blue
    RED -> R.color.color_red
    VIOLET -> R.color.color_violet
    PINK -> R.color.color_pink
}



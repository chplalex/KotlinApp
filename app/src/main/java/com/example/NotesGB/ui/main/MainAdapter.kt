package com.example.NotesGB.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.NotesGB.R
import com.example.NotesGB.data.model.Note

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
        private val viewTitle = itemView.findViewById<TextView>(R.id.title)
        private val viewBody = itemView.findViewById<TextView>(R.id.body)

        fun bind(note: Note) {
            with(note) {
                viewTitle.text = title
                viewBody.text = body
                @Suppress("DEPRECATION") // minSDK for this app is 21
                itemView.setBackgroundColor(itemView.context.resources.getColor(color.id))
            }
            itemView.setOnClickListener { onClickListener?.invoke(note) }
        }
    }

}



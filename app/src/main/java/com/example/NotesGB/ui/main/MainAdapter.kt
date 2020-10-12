package com.example.NotesGB.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.NotesGB.R
import com.example.NotesGB.data.model.Note

class MainAdapter(private val onItemClickListener: OnItemClickListener) :
        RecyclerView.Adapter<MainAdapter.NoteViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(note: Note)
    }

    var notes: List<Note> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_note, parent, false)
        return NoteViewHolder(view)
    }

    override fun getItemCount() = notes.size

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(notes[position])
    }

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
            itemView.setOnClickListener { onItemClickListener.onItemClick(note) }
        }
    }

}



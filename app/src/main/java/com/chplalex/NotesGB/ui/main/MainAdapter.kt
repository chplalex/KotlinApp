package com.chplalex.notesgb.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chplalex.notesgb.R
import com.chplalex.notesgb.common.getColorInt
import com.chplalex.notesgb.data.model.Note
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_note.*

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

    inner class NoteViewHolder(override val containerView: View) :
            RecyclerView.ViewHolder(containerView),
            LayoutContainer {

        fun bind(note: Note) = with(note) {
            txtTitle.text = title
            txtBody.text = body
            itemView.setBackgroundColor(color.getColorInt(containerView.context))
            itemView.setOnClickListener { onClickListener?.invoke(this) }
        }
    }
}
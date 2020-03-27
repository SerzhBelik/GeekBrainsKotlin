package com.example.myapplication.ui.main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat


import com.example.myapplication.R
import com.example.myapplication.data.entity.Note
import kotlinx.android.synthetic.main.item_note.view.*

import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.common.getColorInt

class NotesRVAdapter(val onItemClick: ((Note)-> Unit)? = null) : RecyclerView.Adapter<NotesRVAdapter.ViewHolder>() {

    var notes: List<Note> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                    R.layout.item_note,
                    parent,
                    false
            )
    )

    override fun getItemCount() = notes.size

    override fun onBindViewHolder(vh: ViewHolder, pos: Int) = vh.bind(notes[pos])

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(note: Note) = with(itemView) {
            tv_title.text = note.title
            tv_text.text = note.text

            itemView.setBackgroundColor(note.color.getColorInt(context))
            itemView.setOnClickListener {
                onItemClick?.invoke(note)
            }
        }

    }

}
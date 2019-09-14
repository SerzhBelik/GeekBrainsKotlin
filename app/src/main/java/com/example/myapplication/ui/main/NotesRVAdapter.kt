package com.example.myapplication.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


import com.example.myapplication.R
import com.example.myapplication.data.entity.Note
import kotlinx.android.synthetic.main.item_note.view.*

import androidx.recyclerview.widget.RecyclerView

class NotesRVAdapter : RecyclerView.Adapter<NotesRVAdapter.ViewHolder>() {

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


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(note: Note) = with(note) {
            itemView.tv_title.text = title
            itemView.tv_text.text = text
            itemView.setBackgroundColor(note.color)
        }

    }

}
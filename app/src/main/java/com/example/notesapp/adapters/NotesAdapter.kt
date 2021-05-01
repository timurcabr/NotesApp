package com.example.notesapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp.R
import com.example.notesapp.models.Note

class NotesAdapter(
    var notes: List<Note>,
    var listener: OnItemClickListener
) : RecyclerView.Adapter<NotesAdapter.NotesHolder>(){

    inner class NotesHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener{
        val noteTitle: TextView = itemView.findViewById(R.id.note_title)
        val noteDesc: TextView = itemView.findViewById(R.id.note_desc)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            listener.onItemClick(adapterPosition)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false)
        return NotesHolder(view)
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    override fun onBindViewHolder(holder: NotesHolder, position: Int) {
        holder.itemView.apply {
            holder.noteTitle.text = notes[position].title
            holder.noteDesc.text = notes[position].description
        }
    }

    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }

}
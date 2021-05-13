package com.example.notesapp.adapters

import android.content.Context
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp.R
import com.example.notesapp.models.Note

class NotesAdapter(
    var context: Context,
    var notes: List<Note>,
    var listener: OnItemClickListener
) : RecyclerView.Adapter<NotesAdapter.NotesHolder>() {

    inner class NotesHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener, View.OnCreateContextMenuListener {
        val noteTitle: TextView = itemView.findViewById(R.id.note_title)
        val noteDesc: TextView = itemView.findViewById(R.id.note_desc)
        private val noteDelete: ImageView = itemView.findViewById(R.id.note_delete)
        private val noteEdit: ImageView = itemView.findViewById(R.id.note_edit)
        private val parent: CardView = itemView.findViewById(R.id.parent)

        init {
            parent.setOnClickListener(this)
            noteDelete.setOnClickListener(this)
            noteEdit.setOnClickListener(this)
            itemView.setOnCreateContextMenuListener(this)
            itemView.setOnLongClickListener{
                it.showContextMenu()
            }
        }

        override fun onClick(v: View?) {
            when (v?.id) {
                R.id.parent -> listener.onItemClick(adapterPosition)
                R.id.note_delete -> Toast.makeText(context, "This function currently is in context menu", Toast.LENGTH_SHORT).show()
                R.id.note_edit -> Toast.makeText(context, "This function currently is in context menu", Toast.LENGTH_SHORT).show()
            }
        }

        override fun onCreateContextMenu(
            menu: ContextMenu?,
            v: View?,
            menuInfo: ContextMenu.ContextMenuInfo?
        ) {
            menu?.setHeaderTitle("Manipulate with note")
            menu?.add(Menu.NONE, R.id.context_edit,1,"Edit")?.setOnMenuItemClickListener(onClickMenu)
            menu?.add(Menu.NONE, R.id.context_delete,2,"Delete")?.setOnMenuItemClickListener(onClickMenu)
        }

        private val onClickMenu: MenuItem.OnMenuItemClickListener =
            MenuItem.OnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.context_edit -> {
                        listener.onEditClick(adapterPosition)
                    }
                    R.id.context_delete -> {
                        listener.onDeleteClick(adapterPosition)
                    }
                }
                true
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

    interface OnItemClickListener {
        fun onItemClick(position: Int)
        fun onDeleteClick(position: Int)
        fun onEditClick(position: Int)
    }
}
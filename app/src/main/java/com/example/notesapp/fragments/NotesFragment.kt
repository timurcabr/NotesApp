package com.example.notesapp.fragments

import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp.models.Note
import com.example.notesapp.adapters.NotesAdapter
import com.example.notesapp.R
import java.util.*

class NotesFragment : Fragment(), NotesAdapter.OnItemClickListener {

    private val userNotes = mutableListOf<Note>()

    companion object{
        const val KEY = "object"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_notes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userNotes.clear()

        userNotes.add(Note("First Title", getString(R.string.note_desc), Date()))
        userNotes.add(Note("Second Title", getString(R.string.note_desc), Date()))
        userNotes.add(Note("Third Title", getString(R.string.note_desc), Date()))
        userNotes.add(Note("Fourth Title", getString(R.string.note_desc), Date()))
        userNotes.add(Note("Fifth Title", getString(R.string.note_desc), Date()))

        val recyclerView = view.findViewById<RecyclerView>(R.id.notesList)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)

        val adapter = NotesAdapter(userNotes, this)
        recyclerView.adapter = adapter

    }

    override fun onItemClick(position: Int) {

        val bundle = Bundle()
        bundle.putSerializable(KEY, userNotes[position])

        val fragment = DetailsFragment()
        fragment.arguments = bundle

        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
            parentFragmentManager.beginTransaction()
                .replace(R.id.details_fragment, fragment)
                .addToBackStack(null)
                .commit()
        }else {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }
    }
}
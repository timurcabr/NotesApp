package com.example.notesapp.fragments

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp.models.Note
import com.example.notesapp.adapters.NotesAdapter
import com.example.notesapp.R
import com.example.notesapp.Utils
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import java.util.*

class NotesFragment : Fragment(), NotesAdapter.OnItemClickListener {

    private val userNotes = mutableListOf<Note>()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NotesAdapter
    private val gson = Gson()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_notes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userNotes.clear()

        val preferences = context?.getSharedPreferences(Utils.SHARED_DB_NAME, Context.MODE_PRIVATE)
        if (preferences?.getString(Utils.DATA_LIST, null) != null) {
            userNotes.addAll(
                gson.fromJson(
                    preferences.getString(Utils.DATA_LIST, null),
                    Array<Note>::class.java
                )
            )
            Log.i("MyData", userNotes.size.toString())
        }

        recyclerView = view.findViewById(R.id.notesList)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)

        adapter = NotesAdapter(userNotes, this)
        recyclerView.adapter = adapter
        registerForContextMenu(recyclerView)
        setSharedPreferences(userNotes)
    }

    override fun onItemClick(position: Int) {

        val bundle = Bundle()
        bundle.putSerializable(Utils.KEY, userNotes[position])

        val fragment = DetailsFragment()
        fragment.arguments = bundle

        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            parentFragmentManager.beginTransaction()
                .replace(R.id.details_fragment, fragment)
                .addToBackStack(null)
                .commit()
        } else {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onDeleteClick(position: Int) {
        Toast.makeText(context, "Delete click", Toast.LENGTH_SHORT).show()
        userNotes.remove(userNotes[position])
        setSharedPreferences(userNotes)
        adapter.notifyDataSetChanged()
    }

    override fun onEditClick(position: Int) {
        val bundle = Bundle()
        bundle.putSerializable(Utils.KEY, userNotes[position])

        val fragment = EditFragment()
        fragment.arguments = bundle

        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun setSharedPreferences(userNotes: MutableList<Note>) {
        val sharedPreference =
            context?.getSharedPreferences(Utils.SHARED_DB_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreference?.edit()
        val userNotesString = gson.toJson(userNotes)
        editor?.putString(Utils.DATA_LIST, userNotesString)
        editor?.apply()
    }

    override fun onDestroy() {
        super.onDestroy()
        setSharedPreferences(userNotes)
    }
}
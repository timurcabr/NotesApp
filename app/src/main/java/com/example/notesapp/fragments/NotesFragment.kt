package com.example.notesapp.fragments

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp.R
import com.example.notesapp.Utils
import com.example.notesapp.adapters.NotesAdapter
import com.example.notesapp.models.Note
import com.google.gson.Gson
import java.util.*

class NotesFragment : Fragment(), NotesAdapter.OnItemClickListener {

    private var userNotes = mutableListOf<Note>()
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: NotesAdapter
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
        updateList()

        checkListSize(view)

        recyclerView = view.findViewById(R.id.notesList)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)

        adapter = context?.let { NotesAdapter(it, userNotes, this) }!!
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
        view?.let { checkListSize(it) }
    }

    override fun onDeleteClick(position: Int) {
        Toast.makeText(context, "Delete click", Toast.LENGTH_SHORT).show()
        userNotes.remove(userNotes[position])
        setSharedPreferences(userNotes)
        adapter.notifyDataSetChanged()
        view?.let { checkListSize(it) }
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

    fun updateList(){
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
    }

    private fun setSharedPreferences(userNotes: MutableList<Note>) {
        val sharedPreference =
            context?.getSharedPreferences(Utils.SHARED_DB_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreference?.edit()
        val userNotesString = gson.toJson(userNotes)
        editor?.putString(Utils.DATA_LIST, userNotesString)
        editor?.apply()
    }

    private fun checkListSize(view: View) {
        if (userNotes.size == 0) {
            view.findViewById<TextView>(R.id.message).visibility = View.VISIBLE
        } else {
            view.findViewById<TextView>(R.id.message).visibility = View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        setSharedPreferences(userNotes)
    }
}
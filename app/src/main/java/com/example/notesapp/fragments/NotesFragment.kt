package com.example.notesapp.fragments

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp.R
import com.example.notesapp.Utils
import com.example.notesapp.adapters.NotesAdapter
import com.example.notesapp.models.Note
import com.google.firebase.firestore.FirebaseFirestore

class NotesFragment : Fragment(), NotesAdapter.OnItemClickListener {

    lateinit var recyclerView: RecyclerView
    lateinit var adapter: NotesAdapter
    lateinit var progressBar: ProgressBar
    private var userNotes = mutableListOf<Note>()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_notes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBar = view.findViewById(R.id.progressBar)

        retrieveDataFromFirestore()
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
        adapter.notifyItemRemoved(position)
        val note = userNotes[position]
        Toast.makeText(context, note.id, Toast.LENGTH_SHORT).show()
        db.collection("notes")
            .document(note.id)
            .delete()
            .addOnSuccessListener { Log.d("MyData", "Note successfully deleted!") }
            .addOnFailureListener { e -> Log.w("MyData", "Error deleting document", e) }
        retrieveDataFromFirestore()
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

    private fun handleRecyclerView(userNotes: MutableList<Note>) {
        Log.i("MyData handle", userNotes.toString())
        recyclerView = requireView().findViewById(R.id.notesList)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)

        adapter = context?.let { NotesAdapter(it, userNotes, this) }!!
        recyclerView.adapter = adapter
        registerForContextMenu(recyclerView)
    }

    private fun retrieveDataFromFirestore() {
        var title: String
        var description: String
        var date: String
        var id: String
        val list = mutableListOf<Note>()
        db.collection("notes")
            .get()
            .addOnCompleteListener {

                if (it.isSuccessful) {
                    for (document in it.result!!) {
                        title = document.data["title"] as String
                        description = document.data["description"] as String
                        date = document.data["publicDate"] as String
                        id = document.data["id"] as String
                        val temp = Note(title, description, date, id)
                        list.add(temp)
                    }
                    userNotes.clear()
                    userNotes.addAll(list)
                    handleRecyclerView(list)
                    progressBar.visibility = View.GONE
                }
            }
            .addOnFailureListener { exception ->
                Log.i("MyData", exception.toString())
            }
    }
}
package com.example.notesapp.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.notesapp.R
import com.example.notesapp.Utils
import com.example.notesapp.models.Note
import java.text.SimpleDateFormat

class DetailsFragment : Fragment() {

    private lateinit var noteTitle: TextView
    private lateinit var noteDesc: TextView
    private lateinit var noteDate: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    @SuppressLint("SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        noteTitle = view.findViewById(R.id.noteTitle)
        noteDesc = view.findViewById(R.id.noteDesc)
        noteDate = view.findViewById(R.id.noteDate)

        val sdf = SimpleDateFormat("dd/MM/yyyy hh:mm:ss")

        val bundle = this.arguments
        val note = bundle?.getSerializable(Utils.KEY) as Note
        noteTitle.text = note.title
        noteDesc.text = note.description
        noteDate.text = sdf.format(note.publicDate).toString()
    }
}

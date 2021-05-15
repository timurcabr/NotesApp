package com.example.notesapp.fragments

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.notesapp.R
import com.example.notesapp.Utils
import com.example.notesapp.models.Note
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class EditFragment : Fragment() {

    private lateinit var noteTitle: TextInputEditText
    private lateinit var noteDesc: TextInputEditText
    private lateinit var noteDateView: TextView
    private lateinit var selectDate: Button
    private lateinit var selectedDateView: TextView
    private lateinit var saveNote: Button
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit, container, false)
    }

    @SuppressLint("CutPasteId")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        noteTitle = view.findViewById(R.id.noteTitle_editText)
        noteDesc = view.findViewById(R.id.noteDesc_editText)
        noteDateView = view.findViewById(R.id.selectedDate_edit)
        selectDate = view.findViewById(R.id.selectDate_edit)
        selectedDateView = view.findViewById(R.id.selectedDate_edit)
        saveNote = view.findViewById(R.id.saveNote)

        selectDate.setOnClickListener {
            val myCalendar = Calendar.getInstance()
            val year = myCalendar.get(Calendar.YEAR)
            val month = myCalendar.get(Calendar.MONTH)
            val day = myCalendar.get(Calendar.DAY_OF_MONTH)

            context?.let {
                DatePickerDialog(
                    it,
                    DatePickerDialog.OnDateSetListener { view, selectedYear, selectedMonth, selectedDay ->
                        val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                        selectedDateView.text = (selectedDate)
                    },
                    year,
                    month,
                    day
                ).show()
            }
        }

        val bundle = this.arguments
        val noteArg = bundle?.getSerializable(Utils.KEY) as Note
        noteTitle.setText(noteArg.title)
        noteDesc.setText(noteArg.description)
        noteDateView.text = noteArg.publicDate

        saveNote.setOnClickListener {
            val noteTitleView = noteTitle.text.toString()
            val noteDescView = noteDesc.text.toString()
            Log.i("MyData", "$noteTitleView  $noteDescView")
            val note = Note(noteTitleView, noteDescView, "Timestamp.now()", noteArg.id)
            updateNote(convertObjectToMap(note), note.id)
            setFragment(NotesFragment())
        }
    }

    private fun updateNote(note: Map<String, String>, id: String) {
        db.collection("notes")
            .document(id)
            .update(note)
            .addOnSuccessListener { Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show() }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Error $e", Toast.LENGTH_SHORT).show()
            }
    }

    private fun convertObjectToMap(note: Note): Map<String, String> {
        return mapOf(
            "title" to note.title,
            "description" to note.description,
            "publicDate" to note.publicDate,
            "id" to note.id
        )
    }

    private fun setFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, fragment)
            commit()
        }
    }

}
package com.example.notesapp.fragments

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.notesapp.R
import com.example.notesapp.Utils
import com.example.notesapp.models.Note
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.*

class EditFragment : Fragment() {

    private lateinit var noteTitle: TextInputEditText
    private lateinit var noteDesc: TextInputEditText
    private lateinit var noteDateView: TextView
    private lateinit var selectDate: Button
    private lateinit var selectedDateView: TextView
    private lateinit var saveNote: Button
    private lateinit var noteDate: Date
    private val gson = Gson()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit, container, false)
    }

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
                        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
                        noteDate = sdf.parse(selectedDate) as Date
                    },
                    year,
                    month,
                    day
                ).show()
            }
        }

        val sdf = SimpleDateFormat("dd/MM/yyyy hh:mm:ss")
        val bundle = this.arguments
        val noteArg = bundle?.getSerializable(Utils.KEY) as Note
        noteTitle.setText(noteArg.title)
        noteDesc.setText(noteArg.description)
        noteDateView.text = sdf.format(noteArg.publicDate).toString()

        saveNote.setOnClickListener {
            val noteTitleView = noteTitle.text.toString()
            val noteDescView = noteDesc.text.toString()
            val noteDate = Date()
            Log.i("MyData", "$noteTitleView  $noteDescView")
            val note = Note(noteTitleView, noteDescView, noteDate)
            val sharedPreferences =
                context?.getSharedPreferences(Utils.SHARED_DB_NAME, Context.MODE_PRIVATE)
            if (sharedPreferences?.getString(Utils.DATA_LIST, null) != null) {
                val listType = object : TypeToken<MutableList<Note>>() {}.type
                val json = sharedPreferences.getString(Utils.DATA_LIST, null)
                val userNotes: MutableList<Note> = gson.fromJson(json, listType)
                userNotes.remove(noteArg)
                userNotes.add(note)
                setSharedPreferences(userNotes)
                setFragment(NotesFragment())
            }
        }
    }

    private fun setFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, fragment)
            commit()
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
}
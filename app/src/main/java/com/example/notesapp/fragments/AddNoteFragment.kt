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
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.notesapp.R
import com.example.notesapp.Utils
import com.example.notesapp.models.Note
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AddNoteFragment : DialogFragment() {

    private lateinit var selectDate: Button
    private lateinit var addNote: Button
    private lateinit var selectedDateView: TextView
    private lateinit var noteTitle: TextInputLayout
    private lateinit var noteDesc: TextInputLayout
    private lateinit var noteDate: Date
    private val gson = Gson()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_note, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        selectDate = view.findViewById(R.id.selectDate)
        addNote = view.findViewById(R.id.addNote)
        selectedDateView = view.findViewById(R.id.selectedDate)
        noteTitle = view.findViewById(R.id.noteTitle_add)
        noteDesc = view.findViewById(R.id.noteDesc_add)

        addNote.setOnClickListener{
            addNote()
        }

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
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.90).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.50).toInt()
        dialog!!.window?.setLayout(width, height)
    }

    private fun addNote() {
        val noteTitleView = noteTitle.editText?.text.toString()
        val noteDescView = noteDesc.editText?.text.toString()
        if (noteTitleView.isNotEmpty() && noteDescView.isNotEmpty()) {
            val note = Note(noteTitleView, noteDescView, noteDate)
            val sharedPreferences =
                context?.getSharedPreferences(Utils.SHARED_DB_NAME, Context.MODE_PRIVATE)
            if (sharedPreferences?.getString(Utils.DATA_LIST, null) != null) {
                val listType = object : TypeToken<MutableList<Note>>() {}.type
                val json = sharedPreferences.getString(Utils.DATA_LIST, null)
                val userNotes: MutableList<Note> = gson.fromJson(json, listType)
                userNotes.add(note)
                setSharedPreferences(userNotes)
                dismiss()
            }
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
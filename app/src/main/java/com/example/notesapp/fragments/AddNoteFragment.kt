package com.example.notesapp.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.notesapp.R
import java.text.SimpleDateFormat
import java.util.*

class AddNoteFragment : DialogFragment(), View.OnClickListener {

    lateinit var selectDate: Button
    lateinit var addNote: Button
    lateinit var selectedDateView: TextView

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

        selectDate.setOnClickListener { view ->
            clickDatePicker(view)
        }
        addNote.setOnClickListener(this)

    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.90).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.50).toInt()
        dialog!!.window?.setLayout(width, height)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.addNote -> Toast.makeText(context, "Note is being added", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun clickDatePicker(view: View) {
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


}
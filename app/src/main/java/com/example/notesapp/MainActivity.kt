package com.example.notesapp

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.notesapp.fragments.AboutFragment
import com.example.notesapp.fragments.NotesFragment
import com.example.notesapp.models.Note
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var toggle: ActionBarDrawerToggle
    lateinit var drawerLayout: DrawerLayout
    lateinit var navigationView: NavigationView
    private val gson = Gson()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawerLayout = findViewById(R.id.drawerLayout)
        navigationView = findViewById(R.id.navigationMenu)
        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.main -> setFragment(NotesFragment())
                R.id.about -> setFragment(AboutFragment())
            }
            drawerLayout.closeDrawers()
            true
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val view = navigationView.getHeaderView(0)
        val signInButton = view.findViewById<SignInButton>(R.id.signInWithGoogle)
        signInButton.setOnClickListener {
            //TODO Google Sign in
        }
        fragmentOrientation()

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            openAddDialog()
        }
    }

    @SuppressLint("InflateParams")
    private fun openAddDialog() {
        val dialog = AlertDialog.Builder(this)
            .setTitle("Add Note")
            .setMessage("Please fill in the blanks to create a new note!")
            .setIcon(R.drawable.ic_baseline_note_add_24)

        val view = LayoutInflater.from(this).inflate(R.layout.dialog_add_note, null)
        dialog.setView(view)

        val noteTitle = view.findViewById<TextInputLayout>(R.id.noteTitle_add)
        val noteDesc = view.findViewById<TextInputLayout>(R.id.noteDesc_add)
        val selectedDateView = view.findViewById<TextView>(R.id.selectedDate)
        var selectedDate = ""

        view.findViewById<Button>(R.id.selectDate_add).setOnClickListener {
            val myCalendar = Calendar.getInstance()
            val year = myCalendar.get(Calendar.YEAR)
            val month = myCalendar.get(Calendar.MONTH)
            val day = myCalendar.get(Calendar.DAY_OF_MONTH)

            this.let {
                DatePickerDialog(
                    it,
                    { _, selectedYear, selectedMonth, selectedDay ->
                        selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                        selectedDateView.text = (selectedDate)
                    },
                    year,
                    month,
                    day
                ).show()
            }
        }
        dialog.setPositiveButton("Add Note") { _, _ ->
            if (noteTitle.editText?.text.toString().isNotEmpty()
                && noteDesc.editText?.text.toString().isNotEmpty()
                && selectedDate != ""
            ) {
                val id = "note${UUID.randomUUID()}"

                val note = Note(
                    noteTitle.editText?.text.toString(),
                    noteDesc.editText?.text.toString(),
                    "Timestamp.now()",
                    id
                )

                val map = mutableMapOf<String, Any>()

                map["title"] = note.title
                map["description"] = note.description
                map["publicDate"] = note.publicDate
                map["id"] = note.id

                db.collection("notes")
                    .document(id)
                    .set(map)
                    .addOnSuccessListener {
                        Log.i("MyData", "Data was successfully saved")
                    }
                    .addOnFailureListener { e ->
                        Log.i("MyData", "Loading failed: $e")
                    }
            }
            setFragment(NotesFragment())
        }

        dialog.setNegativeButton("Cancel") { _, _ ->

        }
        dialog.show()
    }


    private fun setFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun fragmentOrientation() {
        val fragmentManager = supportFragmentManager
        val fragment = fragmentManager.findFragmentById(R.id.fragment_container)
        if (fragment == null) {
            fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, NotesFragment())
                .commit()
        }

        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, NotesFragment())
                .commit()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        if (item.itemId == R.id.doneNotes) {
            Toast.makeText(this, "Here we will show done notes", Toast.LENGTH_SHORT).show()
        }
        return super.onOptionsItemSelected(item)
    }
}


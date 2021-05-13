package com.example.notesapp

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.notesapp.fragments.AboutFragment
import com.example.notesapp.fragments.AddNoteFragment
import com.example.notesapp.fragments.NotesFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    lateinit var toggle: ActionBarDrawerToggle
    lateinit var drawerLayout: DrawerLayout
    lateinit var navigationView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        1. Создайте список ваших заметок.
//        2. Создайте карточку для элемента списка.
//        3. Класс данных, созданный на шестом уроке, используйте для заполнения карточки списка.
//        4. * Создайте фрагмент для редактирования данных в конкретной карточке. Этот фрагмент пока можно вызвать через основное меню.

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
        fragmentOrientation()

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            AddNoteFragment().show(supportFragmentManager, "Add Note Fragment")
        }
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
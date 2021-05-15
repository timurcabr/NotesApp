package com.example.notesapp.models

import com.google.firebase.Timestamp
import java.io.Serializable
import java.util.*

data class Note(var title: String, var description: String, var publicDate: String, var id: String) : Serializable
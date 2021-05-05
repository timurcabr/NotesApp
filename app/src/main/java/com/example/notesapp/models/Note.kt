package com.example.notesapp.models

import java.io.Serializable
import java.util.*

data class Note( var title: String, var description: String, var publicDate: Date) : Serializable
package com.example.tugastodolist

import java.util.Date

data class Todo(val text: String, var isDone: Boolean = false, val deadline: Date? = null)
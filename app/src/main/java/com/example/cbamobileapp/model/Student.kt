package com.example.cbamobileapp.model

data class Student(
    val name: String,
    val subjects: List<Subject>
) {
    fun average(): Double{
        if (subjects.isEmpty()) {
            return 0.0
        }
        return subjects
            .map { it.mark }
            .average()
    }
}

package com.example.cbamobileapp.model

data class Subject(
    val name: String,
    val mark: Int,
    val description: String? = null
) {
    fun grade(): Grade {
        return when {
            mark >= 80 -> Grade.HD
            mark >= 70 -> Grade.D
            mark >= 60 -> Grade.C
            mark >= 50 -> Grade.P
            else -> Grade.F
        }
    }
}

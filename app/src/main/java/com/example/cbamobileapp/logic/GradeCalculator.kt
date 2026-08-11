package com.example.cbamobileapp.logic
import com.example.cbamobileapp.model.Grade

interface GradeCalculator {
    fun calculate(mark: Int): Grade
}

class DefaultGradeCalculator : GradeCalculator {
    override fun calculate(mark: Int): Grade {
        return when {
            mark >= 80 -> Grade.HD
            mark >= 70 -> Grade.D
            mark >= 60 -> Grade.C
            mark >= 50 -> Grade.P
            else -> Grade.F
        }
    }
}
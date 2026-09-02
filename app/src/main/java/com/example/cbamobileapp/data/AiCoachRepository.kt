package com.example.cbamobileapp.data

import com.example.cbamobileapp.model.ProductivityTask

interface AiCoachRepository {

    suspend fun askCoach(
        question: String,
        tasks: List<ProductivityTask>
    ): String
}
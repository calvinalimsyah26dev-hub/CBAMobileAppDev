package com.example.cbamobileapp.data

import com.example.cbamobileapp.model.ProductivityTask
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend
import javax.inject.Inject

class FirebaseAiCoachRepository @Inject constructor() :
    AiCoachRepository {

    private val model =
        Firebase
            .ai(
                backend =
                    GenerativeBackend.googleAI()
            )
            .generativeModel(
                modelName = "gemini-3.7-flash"
            )

    override suspend fun askCoach(
        question: String,
        tasks: List<ProductivityTask>
    ): String {
        val taskSummary =
            if (tasks.isEmpty()) {
                "The user currently has no active tasks."
            } else {
                tasks.joinToString(
                    separator = "\n"
                ) { task ->
                    """
                    - Title: ${task.title}
                      Description: ${task.description}
                      Priority: ${task.priority}
                      Estimated time: ${task.estimatedMin} minutes
                    """.trimIndent()
                }
            }

        val prompt = """
            You are an AI productivity coach.

            Help the user organise their work in a supportive,
            practical and concise way.

            Current active tasks:
            $taskSummary

            User's question:
            $question

            Instructions:
            - Base your advice on the supplied tasks.
            - Prefer clear and achievable next steps.
            - Do not claim that you completed a task.
            - Do not invent tasks that were not supplied.
            - Keep the response below 250 words.
            - If the user asks for harmful or unrelated advice,
              politely redirect them to productivity planning.
        """.trimIndent()

        val response =
            model.generateContent(prompt)

        return response.text
            ?.trim()
            ?.takeIf { it.isNotBlank() }
            ?: "The AI coach did not return a response."
    }
}
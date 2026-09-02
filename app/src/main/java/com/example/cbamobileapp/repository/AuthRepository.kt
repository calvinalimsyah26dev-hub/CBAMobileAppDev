package com.example.cbamobileapp.repository

interface AuthRepository {

    val currentUserId: String?

    val currentUserEmail: String?

    suspend fun register(
        email: String,
        password: String
    )

    suspend fun signIn(
        email: String,
        password: String
    )

    fun signOut()
}
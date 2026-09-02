package com.example.cbamobileapp.repository

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseAuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {

    override val currentUserId: String?
        get() = firebaseAuth.currentUser?.uid

    override val currentUserEmail: String?
        get() = firebaseAuth.currentUser?.email

    override suspend fun register(
        email: String,
        password: String
    ) {
        firebaseAuth
            .createUserWithEmailAndPassword(email, password)
            .await()
    }

    override suspend fun signIn(
        email: String,
        password: String
    ) {
        firebaseAuth
            .signInWithEmailAndPassword(email, password)
            .await()
    }

    override fun signOut() {
        firebaseAuth.signOut()
    }
}
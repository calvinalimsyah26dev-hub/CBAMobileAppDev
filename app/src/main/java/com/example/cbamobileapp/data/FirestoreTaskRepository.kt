package com.example.cbamobileapp.data

import com.example.cbamobileapp.data.cloud.FirestoreTaskDocument
import com.example.cbamobileapp.data.cloud.toFirestoreTaskDocument
import com.example.cbamobileapp.data.cloud.toProductivityTask
import com.example.cbamobileapp.model.ProductivityTask
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.tasks.await

@Singleton
class FirestoreTaskRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) : TaskRepository {

    override val tasks: Flow<List<ProductivityTask>> =
        callbackFlow {
            var taskListener: ListenerRegistration? = null

            val authListener =
                FirebaseAuth.AuthStateListener { auth ->
                    // Stop listening to the previous user's tasks.
                    taskListener?.remove()
                    taskListener = null

                    val user = auth.currentUser

                    if (user == null) {
                        trySend(emptyList())
                        return@AuthStateListener
                    }

                    taskListener = firestore
                        .collection("users")
                        .document(user.uid)
                        .collection("tasks")
                        .orderBy(
                            "createdAt",
                            Query.Direction.DESCENDING
                        )
                        .addSnapshotListener {
                                snapshot,
                                error ->

                            if (error != null) {
                                return@addSnapshotListener
                            }

                            val updatedTasks = snapshot
                                ?.documents
                                ?.mapNotNull { document ->
                                    document.toObject(
                                        FirestoreTaskDocument::class.java
                                    )
                                }
                                ?.map { document ->
                                    document.toProductivityTask()
                                }
                                ?: emptyList()

                            trySend(updatedTasks)
                        }
                }

            firebaseAuth.addAuthStateListener(
                authListener
            )

            awaitClose {
                taskListener?.remove()

                firebaseAuth.removeAuthStateListener(
                    authListener
                )
            }
        }.distinctUntilChanged()

    override suspend fun addTask(
        task: ProductivityTask
    ) {
        val userId = requireCurrentUserId()

        /*
         * ProductivityTask currently uses a Long ID.
         * The current time gives each new task a useful
         * unique numeric ID for this project.
         */
        val generatedId =
            if (task.id > 0) {
                task.id
            } else {
                System.currentTimeMillis()
            }

        val taskDocument =
            task.toFirestoreTaskDocument(
                generatedId = generatedId
            )

        firestore
            .collection("users")
            .document(userId)
            .collection("tasks")
            .document(generatedId.toString())
            .set(taskDocument)
            .await()
    }

    override suspend fun updateTaskCompletion(
        taskId: Long,
        isCompleted: Boolean
    ) {
        val userId = requireCurrentUserId()

        firestore
            .collection("users")
            .document(userId)
            .collection("tasks")
            .document(taskId.toString())
            .update(
                "completed",
                isCompleted
            )
            .await()
    }

    private fun requireCurrentUserId(): String {
        return firebaseAuth.currentUser?.uid
            ?: throw IllegalStateException(
                "You must be signed in to access tasks."
            )
    }
}
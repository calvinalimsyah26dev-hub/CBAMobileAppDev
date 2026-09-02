package com.example.cbamobileapp.pages

import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cbamobileapp.viewmodel.QuoteViewModel
import com.example.cbamobileapp.viewmodel.TaskViewModel

@Composable
fun ProductivityCoachApp(
    userEmail: String,
    onSignOut: () -> Unit,
    taskViewModel: TaskViewModel = hiltViewModel(),
    quoteViewModel: QuoteViewModel = hiltViewModel()
) {
    val navController = rememberNavController()

    val tasks by taskViewModel.tasks
        .collectAsStateWithLifecycle()

    val quoteUiState = quoteViewModel.uiState

    NavHost(
        navController = navController,
        startDestination = AppRoutes.TASK_LIST,
        modifier = Modifier.safeDrawingPadding()
    ) {
        composable(
            route = AppRoutes.TASK_LIST
        ) {
            TaskListScreen(
                tasks = tasks,
                quoteUiState = quoteUiState,
                onRefreshQuote = {
                    quoteViewModel.refreshQuote()
                },
                onAddTaskClick = {
                    navController.navigate(
                        AppRoutes.ADD_TASK
                    )
                },
                onTaskCompletedChange = {
                        taskId,
                        isCompleted ->

                    taskViewModel.updateTaskCompletion(
                        taskId = taskId,
                        isCompleted = isCompleted
                    )
                },
                userEmail = userEmail,
                onSignOut = onSignOut
            )
        }

        composable(
            route = AppRoutes.ADD_TASK
        ) {
            AddTaskScreen(
                onSaveTask = { newTask ->
                    taskViewModel.addTask(newTask)
                    navController.popBackStack()
                },
                onCancel = {
                    navController.popBackStack()
                }
            )
        }
    }
}
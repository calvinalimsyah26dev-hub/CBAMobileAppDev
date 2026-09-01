package com.example.cbamobileapp.pages

import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cbamobileapp.viewmodel.TaskViewModel

@Composable
fun ProductivityCoachApp(
    taskViewModel: TaskViewModel = viewModel()
) {
    val navController =
        rememberNavController()

    val tasks by
    taskViewModel.tasks
        .collectAsStateWithLifecycle()

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
                onAddTaskClick = {
                    navController.navigate(
                        AppRoutes.ADD_TASK
                    )
                },
                onTaskCompletedChange = {
                        taskId,
                        isCompleted ->

                    taskViewModel
                        .updateTaskCompletion(
                            taskId = taskId,
                            isCompleted = isCompleted
                        )
                }
            )
        }

        composable(
            route = AppRoutes.ADD_TASK
        ) {
            AddTaskScreen(
                onSaveTask = { newTask ->
                    taskViewModel.addTask(
                        newTask
                    )

                    navController.popBackStack()
                },
                onCancel = {
                    navController.popBackStack()
                }
            )
        }
    }
}


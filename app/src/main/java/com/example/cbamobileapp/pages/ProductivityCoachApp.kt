package com.example.cbamobileapp.pages

import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cbamobileapp.ui.theme.AiProductivityCoachTheme
import com.example.cbamobileapp.viewmodel.TaskViewModel

@Composable
fun ProductivityCoachApp(
    taskViewModel: TaskViewModel = viewModel()
) {
    /*
     * The NavController manages movement between screens
     * and keeps a back stack.
     */
    val navController = rememberNavController()

    /*
     * NavHost connects route names to Compose screens.
     */
    NavHost(
        navController = navController,
        startDestination = AppRoutes.TASK_LIST,
        modifier = Modifier.safeDrawingPadding()
    ) {
        /*
         * Task-list destination.
         */
        composable(
            route = AppRoutes.TASK_LIST
        ) {
            TaskListScreen(
                tasks = taskViewModel.tasks,

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
                }
            )
        }

        /*
         * Add-task destination.
         */
        composable(
            route = AppRoutes.ADD_TASK
        ) {
            AddTaskScreen(
                onSaveTask = { newTask ->
                    taskViewModel.addTask(newTask)

                    /*
                     * Remove AddTaskScreen from the back stack
                     * and return to TaskListScreen.
                     */
                    navController.popBackStack()
                },

                onCancel = {
                    /*
                     * Return without saving anything.
                     */
                    navController.popBackStack()
                }
            )
        }
    }
}


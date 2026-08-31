package com.example.cbamobileapp.pages

import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.cbamobileapp.model.ProductivityTask
import com.example.cbamobileapp.model.TaskPriority
import com.example.cbamobileapp.ui.theme.AiProductivityCoachTheme

/*
 * These are the two pages currently available in the application.
 */
private enum class ProductivityCoachScreen {
    TASK_LIST,
    ADD_TASK
}

@Composable
fun ProductivityCoachApp() {
    /*
     * This state remembers which page is currently visible.
     *
     * The application starts on the task-list page.
     */
    var currentScreen by remember {
        mutableStateOf(
            ProductivityCoachScreen.TASK_LIST
        )
    }

    /*
     * This is the application's current task list.
     *
     * It is temporary, in-memory data. Room will provide
     * permanent storage in a later week.
     */
    var tasks by remember {
        mutableStateOf(
            listOf(
                ProductivityTask(
                    id = 1,
                    title = "Complete Android project",
                    description = "Finish the task-list screen",
                    priority = TaskPriority.HIGH,
                    estimatedMin = 60
                ),
                ProductivityTask(
                    id = 2,
                    title = "Review Kotlin",
                    description = "Practise data classes and collections",
                    priority = TaskPriority.MEDIUM,
                    estimatedMin = 30
                ),
                ProductivityTask(
                    id = 3,
                    title = "Plan tomorrow",
                    description = "Choose the three most important tasks",
                    priority = TaskPriority.LOW,
                    estimatedMin = 10
                )
            )
        )
    }

    /*
     * Display the appropriate page based on currentScreen.
     */
    when (currentScreen) {
        ProductivityCoachScreen.TASK_LIST -> {
            TaskListScreen(
                tasks = tasks,

                onAddTaskClick = {
                    currentScreen =
                        ProductivityCoachScreen.ADD_TASK
                },

                onTaskCompletedChange = {
                        taskId,
                        isCompleted ->

                    /*
                     * Create an updated task list.
                     *
                     * Only the task with the matching ID is copied
                     * with a new isCompleted value.
                     */
                    tasks = tasks.map { task ->
                        if (task.id == taskId) {
                            task.copy(
                                isCompleted = isCompleted
                            )
                        } else {
                            task
                        }
                    }
                },

                modifier = Modifier.safeDrawingPadding()
            )
        }

        ProductivityCoachScreen.ADD_TASK -> {
            AddTaskScreen(
                onSaveTask = { newTask ->
                    /*
                     * Add the new task to the existing list.
                     */
                    tasks = tasks + newTask

                    /*
                     * Return to the task-list page.
                     */
                    currentScreen =
                        ProductivityCoachScreen.TASK_LIST
                },

                onCancel = {
                    /*
                     * Return without creating a task.
                     */
                    currentScreen =
                        ProductivityCoachScreen.TASK_LIST
                },

                modifier = Modifier.safeDrawingPadding()
            )
        }
    }
}

/*
 * Preview of the complete application.
 */
@Preview(
    name = "Complete Productivity Coach",
    showBackground = true,
    showSystemUi = true,
    device = "spec:width=411dp,height=891dp"
)
@Composable
private fun ProductivityCoachAppPreview() {
    AiProductivityCoachTheme {
        ProductivityCoachApp()
    }
}
package com.example.cbamobileapp.pages


import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cbamobileapp.model.ProductivityTask
import com.example.cbamobileapp.model.TaskPriority
import com.example.cbamobileapp.ui.theme.AiProductivityCoachTheme

@Composable
fun TaskListScreen(
    tasks: List<ProductivityTask>,
    onAddTaskClick: () -> Unit,
    onTaskCompletedChange: (
        taskId: Long,
        isCompleted: Boolean
    ) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "AI Productivity Coach",
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = "Organise your work and make progress.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Text(
            text = "Completed: ${tasks.count { it.isCompleted }} of ${tasks.size}",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Button(
            onClick = onAddTaskClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add a task")
        }

        if (tasks.isEmpty()) {
            EmptyTaskMessage(
                modifier = Modifier.weight(1f)
            )
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(
                    items = tasks,
                    key = { task ->
                        task.id
                    }
                ) { task ->
                    TaskCard(
                        task = task,
                        onCompletedChange = { isCompleted ->
                            onTaskCompletedChange(
                                task.id,
                                isCompleted
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyTaskMessage(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "You don't have any tasks yet.\n" +
                    "Add your first task to get started.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

/*
 * Preview-only tasks
 */

private val taskListPreviewTasks = listOf(
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
        estimatedMin = 30,
        isCompleted = true
    ),
    ProductivityTask(
        id = 3,
        title = "Plan tomorrow",
        description = "Select the three most important tasks",
        priority = TaskPriority.LOW,
        estimatedMin = 10
    )
)

/*
 * TaskListScreen previews
 */

@Preview(
    name = "Task List With Tasks",
    showBackground = true,
    showSystemUi = true,
    device = "spec:width=411dp,height=891dp"
)
@Composable
private fun TaskListScreenPreview() {
    AiProductivityCoachTheme {
        Surface {
            TaskListScreen(
                tasks = taskListPreviewTasks,
                onAddTaskClick = {},
                onTaskCompletedChange = { _, _ -> }
            )
        }
    }
}

@Preview(
    name = "Empty Task List",
    showBackground = true,
    showSystemUi = true,
    device = "spec:width=411dp,height=891dp"
)
@Composable
private fun EmptyTaskListScreenPreview() {
    AiProductivityCoachTheme {
        Surface {
            TaskListScreen(
                tasks = emptyList(),
                onAddTaskClick = {},
                onTaskCompletedChange = { _, _ -> }
            )
        }
    }
}

@Preview(
    name = "Task List Dark Mode",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    device = "spec:width=411dp,height=891dp"
)
@Composable
private fun DarkTaskListScreenPreview() {
    AiProductivityCoachTheme {
        Surface {
            TaskListScreen(
                tasks = taskListPreviewTasks,
                onAddTaskClick = {},
                onTaskCompletedChange = { _, _ -> }
            )
        }
    }
}
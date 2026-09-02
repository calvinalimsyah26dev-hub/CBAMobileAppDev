package com.example.cbamobileapp.pages

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cbamobileapp.model.MotivationalQuote
import com.example.cbamobileapp.model.ProductivityTask
import com.example.cbamobileapp.model.TaskPriority
import com.example.cbamobileapp.ui.theme.AiProductivityCoachTheme
import com.example.cbamobileapp.viewmodel.QuoteUiState
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.example.cbamobileapp.notification.NotificationHelper
import com.example.cbamobileapp.notification.ReminderScheduler

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(
    tasks: List<ProductivityTask>,
    quoteUiState: QuoteUiState,
    onRefreshQuote: () -> Unit,
    onAddTaskClick: () -> Unit,
    onOpenAiCoach: () -> Unit,
    onTaskCompletedChange: (Long, Boolean) -> Unit,
    userEmail: String,
    onSignOut: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showSettings by remember {
        mutableStateOf(false)
    }

    /*
     * Only tasks that are not completed are displayed.
     *
     * When Firestore changes isCompleted to true,
     * that task is automatically removed from this list.
     */
    val activeTasks = tasks.filterNot { task ->
        task.isCompleted
    }

    val completedTaskCount = tasks.count { task ->
        task.isCompleted
    }

    val context = LocalContext.current

    var remindersEnabled by remember {
        mutableStateOf(
            ReminderScheduler.areRemindersEnabled(
                context
            )
        )
    }

    val notificationPermissionLauncher =
        rememberLauncherForActivityResult(
            contract =
                ActivityResultContracts.RequestPermission()
        ) { permissionGranted ->
            if (permissionGranted) {
                ReminderScheduler.enableReminders(
                    context
                )

                remindersEnabled = true

                NotificationHelper.showTaskReminder(
                    context = context,
                    incompleteTaskCount =
                        activeTasks.size
                )
            } else {
                remindersEnabled = false

                Toast.makeText(
                    context,
                    "Notification permission was not granted.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    fun enableNotifications() {
        val permissionAlreadyGranted =
            Build.VERSION.SDK_INT <
                    Build.VERSION_CODES.TIRAMISU ||
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) == PackageManager.PERMISSION_GRANTED

        if (permissionAlreadyGranted) {
            ReminderScheduler.enableReminders(
                context
            )

            remindersEnabled = true

            NotificationHelper.showTaskReminder(
                context = context,
                incompleteTaskCount =
                    activeTasks.size
            )
        } else {
            notificationPermissionLauncher.launch(
                Manifest.permission.POST_NOTIFICATIONS
            )
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text("AI Productivity Coach")
                },
                actions = {
                    IconButton(
                        onClick = {
                            showSettings = true
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Open settings"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(
                    horizontal = 16.dp,
                    vertical = 8.dp
                ),
            verticalArrangement =
                Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Organise your work and make progress.",
                style = MaterialTheme.typography.bodyLarge,
                color =
                    MaterialTheme.colorScheme.onSurfaceVariant
            )

            MotivationCard(
                uiState = quoteUiState,
                onRefresh = onRefreshQuote
            )

            OutlinedButton(
                onClick = onOpenAiCoach,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ask AI Coach")
            }

            TaskProgressText(
                remainingTaskCount = activeTasks.size,
                completedTaskCount = completedTaskCount
            )

            Button(
                onClick = onAddTaskClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add a task")
            }

            if (activeTasks.isEmpty()) {
                EmptyTaskMessage(
                    hasCompletedTasks =
                        completedTaskCount > 0,
                    modifier = Modifier.weight(1f)
                )
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement =
                        Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = activeTasks,
                        key = { task ->
                            task.id
                        }
                    ) { task ->
                        TaskCard(
                            task = task,
                            onCompletedChange = {
                                    isCompleted ->

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

    if (showSettings) {
        SettingsDialog(
            userEmail = userEmail,
            remindersEnabled = remindersEnabled,
            onRemindersChanged = { enabled ->
                if (enabled) {
                    enableNotifications()
                } else {
                    ReminderScheduler.disableReminders(
                        context
                    )

                    remindersEnabled = false
                }
            },
            onTestNotification = {
                if (NotificationHelper.canShowNotifications(context)) {
                    NotificationHelper.showTaskReminder(
                        context = context,
                        incompleteTaskCount =
                            activeTasks.size.coerceAtLeast(1)
                    )
                } else {
                    enableNotifications()
                }
            },
            onDismiss = {
                showSettings = false
            },
            onSignOut = {
                showSettings = false
                onSignOut()
            }
        )
    }
}

@Composable
private fun TaskProgressText(
    remainingTaskCount: Int,
    completedTaskCount: Int
) {
    val remainingText =
        if (remainingTaskCount == 1) {
            "1 task remaining"
        } else {
            "$remainingTaskCount tasks remaining"
        }

    val completedText =
        if (completedTaskCount == 1) {
            "1 completed"
        } else {
            "$completedTaskCount completed"
        }

    Text(
        text = "$remainingText • $completedText",
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.primary
    )
}

@Composable
private fun EmptyTaskMessage(
    hasCompletedTasks: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = if (hasCompletedTasks) {
                "All tasks completed!\nGreat work."
            } else {
                "You don't have any tasks yet.\n" +
                        "Add your first task to get started."
            },
            style = MaterialTheme.typography.bodyLarge,
            color =
                MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun SettingsDialog(
    userEmail: String,
    remindersEnabled: Boolean,
    onRemindersChanged: (Boolean) -> Unit,
    onTestNotification: () -> Unit,
    onDismiss: () -> Unit,
    onSignOut: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Settings")
        },
        text = {
            Column(
                verticalArrangement =
                    Arrangement.spacedBy(16.dp)
            ) {
                Column {
                    Text(
                        text = "Signed in as",
                        style =
                            MaterialTheme.typography.labelMedium,
                        color =
                            MaterialTheme.colorScheme
                                .onSurfaceVariant
                    )

                    Text(
                        text = userEmail.ifBlank {
                            "No email available"
                        },
                        style =
                            MaterialTheme.typography.bodyLarge
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement =
                        Arrangement.SpaceBetween,
                    verticalAlignment =
                        Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Daily reminders",
                            style =
                                MaterialTheme.typography.titleMedium
                        )

                        Text(
                            text =
                                "Remind me when unfinished tasks remain.",
                            style =
                                MaterialTheme.typography.bodySmall,
                            color =
                                MaterialTheme.colorScheme
                                    .onSurfaceVariant
                        )
                    }

                    Switch(
                        checked = remindersEnabled,
                        onCheckedChange =
                            onRemindersChanged
                    )
                }

                OutlinedButton(
                    onClick = onTestNotification,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Send test notification")
                }

                Text(
                    text =
                        "Daily reminders are approximate because " +
                                "Android chooses a battery-efficient time.",
                    style =
                        MaterialTheme.typography.bodySmall,
                    color =
                        MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onSignOut,
                colors = ButtonDefaults.buttonColors(
                    containerColor =
                        MaterialTheme.colorScheme.error,
                    contentColor =
                        MaterialTheme.colorScheme.onError
                )
            ) {
                Text("Sign out")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text("Close")
            }
        }
    )
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
        description =
            "Select the three most important tasks",
        priority = TaskPriority.LOW,
        estimatedMin = 10
    )
)

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
                quoteUiState = QuoteUiState.Success(
                    quote = MotivationalQuote(
                        text =
                            "Success is built one task at a time.",
                        author =
                            "Productivity Coach"
                    )
                ),
                onRefreshQuote = {},
                onAddTaskClick = {},
                onTaskCompletedChange = { _, _ -> },
                userEmail = "student@example.com",
                onSignOut = {},
                onOpenAiCoach = {}
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
                quoteUiState = QuoteUiState.Success(
                    quote = MotivationalQuote(
                        text = "Start where you are.",
                        author = "Arthur Ashe"
                    )
                ),
                onRefreshQuote = {},
                onAddTaskClick = {},
                onTaskCompletedChange = { _, _ -> },
                userEmail = "student@example.com",
                onSignOut = {},
                onOpenAiCoach = {}
            )
        }
    }
}

@Preview(
    name = "All Tasks Completed",
    showBackground = true,
    showSystemUi = true,
    device = "spec:width=411dp,height=891dp"
)
@Composable
private fun CompletedTaskListScreenPreview() {
    AiProductivityCoachTheme {
        Surface {
            TaskListScreen(
                tasks = taskListPreviewTasks.map { task ->
                    task.copy(isCompleted = true)
                },
                quoteUiState = QuoteUiState.Success(
                    quote = MotivationalQuote(
                        text = "Great work!",
                        author = "Productivity Coach"
                    )
                ),
                onRefreshQuote = {},
                onAddTaskClick = {},
                onTaskCompletedChange = { _, _ -> },
                userEmail = "student@example.com",
                onSignOut = {},
                onOpenAiCoach = {}
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
                quoteUiState = QuoteUiState.Success(
                    quote = MotivationalQuote(
                        text = "Start where you are.",
                        author = "Arthur Ashe"
                    )
                ),
                onRefreshQuote = {},
                onAddTaskClick = {},
                onTaskCompletedChange = { _, _ -> },
                userEmail = "student@example.com",
                onSignOut = {},
                onOpenAiCoach = {}
            )
        }
    }
}
package com.example.cbamobileapp.pages

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cbamobileapp.model.ProductivityTask
import com.example.cbamobileapp.model.TaskPriority
import com.example.cbamobileapp.ui.theme.AiProductivityCoachTheme

@Composable
fun AddTaskScreen(
    onSaveTask: (ProductivityTask) -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    var title by remember {
        mutableStateOf("")
    }

    var description by remember {
        mutableStateOf("")
    }

    var estimatedMinutesText by remember {
        mutableStateOf("")
    }

    var selectedPriority by remember {
        mutableStateOf(TaskPriority.MEDIUM)
    }

    var showValidationError by remember {
        mutableStateOf(false)
    }

    val estimatedMinutes =
        estimatedMinutesText.toIntOrNull()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .imePadding()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Add Task",
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = "Add something you want to complete.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        OutlinedTextField(
            value = title,
            onValueChange = { newTitle ->
                title = newTitle
                showValidationError = false
            },
            label = {
                Text("Task title")
            },
            supportingText = {
                Text("Give your task a short, clear name.")
            },
            singleLine = true,
            isError = showValidationError && title.isBlank(),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = description,
            onValueChange = { newDescription ->
                description = newDescription
            },
            label = {
                Text("Description")
            },
            supportingText = {
                Text("Describe what needs to be done.")
            },
            minLines = 3,
            maxLines = 5,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = estimatedMinutesText,
            onValueChange = { newValue ->
                if (newValue.all { character ->
                        character.isDigit()
                    }
                ) {
                    estimatedMinutesText = newValue
                    showValidationError = false
                }
            },
            label = {
                Text("Estimated minutes")
            },
            supportingText = {
                Text("For example: 30")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            singleLine = true,
            isError = showValidationError &&
                    (
                            estimatedMinutes == null ||
                                    estimatedMinutes <= 0
                            ),
            modifier = Modifier.fillMaxWidth()
        )

        Text(
            text = "Priority",
            style = MaterialTheme.typography.titleMedium
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TaskPriority.values().forEach { priority ->
                FilterChip(
                    selected = selectedPriority == priority,
                    onClick = {
                        selectedPriority = priority
                    },
                    label = {
                        Text(priority.displayName())
                    }
                )
            }
        }

        if (showValidationError) {
            Text(
                text = "Enter a task title and an estimated " +
                        "time greater than zero.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error
            )
        }

        Button(
            onClick = {
                val taskIsValid =
                    title.isNotBlank() &&
                            estimatedMinutes != null &&
                            estimatedMinutes > 0

                if (!taskIsValid) {
                    showValidationError = true
                } else {
                    val newTask = ProductivityTask(
                        id = System.currentTimeMillis(),
                        title = title.trim(),
                        description = description.trim(),
                        priority = selectedPriority,
                        estimatedMin = estimatedMinutes
                    )

                    onSaveTask(newTask)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save task")
        }

        OutlinedButton(
            onClick = onCancel,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cancel")
        }
    }
}

private fun TaskPriority.displayName(): String {
    return name
        .lowercase()
        .replaceFirstChar { character ->
            character.uppercase()
        }
}

/*
 * AddTaskScreen previews
 */

@Preview(
    name = "Add Task Screen",
    showBackground = true,
    showSystemUi = true,
    device = "spec:width=411dp,height=891dp"
)
@Composable
private fun AddTaskScreenPreview() {
    AiProductivityCoachTheme {
        Surface {
            AddTaskScreen(
                onSaveTask = {},
                onCancel = {}
            )
        }
    }
}

@Preview(
    name = "Add Task Screen Dark Mode",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    device = "spec:width=411dp,height=891dp"
)
@Composable
private fun DarkAddTaskScreenPreview() {
    AiProductivityCoachTheme {
        Surface {
            AddTaskScreen(
                onSaveTask = {},
                onCancel = {}
            )
        }
    }
}
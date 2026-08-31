package com.example.cbamobileapp.pages

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cbamobileapp.model.ProductivityTask
import com.example.cbamobileapp.model.TaskPriority
import com.example.cbamobileapp.ui.theme.AiProductivityCoachTheme

@Composable
fun TaskCard(
    task: ProductivityTask,
    onCompletedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Checkbox(
                checked = task.isCompleted,
                onCheckedChange = onCompletedChange
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleMedium,
                    textDecoration = if (task.isCompleted) {
                        TextDecoration.LineThrough
                    } else {
                        TextDecoration.None
                    }
                )

                if (task.description.isNotBlank()) {
                    Text(
                        text = task.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Text(
                    text = "Priority: ${task.priority.displayName()}",
                    style = MaterialTheme.typography.labelLarge,
                    color = priorityColour(task.priority)
                )

                Text(
                    text = "Estimated time: ${task.estimatedMin} minutes",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun priorityColour(
    priority: TaskPriority
) = when (priority) {
    TaskPriority.HIGH -> MaterialTheme.colorScheme.error
    TaskPriority.MEDIUM -> MaterialTheme.colorScheme.primary
    TaskPriority.LOW -> MaterialTheme.colorScheme.secondary
}

private fun TaskPriority.displayName(): String {
    return name
        .lowercase()
        .replaceFirstChar { character ->
            character.uppercase()
        }
}

/*
 * TaskCard previews
 */

@Preview(
    name = "Incomplete Task Card",
    showBackground = true
)
@Composable
private fun IncompleteTaskCardPreview() {
    AiProductivityCoachTheme {
        Surface {
            TaskCard(
                task = ProductivityTask(
                    id = 1,
                    title = "Complete Android project",
                    description = "Finish designing the TaskCard",
                    priority = TaskPriority.HIGH,
                    estimatedMin = 60
                ),
                onCompletedChange = {},
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Preview(
    name = "Completed Task Card",
    showBackground = true
)
@Composable
private fun CompletedTaskCardPreview() {
    AiProductivityCoachTheme {
        Surface {
            TaskCard(
                task = ProductivityTask(
                    id = 2,
                    title = "Review Kotlin",
                    description = "Practise data classes and collections",
                    priority = TaskPriority.MEDIUM,
                    estimatedMin = 30,
                    isCompleted = true
                ),
                onCompletedChange = {},
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Preview(
    name = "Task Card Dark Mode",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun DarkTaskCardPreview() {
    AiProductivityCoachTheme {
        Surface {
            TaskCard(
                task = ProductivityTask(
                    id = 3,
                    title = "Plan tomorrow",
                    description = "Choose the three most important tasks",
                    priority = TaskPriority.LOW,
                    estimatedMin = 15
                ),
                onCompletedChange = {},
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}
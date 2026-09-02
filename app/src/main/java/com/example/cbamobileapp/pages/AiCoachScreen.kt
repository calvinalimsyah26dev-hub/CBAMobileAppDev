package com.example.cbamobileapp.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cbamobileapp.ui.theme.AiProductivityCoachTheme
import com.example.cbamobileapp.viewmodel.AiCoachUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiCoachScreen(
    uiState: AiCoachUiState,
    taskCount: Int,
    onAskCoach: (String) -> Unit,
    onRetry: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var question by remember {
        mutableStateOf("")
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text("AI Productivity Coach")
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack
                    ) {
                        Icon(
                            imageVector =
                                Icons.AutoMirrored
                                    .Filled.ArrowBack,
                            contentDescription = "Go back"
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
                .padding(16.dp)
                .verticalScroll(
                    rememberScrollState()
                ),
            verticalArrangement =
                Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Ask your AI coach",
                style =
                    MaterialTheme.typography.headlineSmall
            )

            Text(
                text =
                    "The coach can currently see " +
                            "$taskCount active tasks.",
                style =
                    MaterialTheme.typography.bodyLarge,
                color =
                    MaterialTheme.colorScheme
                        .onSurfaceVariant
            )

            OutlinedTextField(
                value = question,
                onValueChange = {
                    question = it
                },
                label = {
                    Text("What do you need help with?")
                },
                placeholder = {
                    Text(
                        "For example: What should I work on first?"
                    )
                },
                minLines = 3,
                maxLines = 6,
                enabled =
                    uiState !is AiCoachUiState.Loading,
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    onAskCoach(question)
                },
                enabled =
                    uiState !is AiCoachUiState.Loading &&
                            question.isNotBlank(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ask coach")
            }

            AiCoachResult(
                uiState = uiState,
                onRetry = onRetry
            )
        }
    }
}

@Composable
private fun AiCoachResult(
    uiState: AiCoachUiState,
    onRetry: () -> Unit
) {
    when (uiState) {
        AiCoachUiState.Ready -> {
            Text(
                text =
                    "Try asking which task to start, how to " +
                            "divide your work, or how to plan today.",
                color =
                    MaterialTheme.colorScheme
                        .onSurfaceVariant
            )
        }

        AiCoachUiState.Loading -> {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.Center,
                verticalAlignment =
                    Alignment.CenterVertically
            ) {
                CircularProgressIndicator()

                Spacer(
                    modifier = Modifier.height(8.dp)
                )
            }

            Text(
                text = "Creating your productivity plan..."
            )
        }

        is AiCoachUiState.Success -> {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement =
                        Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Coach's response",
                        style =
                            MaterialTheme.typography.titleMedium
                    )

                    Text(
                        text = uiState.response,
                        style =
                            MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }

        is AiCoachUiState.Error -> {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement =
                        Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Unable to contact the coach",
                        style =
                            MaterialTheme.typography.titleMedium,
                        color =
                            MaterialTheme.colorScheme.error
                    )

                    Text(uiState.message)

                    OutlinedButton(
                        onClick = onRetry
                    ) {
                        Text("Try again")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AiCoachScreenPreview() {
    AiProductivityCoachTheme {
        AiCoachScreen(
            uiState = AiCoachUiState.Success(
                response =
                    "Start with your high-priority Android " +
                            "project. Work for 25 minutes, take " +
                            "a short break, and then review Kotlin."
            ),
            taskCount = 3,
            onAskCoach = {},
            onRetry = {},
            onBack = {}
        )
    }
}
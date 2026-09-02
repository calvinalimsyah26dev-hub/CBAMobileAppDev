package com.example.cbamobileapp.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cbamobileapp.model.MotivationalQuote
import com.example.cbamobileapp.ui.theme.AiProductivityCoachTheme
import com.example.cbamobileapp.viewmodel.QuoteUiState

@Composable
fun MotivationCard(
    uiState: QuoteUiState,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement =
                Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Daily motivation",
                style =
                    MaterialTheme.typography
                        .titleLarge
            )

            when (uiState) {
                QuoteUiState.Loading -> {
                    Column(
                        modifier =
                            Modifier.fillMaxWidth(),
                        horizontalAlignment =
                            Alignment.CenterHorizontally,
                        verticalArrangement =
                            Arrangement.spacedBy(
                                8.dp
                            )
                    ) {
                        CircularProgressIndicator()

                        Text(
                            text =
                                "Loading motivation..."
                        )
                    }
                }

                is QuoteUiState.Success -> {
                    Text(
                        text =
                            "\"${uiState.quote.text}\"",
                        style =
                            MaterialTheme.typography
                                .bodyLarge
                    )

                    Text(
                        text =
                            "- ${uiState.quote.author}",
                        style =
                            MaterialTheme.typography
                                .labelLarge,
                        color =
                            MaterialTheme.colorScheme
                                .primary
                    )

                    OutlinedButton(
                        onClick = onRefresh
                    ) {
                        Text("New quote")
                    }
                }

                is QuoteUiState.Error -> {
                    Text(
                        text = uiState.message,
                        color =
                            MaterialTheme.colorScheme
                                .error
                    )

                    Button(
                        onClick = onRefresh
                    ) {
                        Text("Try again")
                    }
                }
            }
        }
    }
}

/*
 * Preview the successful state.
 */
@Preview(
    name = "Motivation Success",
    showBackground = true
)
@Composable
private fun MotivationCardSuccessPreview() {
    AiProductivityCoachTheme {
        Surface {
            MotivationCard(
                uiState =
                    QuoteUiState.Success(
                        quote =
                            MotivationalQuote(
                                text =
                                    "Small steps create progress.",
                                author =
                                    "Productivity Coach"
                            )
                    ),
                onRefresh = {},
                modifier =
                    Modifier.padding(16.dp)
            )
        }
    }
}

/*
 * Preview the loading state.
 */
@Preview(
    name = "Motivation Loading",
    showBackground = true
)
@Composable
private fun MotivationCardLoadingPreview() {
    AiProductivityCoachTheme {
        Surface {
            MotivationCard(
                uiState =
                    QuoteUiState.Loading,
                onRefresh = {},
                modifier =
                    Modifier.padding(16.dp)
            )
        }
    }
}

/*
 * Preview the error state.
 */
@Preview(
    name = "Motivation Error",
    showBackground = true
)
@Composable
private fun MotivationCardErrorPreview() {
    AiProductivityCoachTheme {
        Surface {
            MotivationCard(
                uiState =
                    QuoteUiState.Error(
                        message =
                            "Check your internet connection."
                    ),
                onRefresh = {},
                modifier =
                    Modifier.padding(16.dp)
            )
        }
    }
}
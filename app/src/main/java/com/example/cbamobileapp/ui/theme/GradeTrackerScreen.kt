package com.example.cbamobileapp.ui.theme

import android.R
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cbamobileapp.model.Student
import com.example.cbamobileapp.model.Subject

@Composable
fun GradeTrackerScreen(
    student: Student
) {
    var showGrades by remember {
        mutableStateOf(true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Grade Tracker",
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = "Student: ${student.name}",
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = "Average: %.1f%%".format(student.average()),
            style = MaterialTheme.typography.titleMedium
        )

        Button(
            onClick = {
                showGrades = !showGrades
            }
        ) {
            Text(
                text = if (showGrades) {
                    "Hide Grades"
                } else {
                    "Show Grades"
                }
            )
        }

        if (showGrades) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(student.subjects) { subject ->
                    SubjectCard(
                        subject = subject
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GradeTrackerScreenPreview() {

    val student = Student(
        name = "Calvin",
        subjects = listOf(
            Subject(
                name = "Kotlin",
                mark = 85,
                description = "Learning Kotlin programming"
            ),
            Subject(
                name = "Database",
                mark = 72,
                description = "Learning databases"
            ),
            Subject(
                name = "Networking",
                mark = 64,
                description = "Learning computer networking"
            )
        )
    )

    GradeTrackerScreen(
        student = student
    )
}
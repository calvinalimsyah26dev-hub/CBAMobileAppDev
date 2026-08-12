package com.example.cbamobileapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.cbamobileapp.model.Student
import com.example.cbamobileapp.model.Subject
import com.example.cbamobileapp.ui.theme.CBAMobileAppTheme
import com.example.cbamobileapp.ui.theme.GradeTrackerScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val student = Student(
            name = "Calvin",
            subjects = listOf(
                Subject(
                    name = "Maths",
                    mark = 70,
                    description = "add, minus, multi"
                ),
                Subject(
                    name = "English",
                    mark = 60,
                    description = "Grammar"
                ),
                Subject(
                    name = "Science",
                    mark = 80,
                    description = "Chemistry"
                )
            )
        )

        setContent {
            CBAMobileAppTheme() {
                GradeTrackerScreen(
                    student = student
                )
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CBAMobileAppTheme {
        Greeting("Android")
    }
}
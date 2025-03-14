package com.nazim.app.taskmanagerapp.ui.screens

import android.os.Bundle
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.hilt.navigation.compose.hiltViewModel
import com.nazim.app.taskmanagerapp.viewModel.TaskViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TaskDetailsScreen(
    taskId: Int?, navController: NavController, viewModel: TaskViewModel = hiltViewModel()
) {
    var showDialog by remember { mutableStateOf(false) }

    // Fetch task by ID from ViewModel
    taskId?.let {
        LaunchedEffect(taskId) {
            viewModel.fetchTaskById(taskId)
        }
    }

    // Get the task from the ViewModel
    val task = viewModel.taskDetail.collectAsState().value
    val backgroundColor = MaterialTheme.colorScheme.background

    if (task != null) {

        val dateFormatter =
            SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) // Format the date as dd/MM/yyyy
        val formattedDate = dateFormatter.format(Date(task.dueDate))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .background(backgroundColor)
            ) {
                // Title and task header
                Text(
                    text = "Task Details",
                    style = MaterialTheme.typography.headlineLarge.copy(fontSize = 28.sp),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Task details in a card with shadow and rounded corners
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(12.dp),
                    shape = MaterialTheme.shapes.medium.copy(CornerSize(20.dp))
                ) {
                    Column(
                        modifier = Modifier
                            .padding(24.dp)
                            .fillMaxWidth()
                    ) {
                        // Title
                        Text(
                            text = "Title: ${task.title}",
                            style = MaterialTheme.typography.titleLarge.copy(fontSize = 22.sp),
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        // Description
                        Text(
                            text = "Description: ${task.description ?: "No description available."}",
                            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp),
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        // Priority
                        Row(
                            modifier = Modifier.padding(bottom = 12.dp)
                        ) {
                            Text(
                                text = "Priority: ",
                                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp),
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Text(
                                text = task.priority,
                                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp),
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }

                        // Due date
                        Text(
                            text = "Due Date: $formattedDate",
                            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp),
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        // Completion status with color coding
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(bottom = 16.dp)
                        ) {
                            Text(
                                text = "Completed: ",
                                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp),
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            val statusColor = if (task.isCompleted) Color.Green else Color.Red
                            Text(
                                text = if (task.isCompleted) "Yes" else "No",
                                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp),
                                color = statusColor
                            )
                        }

                        // Actions (buttons)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            // Mark as completed button
                            Button(
                                onClick = {
                                    task.copy(isCompleted = true)
                                        .let { updatedTask -> viewModel.updateTask(updatedTask) }
                                },
                                shape = MaterialTheme.shapes.large,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    contentColor = Color.White
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(8.dp)
                            ) {
                                Text("Mark as Completed")
                            }

                            // Delete task button
                            Button(
                                onClick = { showDialog = true },
                                shape = MaterialTheme.shapes.large,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.error,
                                    contentColor = Color.White
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(8.dp)
                            ) {
                                Text("Delete Task")
                            }
                        }

                        // Confirmation Dialog for Deleting Task
                        if (showDialog) {
                            AlertDialog(
                                onDismissRequest = { showDialog = false },
                                title = { Text(text = "Are you sure?") },
                                text = { Text("This action cannot be undone.") },
                                confirmButton = {
                                    Button(
                                        onClick = {
                                            task.id?.let { taskId -> viewModel.deleteTask(taskId) }
                                            showDialog = false
                                            navController.popBackStack() // Go back to task list after deletion
                                        }
                                    ) {
                                        Text("Confirm")
                                    }
                                },
                                dismissButton = {
                                    Button(
                                        onClick = { showDialog = false }
                                    ) {
                                        Text("Cancel")
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    } else {
        Box(
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TaskDetailsScreenPreview() {
    TaskDetailsScreen(taskId = 1, navController = NavController(LocalContext.current))
}


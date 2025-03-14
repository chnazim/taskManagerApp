package com.nazim.app.taskmanagerapp.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.nazim.app.taskmanagerapp.data.model.Task
import com.nazim.app.taskmanagerapp.viewModel.TaskViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(viewModel: TaskViewModel, navController: NavHostController) {
    var selectedFilter by remember { mutableStateOf("All") }
    var selectedSort by remember { mutableStateOf("Due Date") }

    val tasks by viewModel.tasks.collectAsState(initial = emptyList())
    val coroutineScope = rememberCoroutineScope()

    // Sorting logic
    val sortedTasks = remember(tasks, selectedSort) {
        when (selectedSort) {
            "Priority" -> tasks.sortedByDescending { it.priority }
            "Alphabetically" -> tasks.sortedBy { it.title }
            else -> tasks.sortedBy { it.dueDate }
        }
    }

    // Filtering logic
    val filteredTasks = remember(sortedTasks, selectedFilter) {
        when (selectedFilter) {
            "Completed" -> sortedTasks.filter { it.isCompleted }
            "Pending" -> sortedTasks.filter { !it.isCompleted }
            else -> sortedTasks
        }
    }

    Scaffold(topBar = {
        TopAppBar(title = { Text("Task Manager") }, actions = {
            SortingDropdown(selectedSort) { selectedSort = it }
            FilteringDropdown(selectedFilter) { selectedFilter = it }
        })
    }, floatingActionButton = {
        FloatingActionButton(onClick = { navController.navigate("addTask") }) {
            Icon(Icons.Default.Add, contentDescription = "Add Task")
        }
    }) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            if (filteredTasks.isEmpty()) {
                EmptyStateUI()
            } else {
                LazyColumn {
                    items(filteredTasks, key = { it.id ?: 0 }) { task ->
                        val dismissState =
                            rememberSwipeToDismissBoxState(confirmValueChange = { dismissValue ->
                                if (dismissValue == SwipeToDismissBoxValue.EndToStart) {
                                    coroutineScope.launch { viewModel.deleteTask(task.id) }
                                    true
                                } else {
                                    false
                                }
                            })

                        SwipeToDismissBox(state = dismissState,
                            enableDismissFromStartToEnd = false,
                            enableDismissFromEndToStart = true,
                            backgroundContent = { DismissBackground(dismissState) },
                            content = {
                                TaskItem(task,
                                    onTaskClick = { navController.navigate("taskDetails/${task.id}") })
                            })
                    }
                }
            }
        }
    }
}

@Composable
fun SortingDropdown(selectedSort: String, onSortSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        IconButton(onClick = { expanded = true }) {
            Icon(Icons.Default.Sort, contentDescription = "Sort Tasks")
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            listOf("Due Date", "Priority", "Alphabetically").forEach { sortType ->
                DropdownMenuItem(text = { Text(sortType) }, onClick = {
                    onSortSelected(sortType)
                    expanded = false
                })
            }
        }
    }
}

@Composable
fun FilteringDropdown(selectedFilter: String, onFilterSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        IconButton(onClick = { expanded = true }) {
            Icon(Icons.Default.FilterList, contentDescription = "Filter Tasks")
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            listOf("All", "Completed", "Pending").forEach { filterType ->
                DropdownMenuItem(text = { Text(filterType) }, onClick = {
                    onFilterSelected(filterType)
                    expanded = false
                })
            }
        }
    }
}

@ExperimentalMaterial3Api
@Composable
fun DismissBackground(dismissState: SwipeToDismissBoxState) {
    val isVisible = dismissState.targetValue != SwipeToDismissBoxValue.Settled

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Red)
                .padding(16.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.White)
        }
    }
}


// Empty state UI when no tasks exist
@Composable
fun EmptyStateUI() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                Icons.Default.Info,
                contentDescription = "No tasks",
                tint = Color.Gray,
                modifier = Modifier.size(48.dp)
            )
            Text("No tasks yet. Add some!", color = Color.Gray, fontSize = 16.sp)
        }
    }
}

@Composable
fun TaskItem(task: Task, onTaskClick: () -> Unit) {
    val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) // Format: 12/03/2025
    val formattedDate = dateFormatter.format(Date(task.dueDate))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onTaskClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(task.title, style = MaterialTheme.typography.titleLarge)
            task.description?.let {
                Text(it, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text("Priority: ${task.priority}", style = MaterialTheme.typography.bodySmall)
            Text(
                "Due Date: $formattedDate", // ✅ Now formatted as dd/MM/yyyy
                style = MaterialTheme.typography.bodySmall, color = Color.Blue
            )
        }
    }
}


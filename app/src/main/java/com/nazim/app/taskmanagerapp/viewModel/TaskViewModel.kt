package com.nazim.app.taskmanagerapp.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nazim.app.taskmanagerapp.data.TaskDao
import com.nazim.app.taskmanagerapp.data.model.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(private val taskDao: TaskDao) : ViewModel() {
    // Existing flow for all tasks
    val tasks = taskDao.getAllTasks().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    // New flow for fetching task details by ID
    private val _taskDetail = MutableStateFlow<Task?>(null)
    val taskDetail: StateFlow<Task?> = _taskDetail

    // Fetch task by ID
    fun fetchTaskById(taskId: Int) {
        viewModelScope.launch {
            val task = withContext(Dispatchers.IO) {
                taskDao.getTaskById(taskId) // Running DB query on a background thread
            }
            _taskDetail.value = task
        }
    }

    fun getTasksByStatus(status: Boolean) =
        taskDao.getTasksByStatus(status).stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    // Add a new task
    fun addTask(task: Task) = viewModelScope.launch { taskDao.insertTask(task) }

    // Update an existing task
    fun updateTask(task: Task) = viewModelScope.launch { taskDao.updateTask(task) }

    // Delete task by ID
    fun deleteTask(taskId: Int) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            taskDao.deleteTask(taskId)
        }
    }

    // Mark task as completed
    fun markTaskAsCompleted(taskId: Int) = viewModelScope.launch {
        val task = withContext(Dispatchers.IO) { taskDao.getTaskById(taskId) }
        task?.let {
            val updatedTask = it.copy(isCompleted = true) // Mark task as completed
            taskDao.updateTask(updatedTask) // Update task in DB
        }
    }
}

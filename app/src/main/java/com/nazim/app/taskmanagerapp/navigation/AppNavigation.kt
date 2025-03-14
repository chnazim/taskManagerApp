package com.nazim.app.taskmanagerapp.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.nazim.app.taskmanagerapp.ui.screens.AddTaskScreen
import com.nazim.app.taskmanagerapp.ui.screens.TaskDetailsScreen
import com.nazim.app.taskmanagerapp.ui.screens.TaskListScreen
import com.nazim.app.taskmanagerapp.viewModel.TaskViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val viewModel: TaskViewModel = hiltViewModel()

    NavHost(navController, startDestination = "taskList") {
        composable("taskList") { TaskListScreen(viewModel, navController) }
        composable("addTask") { AddTaskScreen(viewModel, navController) }
        composable(
            route = "taskDetails/{taskId}",
            arguments = listOf(navArgument("taskId") { type = NavType.IntType })
        ) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getInt("taskId")
            TaskDetailsScreen(taskId = taskId, navController = navController)
        }
    }
}

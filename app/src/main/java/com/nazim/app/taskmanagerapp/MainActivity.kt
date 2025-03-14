package com.nazim.app.taskmanagerapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.nazim.app.taskmanagerapp.navigation.AppNavigation
import com.nazim.app.taskmanagerapp.ui.theme.TaskManagerAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            setContent {
                TaskManagerAppTheme {
                    AppNavigation()
                }
            }
        } catch (e: Exception) {
            Log.e("MainActivity", "Initialization failed", e)
        }
    }
}
    
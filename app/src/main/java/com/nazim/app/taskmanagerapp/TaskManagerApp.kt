package com.nazim.app.taskmanagerapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TaskManagerApp : Application(){
    override fun onCreate() {
        super.onCreate()
        // Hilt setup if any
    }
}
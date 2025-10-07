package com.example.taskmanager

import android.app.Application
import android.util.Log
import kotlin.system.exitProcess

class TaskManagerApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        setupExceptionHandler()
    }

    private fun setupExceptionHandler() {
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            Log.e("TaskManager", "Uncaught exception in thread ${thread.name}", throwable)
            // Vous pouvez ajouter ici la logique pour envoyer les rapports d'erreur
            exitProcess(1)
        }
    }
} 
package com.example.taskmanager.data

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.taskmanager.data.dao.TaskDao
import com.example.taskmanager.data.dao.UserDao
import com.example.taskmanager.data.entity.Task
import com.example.taskmanager.data.entity.TaskStatus
import com.example.taskmanager.data.entity.User
import com.example.taskmanager.util.DateConverter
import com.example.taskmanager.util.TaskStatusConverter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [User::class, Task::class], version = 1, exportSchema = false)
@TypeConverters(DateConverter::class, TaskStatusConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun taskDao(): TaskDao

    companion object {
        private const val TAG = "AppDatabase"
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                try {
                    Log.d(TAG, "Initialisation de la base de données")
                    val instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "task_manager_database"
                    )
                    .fallbackToDestructiveMigration()
                    .build()
                    
                    // Vérification de la base de données
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            instance.openHelper.writableDatabase
                            Log.d(TAG, "Base de données vérifiée avec succès")
                        } catch (e: Exception) {
                            Log.e(TAG, "Erreur lors de la vérification de la base de données", e)
                            throw e
                        }
                    }
                    
                    Log.d(TAG, "Base de données initialisée avec succès")
                    INSTANCE = instance
                    instance
                } catch (e: Exception) {
                    Log.e(TAG, "Erreur lors de l'initialisation de la base de données", e)
                    throw RuntimeException("Erreur lors de l'initialisation de la base de données: ${e.message}")
                }
            }
        }
    }
} 
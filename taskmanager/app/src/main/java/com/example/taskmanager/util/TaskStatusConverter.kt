package com.example.taskmanager.util

import androidx.room.TypeConverter
import com.example.taskmanager.data.entity.TaskStatus

class TaskStatusConverter {
    @TypeConverter
    fun fromTaskStatus(status: TaskStatus): String {
        return status.name
    }

    @TypeConverter
    fun toTaskStatus(status: String): TaskStatus {
        return try {
            TaskStatus.valueOf(status)
        } catch (e: IllegalArgumentException) {
            TaskStatus.NOT_STARTED
        }
    }
} 
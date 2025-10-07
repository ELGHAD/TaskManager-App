package com.example.taskmanager.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmanager.R
import com.example.taskmanager.data.entity.Task
import com.example.taskmanager.data.entity.TaskStatus
import com.example.taskmanager.databinding.ItemTaskBinding
import java.text.SimpleDateFormat
import java.util.*

class TaskAdapter(
    private val onTaskChecked: (Task, Boolean) -> Unit,
    private val onTaskEdit: (Task) -> Unit,
    private val onTaskDelete: (Task) -> Unit
) : ListAdapter<Task, TaskAdapter.TaskViewHolder>(TaskDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTaskBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class TaskViewHolder(
        private val binding: ItemTaskBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(task: Task) {
            binding.apply {
                titleTextView.text = task.title
                descriptionTextView.text = task.description
                dueDateTextView.text = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    .format(task.dueDate)
                statusTextView.text = when (task.status) {
                    TaskStatus.NOT_STARTED -> "Non commencée"
                    TaskStatus.IN_PROGRESS -> "En cours"
                    TaskStatus.COMPLETED -> "Terminée"
                }
                checkBox.isChecked = task.isCompleted

                // Mise à jour du style en fonction de l'état de la tâche
                if (task.isCompleted) {
                    titleTextView.setTextColor(ContextCompat.getColor(root.context, R.color.gray))
                    descriptionTextView.setTextColor(ContextCompat.getColor(root.context, R.color.gray))
                    dueDateTextView.setTextColor(ContextCompat.getColor(root.context, R.color.gray))
                    statusTextView.setTextColor(ContextCompat.getColor(root.context, R.color.gray))
                    root.alpha = 0.7f
                } else {
                    titleTextView.setTextColor(ContextCompat.getColor(root.context, R.color.black))
                    descriptionTextView.setTextColor(ContextCompat.getColor(root.context, R.color.dark_gray))
                    dueDateTextView.setTextColor(ContextCompat.getColor(root.context, R.color.primary))
                    statusTextView.setTextColor(ContextCompat.getColor(root.context, R.color.primary))
                    root.alpha = 1.0f
                }

                checkBox.setOnCheckedChangeListener { _, isChecked ->
                    onTaskChecked(task, isChecked)
                }

                editButton.setOnClickListener {
                    onTaskEdit(task)
                }

                deleteButton.setOnClickListener {
                    onTaskDelete(task)
                }
            }
        }
    }

    private class TaskDiffCallback : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem == newItem
        }
    }
} 
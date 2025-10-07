package com.example.taskmanager.ui

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.taskmanager.R
import com.example.taskmanager.data.AppDatabase
import com.example.taskmanager.data.entity.Task
import com.example.taskmanager.data.entity.TaskStatus
import com.example.taskmanager.databinding.ActivityTaskListBinding
import com.example.taskmanager.databinding.DialogAddTaskBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class TaskListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTaskListBinding
    private lateinit var database: AppDatabase
    private lateinit var adapter: TaskAdapter
    private var userId: Long = -1
    private var currentDialog: AlertDialog? = null
    private var currentFilter: TaskFilter = TaskFilter.ALL
    private var currentSort: TaskSort = TaskSort.DUE_DATE_ASC
    private var searchQuery: String = ""

    enum class TaskFilter {
        ALL, NOT_STARTED, IN_PROGRESS, COMPLETED
    }

    enum class TaskSort {
        DUE_DATE_ASC, DUE_DATE_DESC, TITLE_ASC, TITLE_DESC
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        try {
            setSupportActionBar(binding.toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = "Mes tâches"

            userId = intent.getLongExtra("USER_ID", -1)
            if (userId == -1L) {
                Toast.makeText(this, "Erreur: Utilisateur non identifié", Toast.LENGTH_SHORT).show()
                finish()
                return
            }

            database = AppDatabase.getDatabase(this)
            setupRecyclerView()
            observeTasks()

            binding.addTaskFab.setOnClickListener {
                showAddTaskDialog()
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Erreur lors de l'initialisation: ${e.message}", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_task_list, menu)
        
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchQuery = query ?: ""
                observeTasks()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchQuery = newText ?: ""
                observeTasks()
                return true
            }
        })

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                showLogoutDialog()
                true
            }
            R.id.action_filter -> {
                showFilterDialog()
                true
            }
            R.id.action_sort -> {
                showSortDialog()
                true
            }
            R.id.action_logout -> {
                showLogoutDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        showLogoutDialog()
    }

    override fun onDestroy() {
        super.onDestroy()
        currentDialog?.dismiss()
    }

    private fun showLogoutDialog() {
        AlertDialog.Builder(this)
            .setTitle("Déconnexion")
            .setMessage("Voulez-vous vraiment vous déconnecter ?")
            .setPositiveButton("Oui") { _, _ -> finish() }
            .setNegativeButton("Non", null)
            .show()
    }

    private fun setupRecyclerView() {
        try {
            adapter = TaskAdapter(
                onTaskChecked = { task, isCompleted ->
                    lifecycleScope.launch {
                        try {
                            database.taskDao().update(task.copy(isCompleted = isCompleted))
                        } catch (e: Exception) {
                            Toast.makeText(this@TaskListActivity, "Erreur lors de la mise à jour de la tâche", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                onTaskEdit = { task ->
                    showEditTaskDialog(task)
                },
                onTaskDelete = { task ->
                    showDeleteTaskDialog(task)
                }
            )
            binding.tasksRecyclerView.apply {
                layoutManager = LinearLayoutManager(this@TaskListActivity)
                adapter = this@TaskListActivity.adapter
                addItemDecoration(androidx.recyclerview.widget.DividerItemDecoration(context, androidx.recyclerview.widget.DividerItemDecoration.VERTICAL))
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Erreur lors de la configuration de la liste: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun observeTasks() {
        lifecycleScope.launch {
            try {
                database.taskDao().getTasksByUser(userId).collect { tasks ->
                    try {
                        val filteredTasks = tasks.filter { task ->
                            when (currentFilter) {
                                TaskFilter.ALL -> true
                                TaskFilter.NOT_STARTED -> task.status == TaskStatus.NOT_STARTED
                                TaskFilter.IN_PROGRESS -> task.status == TaskStatus.IN_PROGRESS
                                TaskFilter.COMPLETED -> task.status == TaskStatus.COMPLETED
                            }
                        }.filter { task ->
                            if (searchQuery.isEmpty()) true
                            else task.title.contains(searchQuery, ignoreCase = true) ||
                                 task.description.contains(searchQuery, ignoreCase = true)
                        }.sortedWith { task1, task2 ->
                            when (currentSort) {
                                TaskSort.DUE_DATE_ASC -> task1.dueDate.compareTo(task2.dueDate)
                                TaskSort.DUE_DATE_DESC -> task2.dueDate.compareTo(task1.dueDate)
                                TaskSort.TITLE_ASC -> task1.title.compareTo(task2.title)
                                TaskSort.TITLE_DESC -> task2.title.compareTo(task1.title)
                            }
                        }

                        adapter.submitList(filteredTasks)
                        binding.emptyStateLayout.visibility = if (filteredTasks.isEmpty()) android.view.View.VISIBLE else android.view.View.GONE
                    } catch (e: Exception) {
                        Toast.makeText(this@TaskListActivity, "Erreur lors de l'affichage des tâches", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(this@TaskListActivity, "Erreur lors du chargement des tâches", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showAddTaskDialog() {
        showTaskDialog(null)
    }

    private fun showEditTaskDialog(task: Task) {
        showTaskDialog(task)
    }

    private fun showTaskDialog(task: Task?) {
        try {
            val dialogBinding = DialogAddTaskBinding.inflate(layoutInflater)
            var selectedDate = task?.dueDate ?: Calendar.getInstance().time

            // Set initial values if editing
            task?.let {
                dialogBinding.titleEditText.setText(it.title)
                dialogBinding.descriptionEditText.setText(it.description)
            }

            // Setup status spinner
            val statusAdapter = ArrayAdapter(
                this,
                android.R.layout.simple_dropdown_item_1line,
                TaskStatus.values().map { status ->
                    when (status) {
                        TaskStatus.NOT_STARTED -> "Non commencée"
                        TaskStatus.IN_PROGRESS -> "En cours"
                        TaskStatus.COMPLETED -> "Terminée"
                    }
                }
            )
            dialogBinding.statusSpinner.setAdapter(statusAdapter)
            dialogBinding.statusSpinner.setText("Non commencée", false)
            task?.let {
                dialogBinding.statusSpinner.setText(
                    when (it.status) {
                        TaskStatus.NOT_STARTED -> "Non commencée"
                        TaskStatus.IN_PROGRESS -> "En cours"
                        TaskStatus.COMPLETED -> "Terminée"
                    },
                    false
                )
            }

            // Set initial date
            dialogBinding.dateButton.text = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                .format(selectedDate)

            dialogBinding.dateButton.setOnClickListener {
                val calendar = Calendar.getInstance()
                calendar.time = selectedDate
                
                DatePickerDialog(
                    this,
                    { _, year, month, day ->
                        calendar.set(year, month, day)
                        selectedDate = calendar.time
                        dialogBinding.dateButton.text = SimpleDateFormat(
                            "dd/MM/yyyy",
                            Locale.getDefault()
                        ).format(selectedDate)
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                ).show()
            }

            currentDialog = AlertDialog.Builder(this)
                .setTitle(if (task == null) "Nouvelle tâche" else "Modifier la tâche")
                .setView(dialogBinding.root)
                .setPositiveButton(if (task == null) "Ajouter" else "Modifier", null)
                .setNegativeButton("Annuler", null)
                .create()

            currentDialog?.setOnShowListener {
                val positiveButton = currentDialog?.getButton(AlertDialog.BUTTON_POSITIVE)
                positiveButton?.setOnClickListener {
                    val title = dialogBinding.titleEditText.text.toString()
                    val description = dialogBinding.descriptionEditText.text.toString()
                    val statusText = dialogBinding.statusSpinner.text.toString()
                    val status = when (statusText) {
                        "Non commencée" -> TaskStatus.NOT_STARTED
                        "En cours" -> TaskStatus.IN_PROGRESS
                        "Terminée" -> TaskStatus.COMPLETED
                        else -> TaskStatus.NOT_STARTED
                    }

                    if (title.isBlank()) {
                        dialogBinding.titleEditText.error = "Le titre est obligatoire"
                        return@setOnClickListener
                    }

                    lifecycleScope.launch {
                        try {
                            if (task == null) {
                                // Create new task
                                val newTask = Task(
                                    title = title,
                                    description = description,
                                    dueDate = selectedDate,
                                    status = status,
                                    userId = userId,
                                    isCompleted = false
                                )
                                database.taskDao().insert(newTask)
                                Toast.makeText(this@TaskListActivity, "Tâche ajoutée", Toast.LENGTH_SHORT).show()
                            } else {
                                // Update existing task
                                database.taskDao().update(
                                    task.copy(
                                        title = title,
                                        description = description,
                                        dueDate = selectedDate,
                                        status = status,
                                        isCompleted = task.isCompleted
                                    )
                                )
                                Toast.makeText(this@TaskListActivity, "Tâche modifiée", Toast.LENGTH_SHORT).show()
                            }
                            currentDialog?.dismiss()
                        } catch (e: Exception) {
                            Toast.makeText(
                                this@TaskListActivity,
                                if (task == null) "Erreur lors de l'ajout de la tâche" else "Erreur lors de la modification de la tâche",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }

            currentDialog?.show()
        } catch (e: Exception) {
            Toast.makeText(this, "Erreur lors de l'affichage du dialogue: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun showDeleteTaskDialog(task: Task) {
        try {
            AlertDialog.Builder(this)
                .setTitle("Supprimer la tâche")
                .setMessage("Êtes-vous sûr de vouloir supprimer cette tâche ?")
                .setPositiveButton("Supprimer") { _, _ ->
                    lifecycleScope.launch {
                        try {
                            database.taskDao().delete(task)
                            Toast.makeText(this@TaskListActivity, "Tâche supprimée", Toast.LENGTH_SHORT).show()
                        } catch (e: Exception) {
                            Toast.makeText(this@TaskListActivity, "Erreur lors de la suppression de la tâche", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                .setNegativeButton("Annuler", null)
                .show()
        } catch (e: Exception) {
            Toast.makeText(this, "Erreur lors de l'affichage du dialogue de suppression: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun showFilterDialog() {
        val options = arrayOf("Toutes", "Non commencées", "En cours", "Terminées")
        val currentSelection = when (currentFilter) {
            TaskFilter.ALL -> 0
            TaskFilter.NOT_STARTED -> 1
            TaskFilter.IN_PROGRESS -> 2
            TaskFilter.COMPLETED -> 3
        }

        AlertDialog.Builder(this)
            .setTitle("Filtrer les tâches")
            .setSingleChoiceItems(options, currentSelection) { _, which ->
                currentFilter = when (which) {
                    0 -> TaskFilter.ALL
                    1 -> TaskFilter.NOT_STARTED
                    2 -> TaskFilter.IN_PROGRESS
                    3 -> TaskFilter.COMPLETED
                    else -> TaskFilter.ALL
                }
                observeTasks()
            }
            .setPositiveButton("OK", null)
            .show()
    }

    private fun showSortDialog() {
        val options = arrayOf(
            "Date d'échéance (croissant)",
            "Date d'échéance (décroissant)",
            "Titre (A-Z)",
            "Titre (Z-A)"
        )
        val currentSelection = when (currentSort) {
            TaskSort.DUE_DATE_ASC -> 0
            TaskSort.DUE_DATE_DESC -> 1
            TaskSort.TITLE_ASC -> 2
            TaskSort.TITLE_DESC -> 3
        }

        AlertDialog.Builder(this)
            .setTitle("Trier les tâches")
            .setSingleChoiceItems(options, currentSelection) { _, which ->
                currentSort = when (which) {
                    0 -> TaskSort.DUE_DATE_ASC
                    1 -> TaskSort.DUE_DATE_DESC
                    2 -> TaskSort.TITLE_ASC
                    3 -> TaskSort.TITLE_DESC
                    else -> TaskSort.DUE_DATE_ASC
                }
                observeTasks()
            }
            .setPositiveButton("OK", null)
            .show()
    }
} 
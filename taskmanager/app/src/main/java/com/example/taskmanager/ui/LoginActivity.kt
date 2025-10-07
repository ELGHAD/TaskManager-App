package com.example.taskmanager.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.taskmanager.data.AppDatabase
import com.example.taskmanager.databinding.ActivityLoginBinding
import com.example.taskmanager.util.PasswordUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        try {
            database = AppDatabase.getDatabase(this)
            Log.d("LoginActivity", "Base de données initialisée avec succès")
        } catch (e: Exception) {
            Log.e("LoginActivity", "Erreur d'initialisation de la base de données", e)
            Toast.makeText(this, "Erreur d'initialisation de la base de données: ${e.message}", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.loginButton.setOnClickListener {
            val username = binding.usernameEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()

            if (!validateInputs(username, password)) {
                return@setOnClickListener
            }

            disableButtons()
            attemptLogin(username, password)
        }

        binding.registerButton.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun validateInputs(username: String, password: String): Boolean {
        if (username.isBlank() || password.isBlank()) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun disableButtons() {
        binding.loginButton.isEnabled = false
        binding.registerButton.isEnabled = false
    }

    private fun enableButtons() {
        binding.loginButton.isEnabled = true
        binding.registerButton.isEnabled = true
    }

    private fun attemptLogin(username: String, password: String) {
        lifecycleScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    Log.d("LoginActivity", "Tentative de connexion pour l'utilisateur: $username")
                    val user = database.userDao().getUserByUsername(username)
                    
                    if (user == null) {
                        Log.d("LoginActivity", "Utilisateur non trouvé: $username")
                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@LoginActivity, "Identifiants incorrects", Toast.LENGTH_SHORT).show()
                            enableButtons()
                        }
                        return@withContext
                    }

                    if (PasswordUtils.verifyPassword(password, user.password)) {
                        Log.d("LoginActivity", "Connexion réussie pour l'utilisateur: $username")
                        withContext(Dispatchers.Main) {
                            startActivity(Intent(this@LoginActivity, TaskListActivity::class.java).apply {
                                putExtra("USER_ID", user.id)
                                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            })
                            finish()
                        }
                    } else {
                        Log.d("LoginActivity", "Mot de passe incorrect pour l'utilisateur: $username")
                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@LoginActivity, "Identifiants incorrects", Toast.LENGTH_SHORT).show()
                            enableButtons()
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("LoginActivity", "Erreur lors de la connexion", e)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@LoginActivity, "Erreur de connexion: ${e.message}", Toast.LENGTH_SHORT).show()
                    enableButtons()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        enableButtons()
    }
} 
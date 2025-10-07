package com.example.taskmanager.ui

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.taskmanager.data.AppDatabase
import com.example.taskmanager.data.entity.User
import com.example.taskmanager.databinding.ActivityRegisterBinding
import com.example.taskmanager.util.PasswordUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        try {
            database = AppDatabase.getDatabase(this)
            Log.d("RegisterActivity", "Base de données initialisée avec succès")
        } catch (e: Exception) {
            Log.e("RegisterActivity", "Erreur d'initialisation de la base de données", e)
            Toast.makeText(this, "Erreur d'initialisation de la base de données: ${e.message}", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        binding.registerButton.setOnClickListener {
            val username = binding.usernameEditText.text.toString().trim()
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()
            val confirmPassword = binding.confirmPasswordEditText.text.toString().trim()

            if (!validateInputs(username, email, password, confirmPassword)) {
                return@setOnClickListener
            }

            binding.registerButton.isEnabled = false

            lifecycleScope.launch {
                try {
                    withContext(Dispatchers.IO) {
                        Log.d("RegisterActivity", "Vérification de l'existence de l'utilisateur: $username")
                        if (database.userDao().getUserByUsername(username) != null) {
                            Log.d("RegisterActivity", "Nom d'utilisateur déjà pris: $username")
                            withContext(Dispatchers.Main) {
                                Toast.makeText(this@RegisterActivity, "Ce nom d'utilisateur est déjà pris", Toast.LENGTH_SHORT).show()
                                binding.registerButton.isEnabled = true
                            }
                            return@withContext
                        }

                        Log.d("RegisterActivity", "Vérification de l'existence de l'email: $email")
                        if (database.userDao().getUserByEmail(email) != null) {
                            Log.d("RegisterActivity", "Email déjà utilisé: $email")
                            withContext(Dispatchers.Main) {
                                Toast.makeText(this@RegisterActivity, "Cet email est déjà utilisé", Toast.LENGTH_SHORT).show()
                                binding.registerButton.isEnabled = true
                            }
                            return@withContext
                        }

                        val hashedPassword = PasswordUtils.hashPassword(password)
                        val user = User(
                            username = username,
                            email = email,
                            password = hashedPassword
                        )

                        Log.d("RegisterActivity", "Tentative d'inscription de l'utilisateur: $username")
                        val userId = database.userDao().insert(user)
                        Log.d("RegisterActivity", "Inscription réussie pour l'utilisateur: $username (ID: $userId)")
                        
                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@RegisterActivity, "Inscription réussie", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    }
                } catch (e: Exception) {
                    Log.e("RegisterActivity", "Erreur lors de l'inscription", e)
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@RegisterActivity, "Erreur lors de l'inscription: ${e.message}", Toast.LENGTH_SHORT).show()
                        binding.registerButton.isEnabled = true
                    }
                }
            }
        }
    }

    private fun validateInputs(username: String, email: String, password: String, confirmPassword: String): Boolean {
        if (username.isBlank() || email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()
            return false
        }

        if (username.length < 3) {
            Toast.makeText(this, "Le nom d'utilisateur doit contenir au moins 3 caractères", Toast.LENGTH_SHORT).show()
            return false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Veuillez entrer une adresse email valide", Toast.LENGTH_SHORT).show()
            return false
        }

        if (password.length < 6) {
            Toast.makeText(this, "Le mot de passe doit contenir au moins 6 caractères", Toast.LENGTH_SHORT).show()
            return false
        }

        if (password != confirmPassword) {
            Toast.makeText(this, "Les mots de passe ne correspondent pas", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    override fun onResume() {
        super.onResume()
        binding.registerButton.isEnabled = true
    }
} 
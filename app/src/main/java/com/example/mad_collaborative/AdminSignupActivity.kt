package com.example.mad_collaborative

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mad_collaborative.databinding.AdminSignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AdminSignupActivity : AppCompatActivity() {

    private lateinit var binding: AdminSignupBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AdminSignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        binding.btnCreateAdmin.setOnClickListener {
            val username = binding.etAdminUsername.text.toString().trim()
            val email = binding.etAdminEmail.text.toString().trim()
            val password = binding.etAdminPassword.text.toString().trim()
            val confirmPassword = binding.etConfirmPassword.text.toString().trim()

            if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!isValidPassword(password)) {
                Toast.makeText(
                    this,
                    "Invalid password: Must be 12+ characters, include 1 upper, 1 lower, 1 special, and no invalid symbols",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(this, "Passwords do not match!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            createAdminAccount(username, email, password)
        }

        binding.BackToAdmin.setOnClickListener {
            finish()
        }
    }

    private fun createAdminAccount(username: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { authResult ->
                val adminId = authResult.user?.uid ?: return@addOnSuccessListener
                val adminData = hashMapOf(
                    "username" to username,
                    "email" to email,
                    "role" to "Admin"
                )

                firestore.collection("users").document(adminId)
                    .set(adminData)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Admin created successfully!", Toast.LENGTH_SHORT)
                            .show()

                        val intent = Intent(this, LoginActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Failed to save admin data", Toast.LENGTH_SHORT).show()
                    }
            }
    }
    private fun isValidPassword(password: String): Boolean {
        val passwordRegex = """^(?=.*[A-Z])(?=.*[a-z])(?=.*\d)(?=.*[@#$%&!])[A-Za-z\d@#$%&!]{12,}$""".toRegex()
        val forbiddenChars = listOf('^', '\'', '<', '>', '/')

        return password.matches(passwordRegex) && forbiddenChars.none { password.contains(it) }
    }
}

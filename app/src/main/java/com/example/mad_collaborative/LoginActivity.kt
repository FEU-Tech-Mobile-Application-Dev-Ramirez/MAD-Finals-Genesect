package com.example.mad_collaborative

import com.example.mad_collaborative.databinding.LoginpageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge

class LoginActivity : ComponentActivity() {

    private lateinit var binding: LoginpageBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginpageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        binding.userCheckbox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) binding.organizerCheckbox.isChecked = false
        }

        binding.organizerCheckbox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) binding.userCheckbox.isChecked = false
        }

        binding.signupButton.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }

        binding.loginButton.setOnClickListener {
            val email = binding.emailInput.text.toString().trim()
            val password = binding.passwordInput.text.toString().trim()
            val isUserChecked = binding.userCheckbox.isChecked
            val isOrganizerChecked = binding.organizerCheckbox.isChecked

            if (email.isEmpty() || password.isEmpty()) {
                showToast("Please enter both email and password")
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    showToast("Login failed. Check your credentials")
                } else {
                    val userId = auth.currentUser?.uid
                    if (userId == null) {
                        showToast("User ID not found")
                        return@addOnCompleteListener
                    }

                    db.collection("users").document(userId).get().addOnCompleteListener { docTask ->
                        if (!docTask.isSuccessful || docTask.result == null || !docTask.result.exists()) {
                            showToast("User not found in database")
                        } else {
                            val role = docTask.result.getString("role") ?: ""

                            if (role == "Admin") {
                                navigateTo(AdminHomeActivity::class.java)
                            } else {
                                if (!isUserChecked && !isOrganizerChecked) {
                                    showToast("Please select a role")
                                    return@addOnCompleteListener
                                }

                                val selectedRole = if (isUserChecked) "User" else "Organizer"

                                if (selectedRole == role) {
                                    val targetActivity = when (selectedRole) {
                                        "User" -> UserHomeActivity::class.java
                                        "Organizer" -> OrganizerHomeActivity::class.java
                                        else -> null
                                    }
                                    targetActivity?.let { navigateTo(it) }
                                } else {
                                    showToast("Role mismatch. Please select the correct role")
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun navigateTo(activityClass: Class<*>) {
        startActivity(Intent(this, activityClass))
        finish()
    }
}


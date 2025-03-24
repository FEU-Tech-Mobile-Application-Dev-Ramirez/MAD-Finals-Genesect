package com.example.mad_collaborative

import android.content.Intent
import android.widget.Toast
import android.os.Bundle
import android.util.Patterns
import com.example.mad_collaborative.databinding.SignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge

class SignupActivity : ComponentActivity() {

    private lateinit var binding: SignupBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        binding.gotologin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.userCheckbox2.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) binding.organizerCheckbox2.isChecked = false
        }

        binding.organizerCheckbox2.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) binding.userCheckbox2.isChecked = false
        }

        binding.signupButton.setOnClickListener {
            val email = binding.email.text.toString().trim()
            val password = binding.password.text.toString().trim()
            val confirmPassword = binding.confirmPassword.text.toString().trim()
            val username = binding.username.text.toString().trim()

            if (email.isEmpty() || password.isEmpty() || username.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Invalid email format", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val role = when {
                binding.userCheckbox2.isChecked -> "User"
                binding.organizerCheckbox2.isChecked -> "Organizer"
                else -> {
                    Toast.makeText(this, "Select User or Organizer", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }

            if (!isValidPassword(password)) {
                Toast.makeText(this, "Invalid password: Must be 12+ characters, include 1 uppercase, 1 lowercase, 1 special character (@#\$%&!), and no invalid symbols (^'<>/)", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener { user ->
                    val userData = hashMapOf(
                        "email" to email,
                        "username" to username,
                        "role" to role
                    )

                    db.collection("users").document(user.user!!.uid).set(userData)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Signup successful", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, LoginActivity::class.java))
                            finish()
                        }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Signup failed", Toast.LENGTH_SHORT).show()
                }
        }
    }



    private fun isValidPassword(password: String): Boolean {
        val passwordRegex = """^(?=.*[A-Z])(?=.*[a-z])(?=.*\d)(?=.*[@#$%&!])[A-Za-z\d@#$%&!]{12,}$""".toRegex()
        val forbiddenChars = listOf('^', '\'', '<', '>', '/')

        return password.matches(passwordRegex) && forbiddenChars.none { password.contains(it) }
    }
}

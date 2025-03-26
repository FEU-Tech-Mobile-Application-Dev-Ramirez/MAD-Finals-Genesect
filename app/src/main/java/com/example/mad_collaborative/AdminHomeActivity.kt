package com.example.mad_collaborative

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mad_collaborative.databinding.AdminHomeBinding
import com.google.firebase.firestore.FirebaseFirestore

class AdminHomeActivity : ComponentActivity() {

    private lateinit var binding: AdminHomeBinding
    private lateinit var adminAdapter: AdminAdapter
    private val users = mutableListOf<UserModel>()
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AdminHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        loadUsers()

        binding.btnAdminSignUp.setOnClickListener {
            startActivity(Intent(this, AdminSignupActivity::class.java))
        }
    }

    private fun setupRecyclerView() {
        adminAdapter = AdminAdapter(users, firestore)
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@AdminHomeActivity)
            adapter = adminAdapter
        }
    }

    private fun loadUsers() {
        firestore.collection("users").get()
            .addOnSuccessListener { documents ->
                users.clear()
                for (document in documents) {
                    val user = UserModel(
                        userId = document.id,
                        username = document.getString("username") ?: "",
                        email = document.getString("email") ?: "",
                        role = document.getString("role") ?: ""
                    )
                    users.add(user)
                }
                adminAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to load users", Toast.LENGTH_SHORT).show()
            }
    }
}

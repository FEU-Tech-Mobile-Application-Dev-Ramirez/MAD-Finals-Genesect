package com.example.mad_collaborative

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mad_collaborative.databinding.UserHomeBinding
import com.google.firebase.firestore.FirebaseFirestore
import android.content.Intent
import com.google.firebase.auth.FirebaseAuth




class UserHomeActivity : ComponentActivity() {
    private lateinit var binding: UserHomeBinding
    private lateinit var userEventAdapter: UserEventAdapter
    private val events = mutableListOf<EventModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = UserHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.recyclerView.visibility = View.VISIBLE
        binding.newsSection.visibility = View.GONE


        binding.btnNews.setOnClickListener {
            binding.recyclerView.visibility = View.GONE
            binding.newsSection.visibility = View.VISIBLE
        }


        binding.btnTournaments.setOnClickListener {
            binding.newsSection.visibility = View.GONE
            binding.recyclerView.visibility = View.VISIBLE
        }

        val auth = FirebaseAuth.getInstance()
        val firestore = FirebaseFirestore.getInstance()
        val currentUser = auth.currentUser

        if (currentUser != null) {
            firestore.collection("users").document(currentUser.uid).get()
                .addOnSuccessListener { document ->
                    val currentUsername = document.getString("username") ?: "Unknown"
                    val currentUserRole = document.getString("role") ?: "Unknown"
                    val currentUserEmail = document.getString("email") ?: "Unknown"

                    binding.tvUsername.text = currentUsername

                    binding.btnMenu.setOnClickListener {
                        val intent = Intent(this, MenuActivity::class.java)
                        intent.putExtra("username", currentUsername)
                        intent.putExtra("role", currentUserRole)
                        intent.putExtra("email", currentUserEmail)
                        startActivity(intent)
                    }
                }
        }


        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        userEventAdapter = UserEventAdapter(events, this)
        binding.recyclerView.adapter = userEventAdapter
        loadEventsFromFirestore()
    }

    private fun loadEventsFromFirestore() {
        val firestore = FirebaseFirestore.getInstance()
        firestore.collection("events")
            .whereEqualTo("isCreated", true)
            .get()
            .addOnSuccessListener { documents ->
                events.clear()
                for (document in documents) {
                    val event = document.toObject(EventModel::class.java)
                    event.id = document.id
                    events.add(event)
                }
                userEventAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to load events", Toast.LENGTH_SHORT).show()
            }
    }
}


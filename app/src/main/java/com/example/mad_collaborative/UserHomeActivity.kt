package com.example.mad_collaborative

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mad_collaborative.databinding.UserHomeBinding
import com.google.firebase.firestore.FirebaseFirestore

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


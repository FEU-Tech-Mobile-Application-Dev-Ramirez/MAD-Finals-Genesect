package com.example.mad_collaborative

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mad_collaborative.databinding.ActivityParticipantsBinding
import com.google.firebase.firestore.FirebaseFirestore

class ParticipantsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityParticipantsBinding
    private lateinit var participantsAdapter: ParticipantsAdapter
    private val firestore = FirebaseFirestore.getInstance()
    private val eventsWithParticipants = mutableListOf<Pair<String, List<String>>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityParticipantsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerViewParticipants.layoutManager = LinearLayoutManager(this)

        loadParticipants()
    }

    private fun loadParticipants() {
        firestore.collection("events").get().addOnSuccessListener { documents ->
            eventsWithParticipants.clear()
            for (document in documents) {
                val eventName = document.getString("eventName") ?: "Unknown Event"
                val participants = document.get("participants") as? List<String> ?: emptyList()
                eventsWithParticipants.add(Pair(eventName, participants))
            }

            participantsAdapter = ParticipantsAdapter(eventsWithParticipants)
            binding.recyclerViewParticipants.adapter = participantsAdapter
        }.addOnFailureListener {
            Toast.makeText(this, "Failed to load participants", Toast.LENGTH_SHORT).show()
        }
    }
}



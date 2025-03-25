package com.example.mad_collaborative

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mad_collaborative.databinding.OrganizerHomeBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class OrganizerHomeActivity : AppCompatActivity() {
    private lateinit var binding: OrganizerHomeBinding
    private lateinit var eventAdapter: EventAdapter
    private val events = mutableListOf<EventModel>()
    private var selectedEventPosition: Int = -1
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = OrganizerHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        eventAdapter = EventAdapter(events, this)
        binding.recyclerView.adapter = eventAdapter

        binding.fabAddEvent.setOnClickListener {
            addNewEvent()
        }

        loadEventsFromFirestore()
    }

    private fun addNewEvent() {
        val newEvent = EventModel()
        events.add(newEvent)
        eventAdapter.notifyItemInserted(events.size - 1)
    }

    private fun loadEventsFromFirestore() {
        firestore.collection("events").get()
            .addOnSuccessListener { documents ->
                events.clear()
                for (document in documents) {
                    val event = document.toObject(EventModel::class.java)
                    event.id = document.id
                    events.add(event)
                }
                eventAdapter.notifyItemInserted(events.size - 1)
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to load events", Toast.LENGTH_SHORT).show()
            }
    }

    fun openImagePicker(position: Int) {
        selectedEventPosition = position
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        imagePickerLauncher.launch(intent)
    }

    private val imagePickerLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                val imageUri: Uri? = result.data?.data
                if (imageUri != null && selectedEventPosition != -1) {
                    uploadImageToFirebase(imageUri, selectedEventPosition)
                }
            }
        }

    private fun uploadImageToFirebase(imageUri: Uri, position: Int) {
        val storageRef = FirebaseStorage.getInstance().reference.child("event_images/${UUID.randomUUID()}.jpg")

        storageRef.putFile(imageUri)
            .addOnSuccessListener { taskSnapshot ->
                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    events[position].imageUrl = uri.toString()
                    eventAdapter.notifyItemChanged(position)

                    Toast.makeText(this, "Image uploaded. Press 'Create' to save it.", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Image upload failed", Toast.LENGTH_SHORT).show()
            }
    }

}


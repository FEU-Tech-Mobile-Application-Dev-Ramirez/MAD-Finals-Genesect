package com.example.mad_collaborative

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mad_collaborative.databinding.ItemEventBinding
import com.google.firebase.firestore.FirebaseFirestore

class EventAdapter(private val events: MutableList<EventModel>, private val activity: OrganizerHomeActivity) :
    RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    class EventViewHolder(val binding: ItemEventBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding = ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = events[position]
        val binding = holder.binding

        binding.etDate.setText(event.date)
        binding.etTime.setText(event.time)
        binding.etLocation.setText(event.location)

        if (!event.imageUrl.isNullOrEmpty()) {
            Glide.with(binding.root.context)
                .load(event.imageUrl)
                .into(binding.imgEvent)

            binding.imgEvent.visibility = View.VISIBLE
            binding.btnAddImage.visibility = View.GONE
        } else {
            binding.imgEvent.visibility = View.GONE
            binding.btnAddImage.visibility = View.VISIBLE
        }

        val isCreated = event.isCreated
        binding.etDate.isEnabled = !isCreated
        binding.etTime.isEnabled = !isCreated
        binding.etLocation.isEnabled = !isCreated
        binding.btnAddImage.isEnabled = !isCreated

        binding.btnJoin1.text = if (isCreated) "Created" else "Create"
        binding.btnJoin1.isEnabled = !isCreated
        if (isCreated) {
            binding.btnJoin1.setBackgroundColor(Color.GRAY)
        }

        binding.btnAddImage.setOnClickListener {
            activity.openImagePicker(position)
        }

        binding.btnJoin1.setOnClickListener {
            val date = binding.etDate.text.toString()
            val time = binding.etTime.text.toString()
            val location = binding.etLocation.text.toString()
            val imageUrl = event.imageUrl

            if (date.isEmpty() || time.isEmpty() || location.isEmpty() || imageUrl.isNullOrEmpty()) {
                Toast.makeText(binding.root.context, "Please fill all fields and select an image", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            assignEventNameAndUpload(event, date, time, location, imageUrl, binding)
        }
    }

    private fun assignEventNameAndUpload(
        event: EventModel,
        date: String,
        time: String,
        location: String,
        imageUrl: String?,
        binding: ItemEventBinding
    ) {
        val firestore = FirebaseFirestore.getInstance()


        firestore.collection("events").get()
            .addOnSuccessListener { documents ->
                val eventCount = documents.size() + 1
                val eventName = eventCount.toString()

                val eventData = hashMapOf(
                    "eventName" to eventName,
                    "date" to date,
                    "time" to time,
                    "location" to location,
                    "imageUrl" to imageUrl,
                    "isCreated" to true
                )

                firestore.collection("events").add(eventData)
                    .addOnSuccessListener { documentReference ->
                        event.id = documentReference.id
                        event.isCreated = true
                        event.eventName = eventName

                        binding.etDate.isEnabled = false
                        binding.etTime.isEnabled = false
                        binding.etLocation.isEnabled = false
                        binding.btnAddImage.isEnabled = false

                        binding.btnJoin1.text = "Created"
                        binding.btnJoin1.isEnabled = false
                        binding.btnJoin1.setBackgroundColor(Color.GRAY)

                        Toast.makeText(binding.root.context, "Event saved successfully!", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(binding.root.context, "Failed to save event", Toast.LENGTH_SHORT).show()
                    }
            }
            .addOnFailureListener {
                Toast.makeText(binding.root.context, "Failed to get event count", Toast.LENGTH_SHORT).show()
            }
    }

    override fun getItemCount(): Int = events.size
}

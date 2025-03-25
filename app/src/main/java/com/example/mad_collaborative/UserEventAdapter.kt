package com.example.mad_collaborative

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mad_collaborative.databinding.ItemEventBinding
import com.google.firebase.firestore.FirebaseFirestore
import android.view.View

class UserEventAdapter(private val events: List<EventModel>, private val activity: UserHomeActivity) :
    RecyclerView.Adapter<UserEventAdapter.EventViewHolder>() {

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
            Glide.with(holder.itemView.context)
                .load(event.imageUrl)
                .into(binding.imgEvent)

            binding.imgEvent.visibility = View.VISIBLE
        } else {
            binding.imgEvent.visibility = View.GONE
        }

        binding.etDate.isEnabled = false
        binding.etTime.isEnabled = false
        binding.etLocation.isEnabled = false
        binding.btnAddImage.visibility = View.GONE
        binding.btnJoin1.text = if (event.isJoined) "Joined" else "Join"
        binding.btnJoin1.isEnabled = !event.isJoined

        binding.btnJoin1.setOnClickListener {
            joinEvent(event, binding)
        }
    }

    private fun joinEvent(event: EventModel, binding: ItemEventBinding) {
        val firestore = FirebaseFirestore.getInstance()
        firestore.collection("events").document(event.id!!)
            .update("isJoined", true)
            .addOnSuccessListener {
                event.isJoined = true
                binding.btnJoin1.text = "Joined"
                binding.btnJoin1.isEnabled = false
                binding.btnJoin1.setBackgroundColor(Color.GRAY)
                Toast.makeText(binding.root.context, "Joined Event!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(binding.root.context, "Failed to join event", Toast.LENGTH_SHORT).show()
            }
    }

    override fun getItemCount(): Int = events.size
}


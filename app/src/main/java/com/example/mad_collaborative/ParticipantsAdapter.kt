package com.example.mad_collaborative

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mad_collaborative.databinding.ItemParticipantsBinding

class ParticipantsAdapter(private val eventsWithParticipants: List<Pair<String, List<String>>>) :
    RecyclerView.Adapter<ParticipantsAdapter.ParticipantsViewHolder>() {

    class ParticipantsViewHolder(val binding: ItemParticipantsBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParticipantsViewHolder {
        val binding = ItemParticipantsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ParticipantsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ParticipantsViewHolder, position: Int) {
        val (eventName, participants) = eventsWithParticipants[position]
        holder.binding.tvEventName.text = "Event: $eventName"
        holder.binding.tvParticipants.text = if (participants.isNotEmpty()) {
            "Participants: \n" + participants.joinToString("\n")
        } else {
            "No participants yet"
        }
    }

    override fun getItemCount(): Int = eventsWithParticipants.size
}

package com.example.mad_collaborative

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.mad_collaborative.databinding.ItemAdminBinding
import com.google.firebase.firestore.FirebaseFirestore

class AdminAdapter(private val users: MutableList<UserModel>, private val firestore: FirebaseFirestore) :
    RecyclerView.Adapter<AdminAdapter.AdminViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminViewHolder {
        val binding = ItemAdminBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AdminViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AdminViewHolder, position: Int) {
        holder.bind(users[position])
    }

    override fun getItemCount(): Int = users.size

    inner class AdminViewHolder(private val binding: ItemAdminBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(user: UserModel) {
            binding.etUsername.setText(user.username)
            binding.etRole.setText(user.role)

            binding.etUsername.isEnabled = false
            binding.etRole.isEnabled = false
            binding.btnSave.visibility = View.GONE

            binding.btnEdit.setOnClickListener {
                binding.etUsername.isEnabled = true
                binding.etRole.isEnabled = true

                binding.btnSave.visibility = View.VISIBLE
                binding.btnEdit.visibility = View.GONE
            }

            binding.btnSave.setOnClickListener {
                val newUsername = binding.etUsername.text.toString().trim()
                val newRole = binding.etRole.text.toString().trim()
                val userId = user.userId
                if (userId.isNotEmpty()) {
                    val userRef = firestore.collection("users").document(userId)
                    userRef.update("username", newUsername, "role", newRole)
                        .addOnSuccessListener {
                            Toast.makeText(binding.root.context, "User updated", Toast.LENGTH_SHORT).show()

                            binding.etUsername.isEnabled = false
                            binding.etRole.isEnabled = false

                            binding.btnSave.visibility = View.GONE
                            binding.btnEdit.visibility = View.VISIBLE
                        }
                        .addOnFailureListener {
                            Toast.makeText(binding.root.context, "Update failed", Toast.LENGTH_SHORT).show()
                        }
                }
            }

            binding.btnDelete.setOnClickListener {
                val userId = user.userId
                if (userId.isNotEmpty()) {
                    firestore.collection("users").document(userId).delete()
                        .addOnSuccessListener {
                            Toast.makeText(binding.root.context, "User deleted", Toast.LENGTH_SHORT).show()
                            users.removeAt(adapterPosition)
                            notifyItemRemoved(adapterPosition)
                        }
                        .addOnFailureListener {
                            Toast.makeText(binding.root.context, "Delete failed", Toast.LENGTH_SHORT).show()
                        }
                }
            }
        }
    }
}

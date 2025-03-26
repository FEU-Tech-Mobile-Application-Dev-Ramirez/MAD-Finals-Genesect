package com.example.mad_collaborative

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import com.example.mad_collaborative.databinding.FragmentMenuBinding
import com.google.firebase.auth.FirebaseAuth

class MenuActivity : ComponentActivity() {

    private lateinit var binding: FragmentMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = intent.getStringExtra("username") ?: "User"
        val role = intent.getStringExtra("role") ?: "Role"
        val email = intent.getStringExtra("email") ?: "Email"

        binding.tvUsername.text = username
        binding.tvRole.text = role
        binding.tvEmail.text = "Email: $email"

        binding.btnClose.setOnClickListener {
            finish()
        }

        binding.tvProfile.setOnClickListener {
            if (binding.profileDetails.visibility == View.VISIBLE) {
                binding.profileDetails.visibility = View.GONE
            } else {
                binding.profileDetails.visibility = View.VISIBLE
            }
        }

        binding.tvAbout.setOnClickListener {
            if (binding.tvAboutContent.visibility == View.VISIBLE) {
                binding.tvAboutContent.visibility = View.GONE
            } else {
                binding.tvAboutContent.visibility = View.VISIBLE
            }
        }

        binding.btnLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}

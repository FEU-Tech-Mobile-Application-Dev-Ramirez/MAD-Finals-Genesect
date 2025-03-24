package com.example.mad_collaborative

import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import com.example.mad_collaborative.databinding.UserHomeBinding

class UserHomeActivity : ComponentActivity() {

    private lateinit var binding: UserHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = UserHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()


        binding.T1.visibility = View.VISIBLE
        binding.newsSection.visibility = View.GONE

        binding.btnTournaments.setOnClickListener {
            if (binding.newsSection.visibility == View.VISIBLE) {
                binding.newsSection.visibility = View.GONE
                binding.T1.visibility = View.VISIBLE
            }
        }

        binding.btnNews.setOnClickListener {
            binding.T1.visibility = View.GONE
            binding.newsSection.visibility = View.VISIBLE
        }
    }
}


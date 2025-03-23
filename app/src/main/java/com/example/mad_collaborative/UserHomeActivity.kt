package com.example.mad_collaborative

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.ComponentActivity

class UserHomeActivity : ComponentActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_home)

        val btnTournaments = findViewById<Button>(R.id.btnTournaments)
        val btnNews = findViewById<Button>(R.id.btnNews)
        val tournamentT1 = findViewById<View>(R.id.T1)
        val newsSection = findViewById<View>(R.id.newsSection)

        tournamentT1.visibility = View.VISIBLE
        newsSection.visibility = View.GONE

        btnTournaments.setOnClickListener {
            if (newsSection.visibility == View.VISIBLE) {
                newsSection.visibility = View.GONE
                tournamentT1.visibility = View.VISIBLE
            }
        }

        btnNews.setOnClickListener {
            tournamentT1.visibility = View.GONE
            newsSection.visibility = View.VISIBLE
        }
    }
}


package com.example.mad_collaborative

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.ComponentActivity

class UserHomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_home)

        val btnTournaments = findViewById<Button>(R.id.btnTournaments)
        val btnNews = findViewById<Button>(R.id.btnNews)
        val tournamentT1 = findViewById<View>(R.id.T1)
        val tournamentT2 = findViewById<View>(R.id.T2)
        val tournamentT3 = findViewById<View>(R.id.T3)
        val newsSection = findViewById<View>(R.id.newsSection)

        tournamentT1.visibility = View.VISIBLE
        tournamentT2.visibility = View.VISIBLE
        tournamentT3.visibility = View.VISIBLE
        newsSection.visibility = View.GONE

        btnTournaments.setOnClickListener {
            if (newsSection.visibility == View.VISIBLE) {
                newsSection.visibility = View.GONE
                tournamentT1.visibility = View.VISIBLE
                tournamentT2.visibility = View.VISIBLE
                tournamentT3.visibility = View.VISIBLE
            }
        }

        btnNews.setOnClickListener {
            tournamentT1.visibility = View.GONE
            tournamentT2.visibility = View.GONE
            tournamentT3.visibility = View.GONE
            newsSection.visibility = View.VISIBLE
        }
    }
}


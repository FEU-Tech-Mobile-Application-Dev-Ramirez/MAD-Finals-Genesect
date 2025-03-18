package com.example.mad_collaborative


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge


class UserHomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_home)
        enableEdgeToEdge()


    }
}
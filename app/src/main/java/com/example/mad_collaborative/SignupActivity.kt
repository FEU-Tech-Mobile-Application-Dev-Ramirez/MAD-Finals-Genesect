package com.example.mad_collaborative

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge

class SignupActivity : ComponentActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup)
        enableEdgeToEdge()

        val gotologin = findViewById<Button>(R.id.gotologin)
        val userCheckBox = findViewById<CheckBox>(R.id.userCheckbox2)
        val organizerCheckBox = findViewById<CheckBox>(R.id.organizerCheckbox2)

        gotologin.setOnClickListener {
            val back = Intent(this, MainActivity::class.java)
            startActivity(back)

        }

        userCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                organizerCheckBox.isChecked = false
            }
        }

        organizerCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                userCheckBox.isChecked = false
            }
        }
    }
}
package com.example.mad_collaborative


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.loginpage)
        enableEdgeToEdge()

        val signupButton = findViewById<Button>(R.id.signupButton)
        val userCheckBox = findViewById<CheckBox>(R.id.userCheckbox)
        val organizerCheckBox = findViewById<CheckBox>(R.id.organizerCheckbox)

        signupButton.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
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


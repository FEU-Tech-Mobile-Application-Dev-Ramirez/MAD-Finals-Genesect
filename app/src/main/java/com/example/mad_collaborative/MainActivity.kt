package com.example.mad_collaborative


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
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
        val loginButton =findViewById<Button>(R.id.loginButton)
        val emailInput = findViewById<EditText>(R.id.emailInput)
        val passwordInput = findViewById<EditText>(R.id.passwordInput)

        signupButton.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }

        loginButton.setOnClickListener {
            val emailText = emailInput.text.toString().trim()
            val passwordText = passwordInput.text.toString().trim()

            if (!userCheckBox.isChecked) {
                Toast.makeText(this, "Please select 'User' to proceed", Toast.LENGTH_SHORT).show()
            } else if (emailText.isEmpty() || passwordText.isEmpty()) {
                Toast.makeText(this, "Please enter both email and password", Toast.LENGTH_SHORT).show()
            } else {
                val intent2 = Intent(this, UserHomeActivity::class.java)
                startActivity(intent2)
            }
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


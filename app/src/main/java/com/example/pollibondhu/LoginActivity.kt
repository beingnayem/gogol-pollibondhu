package com.example.pollibondhu

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        // Auto-login check
        if (auth.currentUser != null) {
            navigateToMain()
        }

        findViewById<Button>(R.id.btnLogin).setOnClickListener {
            val email = findViewById<EditText>(R.id.etLoginEmail).text.toString().trim()
            val password = findViewById<EditText>(R.id.etLoginPassword).text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "স্বাগতম!", Toast.LENGTH_SHORT).show()
                            navigateToMain()
                        } else {
                            // Show specific error message
                            val error = task.exception?.message ?: "অজানা ত্রুটি"
                            Toast.makeText(this, "লগইন ব্যর্থ: $error", Toast.LENGTH_LONG).show()
                        }
                    }
            } else {
                Toast.makeText(this, "ইমেইল এবং পাসওয়ার্ড দিন", Toast.LENGTH_SHORT).show()
            }
        }

        findViewById<TextView>(R.id.tvRegisterLink).setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun navigateToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
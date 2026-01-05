package com.example.pollibondhu

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
// FIREBASE IMPORTS
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
// RESOURCE IMPORT
import com.example.pollibondhu.R

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        findViewById<Button>(R.id.btnRegister).setOnClickListener {
            registerUser()
        }

        findViewById<TextView>(R.id.tvLoginLink).setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun registerUser() {
        val name = findViewById<EditText>(R.id.etRegName).text.toString().trim()
        val email = findViewById<EditText>(R.id.etRegEmail).text.toString().trim()
        val phone = findViewById<EditText>(R.id.etRegPhone).text.toString().trim()
        val role = findViewById<EditText>(R.id.etRegRole).text.toString().trim()
        val password = findViewById<EditText>(R.id.etRegPassword).text.toString().trim()

        if (email.isEmpty() || password.isEmpty() || name.isEmpty()) {
            Toast.makeText(this, "সব তথ্য পূরণ করুন", Toast.LENGTH_SHORT).show()
            return
        }

        // 1. Create Account
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { authResult ->
                // AUTH SUCCESS: The user is created. We move NOW.

                val uid = authResult.user?.uid
                val user = User(uid, name, email, role, phone)

                if (uid != null) {
                    // 2. Start Saving Data (Background Task)
                    // We do NOT attach a listener here. We just let it run.
                    database.reference.child("users").child(uid).setValue(user)
                }

                // 3. SHOW SUCCESS & NAVIGATE IMMEDIATELY
                Toast.makeText(this, "রেজিস্ট্রেশন সফল হয়েছে!", Toast.LENGTH_LONG).show()

                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
            .addOnFailureListener { e ->
                // Only stays on page if Account Creation fails (e.g., email taken)
                Toast.makeText(this, "রেজিস্ট্রেশন ব্যর্থ: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }
}
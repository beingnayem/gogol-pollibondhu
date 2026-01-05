package com.example.pollibondhu

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProfileFragment : Fragment() {

    private lateinit var tvUserName: TextView
    private lateinit var tvUserRole: TextView
    private lateinit var prefs: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Initialize Shared Preferences (Local Storage)
        prefs = requireActivity().getSharedPreferences("UserProfile", Context.MODE_PRIVATE)

        tvUserName = view.findViewById(R.id.tvUserName)
        tvUserRole = view.findViewById(R.id.tvUserRole)

        // --- CRITICAL FIX: LOAD CACHED DATA IMMEDIATELY ---
        // This ensures the user sees their name INSTANTLY when opening the app
        val savedName = prefs.getString("NAME", "ব্যবহারকারী")
        val savedRole = prefs.getString("ROLE", "সদস্য")

        tvUserName.text = savedName
        tvUserRole.text = savedRole

        // 2. Fetch Fresh Data from Internet (Background Update)
        fetchUserData()

        // 3. Setup Features
        setupEditProfile(view)
        setupSavedServices(view.findViewById(R.id.containerSavedServices))
        setupStats(view)
        setupSettings(view)
        setupLogout(view)
    }

    private fun fetchUserData() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            val uid = currentUser.uid
            val ref = FirebaseDatabase.getInstance().getReference("users").child(uid)

            // Keep UI in sync with Database
            ref.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        // Get data from Firebase
                        val name = snapshot.child("name").getValue(String::class.java) ?: "ব্যবহারকারী"
                        val role = snapshot.child("role").getValue(String::class.java) ?: "সদস্য"

                        // Update UI with fresh data
                        tvUserName.text = name
                        tvUserRole.text = role

                        // Save to local storage (Update the cache)
                        prefs.edit().putString("NAME", name).putString("ROLE", role).apply()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // If internet fails, do nothing (User already sees cached data)
                    // Optional: Toast.makeText(context, "Network Error", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun setupEditProfile(view: View) {
        val btnEdit = view.findViewById<ImageView>(R.id.btnEditProfile)

        btnEdit.setOnClickListener {
            val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_profile, null)
            val etName = dialogView.findViewById<EditText>(R.id.etEditName)
            val etRole = dialogView.findViewById<EditText>(R.id.etEditRole)

            etName.setText(tvUserName.text)
            etRole.setText(tvUserRole.text)

            AlertDialog.Builder(requireContext())
                .setTitle("প্রোফাইল এডিট করুন")
                .setView(dialogView)
                .setPositiveButton("সংরক্ষণ করুন") { _, _ ->
                    val newName = etName.text.toString()
                    val newRole = etRole.text.toString()

                    // Update UI immediately
                    tvUserName.text = newName
                    tvUserRole.text = newRole

                    // Save to Cache immediately
                    prefs.edit().putString("NAME", newName).putString("ROLE", newRole).apply()

                    // Update Firebase
                    val uid = FirebaseAuth.getInstance().currentUser?.uid
                    if (uid != null) {
                        val updates = mapOf<String, Any>("name" to newName, "role" to newRole)
                        FirebaseDatabase.getInstance().getReference("users").child(uid).updateChildren(updates)
                    }

                    Toast.makeText(context, "আপডেট সম্পন্ন হয়েছে", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("বাতিল", null)
                .show()
        }
    }

    private fun setupSavedServices(container: LinearLayout) {
        data class SavedItem(val title: String, val subtitle: String, val type: String)

        val savedItems = listOf(
            SavedItem("জাতীয় পরিচয়পত্র", "পরিচয়পত্র সেবা", "NID"),
            SavedItem("কৃষি ভর্তুকি", "ভর্তুকি ও সহায়তা", "SUBSIDY"),
            SavedItem("বিশেষজ্ঞ পরামর্শ", "কৃষি সহায়তা", "EXPERT")
        )

        container.removeAllViews()

        savedItems.forEach { item ->
            val itemView = layoutInflater.inflate(R.layout.item_saved_service, container, false)
            itemView.findViewById<TextView>(R.id.tvServiceTitle).text = item.title
            itemView.findViewById<TextView>(R.id.tvServiceSubtitle).text = item.subtitle

            itemView.setOnClickListener {
                val navController = it.findNavController()
                val bundle = Bundle()

                when(item.type) {
                    "NID" -> {
                        bundle.putString("CATEGORY_TITLE", "পরিচয়পত্র সেবা")
                        navController.navigate(R.id.serviceDetailFragment, bundle)
                    }
                    "SUBSIDY" -> {
                        bundle.putString("SERVICE_NAME", "কৃষি ভর্তুকি")
                        navController.navigate(R.id.serviceApplyFragment, bundle)
                    }
                    "EXPERT" -> {
                        navController.navigate(R.id.fieldOfficerFragment)
                    }
                }
            }
            container.addView(itemView)
        }
    }

    private fun setupSettings(view: View) {
        val switchNotif = view.findViewById<SwitchCompat>(R.id.switchNotif)
        val switchLang = view.findViewById<SwitchCompat>(R.id.switchLanguage)

        switchNotif.isChecked = prefs.getBoolean("NOTIF", true)
        switchLang.isChecked = prefs.getBoolean("LANG_BN", true)

        switchNotif.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("NOTIF", isChecked).apply()
        }

        switchLang.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("LANG_BN", isChecked).apply()
        }
    }

    private fun setupStats(view: View) {
        val statServices = view.findViewById<View>(R.id.statServices)
        val statVotes = view.findViewById<View>(R.id.statVotes)
        val statPosts = view.findViewById<View>(R.id.statPosts)

        fun setStat(layout: View, valText: String, labelText: String) {
            layout.findViewById<TextView>(R.id.tvStatValue).text = valText
            layout.findViewById<TextView>(R.id.tvStatLabel).text = labelText
        }

        setStat(statServices, "১২", "সেবা")
        setStat(statVotes, "০৫", "ভোট")
        setStat(statPosts, "০৩", "পোস্ট")
    }

    private fun setupLogout(view: View) {
        view.findViewById<MaterialButton>(R.id.btnLogout).setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("লগ আউট")
                .setMessage("আপনি কি নিশ্চিত?")
                .setPositiveButton("হ্যাঁ") { _, _ ->
                    // Clear Cache on Logout
                    prefs.edit().clear().apply()

                    FirebaseAuth.getInstance().signOut()
                    val intent = Intent(requireActivity(), LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }
                .setNegativeButton("না", null)
                .show()
        }
    }
}
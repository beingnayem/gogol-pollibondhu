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
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.android.material.button.MaterialButton
// THESE IMPORTS FIX YOUR ERROR:
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

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

        prefs = requireActivity().getSharedPreferences("UserProfile", Context.MODE_PRIVATE)

        tvUserName = view.findViewById(R.id.tvUserName)
        tvUserRole = view.findViewById(R.id.tvUserRole)

        // --- 1. FIREBASE DATA FETCH ---
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            val uid = currentUser.uid
            val ref = FirebaseDatabase.getInstance().getReference("users").child(uid)

            ref.get().addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    val name = snapshot.child("name").value.toString()
                    val role = snapshot.child("role").value.toString()

                    tvUserName.text = name
                    tvUserRole.text = role

                    prefs.edit().putString("NAME", name).putString("ROLE", role).apply()
                }
            }.addOnFailureListener {
                // Fallback to offline data
                tvUserName.text = prefs.getString("NAME", "ব্যবহারকারী")
                tvUserRole.text = prefs.getString("ROLE", "সদস্য")
            }
        } else {
            tvUserName.text = prefs.getString("NAME", "ব্যবহারকারী")
            tvUserRole.text = prefs.getString("ROLE", "সদস্য")
        }

        // 2. Setup Features
        setupEditProfile(view)
        setupSavedServices(view.findViewById(R.id.containerSavedServices))
        setupStats(view)
        setupSettings(view)

        // 3. Setup Logout
        setupLogout(view)
    }

    private fun setupEditProfile(view: View) {
        val btnEdit = view.findViewById<ImageButton>(R.id.btnEditProfile)

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

                    tvUserName.text = newName
                    tvUserRole.text = newRole

                    prefs.edit().putString("NAME", newName).putString("ROLE", newRole).apply()

                    // Update Firebase
                    val uid = FirebaseAuth.getInstance().currentUser?.uid
                    if (uid != null) {
                        val updates = mapOf<String, Any>("name" to newName, "role" to newRole)
                        FirebaseDatabase.getInstance().getReference("users").child(uid).updateChildren(updates)
                    }

                    Toast.makeText(context, "প্রোফাইল আপডেট করা হয়েছে", Toast.LENGTH_SHORT).show()
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
            val msg = if(isChecked) "নোটিফিকেশন চালু হয়েছে" else "নোটিফিকেশন বন্ধ হয়েছে"
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }

        switchLang.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("LANG_BN", isChecked).apply()
            val lang = if(isChecked) "বাংলা" else "English"
            Toast.makeText(context, "ভাষা পরিবর্তন: $lang", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupStats(view: View) {
        val statServices = view.findViewById<View>(R.id.statServices)
        val statVotes = view.findViewById<View>(R.id.statVotes)
        val statPosts = view.findViewById<View>(R.id.statPosts)

        fun setStat(view: View, valText: String, labelText: String) {
            view.findViewById<TextView>(R.id.tvStatValue).text = valText
            view.findViewById<TextView>(R.id.tvStatLabel).text = labelText
        }

        setStat(statServices, "১২", "সেবা গ্রহণ")
        setStat(statVotes, "০৫", "ভোট প্রদান")
        setStat(statPosts, "০৩", "ফোরাম পোস্ট")
    }

    private fun setupLogout(view: View) {
        view.findViewById<MaterialButton>(R.id.btnLogout).setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("লগ আউট")
                .setMessage("আপনি কি নিশ্চিত?")
                .setPositiveButton("হ্যাঁ") { _, _ ->
                    // 1. Sign out from Firebase
                    FirebaseAuth.getInstance().signOut()

                    // 2. Navigate to LoginActivity and clear back stack
                    val intent = Intent(requireActivity(), LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)

                    Toast.makeText(context, "লগ আউট সফল হয়েছে", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("না", null)
                .show()
        }
    }
}
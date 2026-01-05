package com.example.pollibondhu

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SwitchCompat
import androidx.navigation.findNavController
import com.google.android.material.button.MaterialButton

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

        // Load saved name/role if exists
        tvUserName.text = prefs.getString("NAME", "মোহাম্মদ করিম উদ্দিন")
        tvUserRole.text = prefs.getString("ROLE", "কৃষক")

        // 1. Setup Edit Profile
        setupEditProfile(view)

        // 2. Setup Saved Services List (The "Khulun" section)
        setupSavedServices(view.findViewById(R.id.containerSavedServices))

        // 3. Setup Stats (Mock Data)
        setupStats(view)

        // 4. Setup Settings (Switches)
        setupSettings(view)

        // 5. Setup Logout
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

                    // Update UI
                    tvUserName.text = newName
                    tvUserRole.text = newRole

                    // Save to Storage
                    prefs.edit().putString("NAME", newName).putString("ROLE", newRole).apply()
                    Toast.makeText(context, "প্রোফাইল আপডেট করা হয়েছে", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("বাতিল", null)
                .show()
        }
    }

    private fun setupSavedServices(container: LinearLayout) {
        // Data: Title, Subtitle, and Destination ID
        data class SavedItem(val title: String, val subtitle: String, val type: String)

        val savedItems = listOf(
            SavedItem("জাতীয় পরিচয়পত্র", "পরিচয়পত্র সেবা", "NID"),
            SavedItem("কৃষি ভর্তুকি", "ভর্তুকি ও সহায়তা", "SUBSIDY"),
            SavedItem("বিশেষজ্ঞ পরামর্শ", "কৃষি সহায়তা", "EXPERT")
        )

        container.removeAllViews() // Clear existing demo items

        savedItems.forEach { item ->
            val itemView = layoutInflater.inflate(R.layout.item_saved_service, container, false)

            itemView.findViewById<TextView>(R.id.tvServiceTitle).text = item.title
            itemView.findViewById<TextView>(R.id.tvServiceSubtitle).text = item.subtitle

            // Handle "Open" (Khulun) Click
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

        // Load State
        switchNotif.isChecked = prefs.getBoolean("NOTIF", true)
        switchLang.isChecked = prefs.getBoolean("LANG_BN", true)

        // Save State on Change
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
        // Just setting static text as placeholder for database data
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
                .setMessage("আপনি কি নিশ্চিত যে আপনি লগ আউট করতে চান?")
                .setPositiveButton("হ্যাঁ") { _, _ ->
                    // Here you would clear session data
                    Toast.makeText(context, "লগ আউট সফল হয়েছে", Toast.LENGTH_SHORT).show()
                    // In a real app: findNavController().navigate(R.id.loginFragment)
                }
                .setNegativeButton("না", null)
                .show()
        }
    }
}
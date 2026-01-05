package com.example.pollibondhu

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.navigation.fragment.findNavController

class HomeFragment : Fragment() {

    private lateinit var tvLocationName: TextView
    private lateinit var prefs: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize Views
        tvLocationName = view.findViewById(R.id.tvLocationName)
        prefs = requireActivity().getSharedPreferences("UserSettings", Context.MODE_PRIVATE)

        // Load previously saved location (Default: Dhaka)
        val savedLocation = prefs.getString("USER_LOCATION", "ঢাকা") ?: "ঢাকা"
        tvLocationName.text = savedLocation

        // Setup UI Components
        setupTopIcons(view)
        setupServiceGrid(view)
    }

    private fun setupTopIcons(view: View) {
        val layoutLocation = view.findViewById<LinearLayout>(R.id.layoutLocationInfo)
        val btnLanguage = view.findViewById<ImageView>(R.id.ivLanguage)
        val btnNotif = view.findViewById<ImageView>(R.id.ivNotif)

        // --- LOCATION SELECTOR ---
        layoutLocation.setOnClickListener {
            val cities = arrayOf("ঢাকা", "চট্টগ্রাম", "সিলেট", "রাজশাহী", "খুলনা", "বরিশাল", "রংপুর", "ময়মনসিংহ", "কুমিল্লা")

            AlertDialog.Builder(requireContext())
                .setTitle("আপনার অবস্থান নির্বাচন করুন")
                .setItems(cities) { _, which ->
                    val selectedCity = cities[which]

                    // 1. Update UI immediately
                    tvLocationName.text = selectedCity

                    // 2. Save to Memory (so other pages can use it)
                    prefs.edit().putString("USER_LOCATION", selectedCity).apply()

                    Toast.makeText(context, "$selectedCity নির্বাচিত হয়েছে", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("বাতিল", null)
                .show()
        }

        // --- LANGUAGE ---
        btnLanguage.setOnClickListener {
            val languages = arrayOf("বাংলা", "English")
            AlertDialog.Builder(requireContext())
                .setTitle("ভাষা নির্বাচন করুন")
                .setSingleChoiceItems(languages, 0) { dialog, which ->
                    dialog.dismiss()
                    Toast.makeText(context, "${languages[which]} নির্বাচিত হয়েছে", Toast.LENGTH_SHORT).show()
                }
                .show()
        }

        // --- NOTIFICATIONS ---
        btnNotif.setOnClickListener {
            val alerts = arrayOf(
                "⚠️ ঘূর্ণিঝড় সতর্কবার্তা: আগামী ২৪ ঘণ্টা সাবধানে থাকুন।",
                "📢 টিকা কার্যক্রম: আগামীকাল ৫-১০ বছরের শিশুদের টিকা দেয়া হবে।",
                "🌾 কৃষি সংবাদ: সারের ভর্তুকি আবেদন শুরু হয়েছে।"
            )
            AlertDialog.Builder(requireContext())
                .setTitle("নোটিফিকেশন")
                .setIcon(R.drawable.ic_notifications)
                .setItems(alerts, null)
                .setPositiveButton("বন্ধ করুন", null)
                .show()
        }
    }

    private fun setupServiceGrid(view: View) {
        val gridServices = view.findViewById<GridLayout>(R.id.gridServices)

        val colorGreen = Color.parseColor("#4CAF50")
        val colorOrange = Color.parseColor("#FF9800")
        val colorBlue = Color.parseColor("#2196F3")
        val colorPurple = Color.parseColor("#9C27B0")
        val colorRed = Color.parseColor("#E53935")

        val services = listOf(
            Service("ডিজিটাল সেবা", "সরকারি ও কমিউনিটি সেবা", R.drawable.ic_digital_service, colorGreen, "TAB_DIGITAL"),
            Service("কৃষি সহায়তা", "ফসলের পরামর্শ ও বিশেষজ্ঞ", R.drawable.ic_service, colorOrange, "TAB_AGRI"),
            Service("ভোট ও মতামত", "স্থানীয় শাসনে অংশগ্রহণ", R.drawable.ic_info, colorBlue, "TAB_VOTE"),
            Service("শিক্ষা সম্পদ", "ডিজিটাল শিক্ষা উপকরণ", R.drawable.ic_contact, colorPurple, "ACTION_EDU"),
            Service("স্বাস্থ্য সেবা", "স্বাস্থ্য পরামর্শ ও তথ্য", R.drawable.ic_service, colorGreen, "NAV_HEALTH"),
            Service("জরুরি যোগাযোগ", "জরুরি সেবা ও হটলাইন", R.drawable.ic_contact, colorRed, "ACTION_CALL")
        )

        services.forEach { service ->
            val cardView = layoutInflater.inflate(R.layout.item_service_card, gridServices, false)
            val params = GridLayout.LayoutParams()
            params.width = 0
            params.height = GridLayout.LayoutParams.WRAP_CONTENT
            params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
            params.setMargins(16, 16, 16, 16)
            cardView.layoutParams = params

            val imageView = cardView.findViewById<ImageView>(R.id.card_image)
            val titleView = cardView.findViewById<TextView>(R.id.card_title)
            val subtitleView = cardView.findViewById<TextView>(R.id.card_subtitle)

            imageView.setImageResource(service.image)
            imageView.setColorFilter(service.color)
            titleView.text = service.title
            subtitleView.text = service.subtitle

            cardView.setOnClickListener { handleServiceClick(service) }
            gridServices.addView(cardView)
        }
    }

    private fun handleServiceClick(service: Service) {
        val bottomNav = activity?.findViewById<BottomNavigationView>(R.id.bottom_navigation)
        when (service.actionType) {
            "TAB_DIGITAL" -> bottomNav?.selectedItemId = R.id.serviceFragment
            "TAB_AGRI" -> bottomNav?.selectedItemId = R.id.agricultureFragment
            "TAB_VOTE" -> bottomNav?.selectedItemId = R.id.citizenFragment
            "ACTION_CALL" -> startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:999")))
            "ACTION_EDU" -> startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("http://www.ebook.gov.bd/")))
            "NAV_HEALTH" -> {
                val bundle = Bundle()
                bundle.putString("CATEGORY_TITLE", "স্বাস্থ্য সেবা")
                bundle.putString("DATA_TYPE", "DOCTORS")
                findNavController().navigate(R.id.serviceDetailFragment, bundle)
            }
            else -> Toast.makeText(context, "${service.title} শীঘ্রই আসছে", Toast.LENGTH_SHORT).show()
        }
    }

    data class Service(val title: String, val subtitle: String, val image: Int, val color: Int, val actionType: String)
}
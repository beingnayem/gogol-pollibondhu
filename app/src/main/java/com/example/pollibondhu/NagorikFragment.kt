package com.example.pollibondhu

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import kotlinx.coroutines.launch

class NagorikFragment : Fragment() {

    // --- 1. DATA MODELS ---
    data class GridItem(val title: String, val bgColorHex: String, val iconTintHex: String, val iconRes: Int)
    data class InfoItem(val title: String, val iconRes: Int)

    // --- 2. MASTER DATA (Used for filtering) ---
    private val allGridItems = listOf(
        GridItem("পরিবহন তথ্য", "#E3F2FD", "#2196F3", R.drawable.ic_poribohon),
        GridItem("বিল পরিশোধ", "#E8F5E9", "#4CAF50", R.drawable.ic_bill),
        GridItem("নাগরিক অভিযোগ", "#FFF3E0", "#FF9800", R.drawable.ic_obijog),
        GridItem("জন্ম নিবন্ধন", "#F3E5F5", "#9C27B0", R.drawable.ic_bc)
    )

    private val allInfoItems = listOf(
        InfoItem("আইন ও নীতি", R.drawable.ic_law),
        InfoItem("নাগরিক অধিকার", R.drawable.ic_nagorikodikar),
        InfoItem("নিরাপত্তা নির্দেশিকা", R.drawable.ic_nirapotta),
        InfoItem("কমিউনিটি গাইডলাইন", R.drawable.ic_community)
    )

    // --- 3. UI REFERENCES ---
    private lateinit var gridNagorik: GridLayout
    private lateinit var containerInfoList: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_nagorik, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Init Views
        gridNagorik = view.findViewById(R.id.gridNagorik)
        containerInfoList = view.findViewById(R.id.containerInfoList)
        val etSearch = view.findViewById<EditText>(R.id.etSearch)

        // 1. Setup Features
        setupLocalWeather(view)
        setupEmergencyList(view.findViewById(R.id.containerEmergency))

        // 2. Initial Data Load
        populateGrid(allGridItems)
        populateInfoList(allInfoItems)

        // 3. SEARCH LISTENER (Filters data as you type)
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterData(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    // --- SEARCH FILTER LOGIC ---
    private fun filterData(query: String) {
        val lowerQuery = query.lowercase()

        // Filter Grid
        val filteredGrid = allGridItems.filter {
            it.title.lowercase().contains(lowerQuery)
        }
        populateGrid(filteredGrid)

        // Filter List
        val filteredInfo = allInfoItems.filter {
            it.title.lowercase().contains(lowerQuery)
        }
        populateInfoList(filteredInfo)
    }

    // --- GRID POPULATOR ---
    private fun populateGrid(items: List<GridItem>) {
        gridNagorik.removeAllViews() // Clear old items

        items.forEach { item ->
            val cardView = layoutInflater.inflate(R.layout.item_nagorik_grid_card, gridNagorik, false)

            // Grid Params
            val params = GridLayout.LayoutParams()
            params.width = 0
            params.height = GridLayout.LayoutParams.WRAP_CONTENT
            params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
            params.setMargins(8, 8, 8, 8)
            cardView.layoutParams = params

            // Set Data
            val titleView = cardView.findViewById<TextView>(R.id.item_title)
            val iconBgCard = cardView.findViewById<CardView>(R.id.icon_bg_card)
            val iconView = cardView.findViewById<ImageView>(R.id.item_icon)

            titleView.text = item.title
            // Ensure icons exist or use placeholders
            iconView.setImageResource(item.iconRes)

            // Colors
            iconBgCard.setCardBackgroundColor(Color.parseColor(item.bgColorHex))
            iconView.setColorFilter(Color.parseColor(item.iconTintHex))

            // Navigation Logic
            cardView.setOnClickListener {
                val navController = it.findNavController()
                val bundle = Bundle()

                when (item.title) {
                    "পরিবহন তথ্য" -> {
                        bundle.putString("TYPE", "Transport")
                        navController.navigate(R.id.nagorikListFragment, bundle)
                    }
                    "বিল পরিশোধ" -> {
                        bundle.putString("TYPE", "Bills")
                        navController.navigate(R.id.nagorikListFragment, bundle)
                    }
                    "নাগরিক অভিযোগ" -> {
                        navController.navigate(R.id.complaintFragment)
                    }
                    "জন্ম নিবন্ধন" -> {
                        bundle.putString("URL", "https://bdris.gov.bd")
                        bundle.putString("TITLE", "জন্ম নিবন্ধন")
                        navController.navigate(R.id.webViewFragment, bundle)
                    }
                }
            }
            gridNagorik.addView(cardView)
        }
    }

    // --- INFO LIST POPULATOR ---
    private fun populateInfoList(items: List<InfoItem>) {
        containerInfoList.removeAllViews() // Clear old items

        items.forEach { item ->
            val itemView = layoutInflater.inflate(R.layout.item_service_category, containerInfoList, false)
            val iconView = itemView.findViewById<ImageView>(R.id.item_icon)
            val titleView = itemView.findViewById<TextView>(R.id.item_title)

            titleView.text = item.title
            iconView.setImageResource(item.iconRes)
            iconView.setColorFilter(Color.parseColor("#5C6BC0"))

            itemView.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("TITLE", item.title)
                it.findNavController().navigate(R.id.citizenInfoFragment, bundle)
            }
            containerInfoList.addView(itemView)
        }
    }

    // --- WEATHER LOGIC ---
    private fun setupLocalWeather(view: View) {
        val prefs = requireActivity().getSharedPreferences("UserSettings", Context.MODE_PRIVATE)
        val savedCity = prefs.getString("USER_LOCATION", "Dhaka") ?: "Dhaka"

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                // Assuming WeatherRepository exists from previous steps
                val weatherData = WeatherRepository.getWeatherForCity(savedCity)
                if (weatherData != null) {
                    // Weather fetched successfully
                }
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    // --- EMERGENCY LIST ---
    private fun setupEmergencyList(container: LinearLayout) {
        data class EmergencyItem(val title: String, val number: String, val iconRes: Int)
        val emergencyItems = listOf(
            EmergencyItem("পুলিশ", "999", android.R.drawable.ic_menu_view),
            EmergencyItem("ফায়ার সার্ভিস", "16163", android.R.drawable.ic_menu_report_image),
            EmergencyItem("অ্যাম্বুলেন্স", "999", android.R.drawable.ic_menu_add),
            EmergencyItem("হটলাইন ৯৯৯", "999", android.R.drawable.ic_menu_call)
        )

        emergencyItems.forEach { item ->
            val itemView = layoutInflater.inflate(R.layout.item_emergency_card, container, false)
            itemView.findViewById<TextView>(R.id.item_title).text = item.title
            itemView.findViewById<ImageView>(R.id.item_icon).setImageResource(item.iconRes)

            itemView.setOnClickListener {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:${item.number}")
                startActivity(intent)
            }
            container.addView(itemView)
        }
    }
}
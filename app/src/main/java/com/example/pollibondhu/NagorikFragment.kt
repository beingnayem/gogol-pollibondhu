package com.example.pollibondhu

import android.content.Intent
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
import androidx.cardview.widget.CardView
import androidx.navigation.findNavController // Required for navigation

class NagorikFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_nagorik, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Setup Emergency Horizontal List
        setupEmergencyList(view.findViewById(R.id.containerEmergency))

        // 2. Setup 2x2 Grid
        setupNagorikGrid(view.findViewById(R.id.gridNagorik))

        // 3. Setup Vertical Info List
        setupInfoList(view.findViewById(R.id.containerInfoList))
    }

    // --- Helper Functions ---

    // 1. EMERGENCY SECTION (Click to Call)
    private fun setupEmergencyList(container: LinearLayout) {
        data class EmergencyItem(val title: String, val number: String, val iconRes: Int)

        // Using placeholders for icons if custom ones aren't available yet
        val emergencyItems = listOf(
            EmergencyItem("পুলিশ", "999", android.R.drawable.ic_menu_view),
            EmergencyItem("ফায়ার সার্ভিস", "16163", android.R.drawable.ic_menu_report_image),
            EmergencyItem("অ্যাম্বুলেন্স", "999", android.R.drawable.ic_menu_add),
            EmergencyItem("হটলাইন ৯৯৯", "999", android.R.drawable.ic_menu_call)
        )

        emergencyItems.forEach { item ->
            val itemView = layoutInflater.inflate(R.layout.item_emergency_card, container, false)
            val titleView = itemView.findViewById<TextView>(R.id.item_title)
            val iconView = itemView.findViewById<ImageView>(R.id.item_icon)

            titleView.text = item.title
            iconView.setImageResource(item.iconRes)

            // --- CLICK LISTENER: MAKE CALL ---
            itemView.setOnClickListener {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:${item.number}")
                startActivity(intent)
            }

            container.addView(itemView)
        }
    }

    // 2. GRID SECTION (Navigation)
    private fun setupNagorikGrid(gridLayout: GridLayout) {

        data class GridItem(val title: String, val bgColorHex: String, val iconTintHex: String, val iconRes: Int)

        val items = listOf(
            GridItem("পরিবহন তথ্য", "#E3F2FD", "#2196F3", R.drawable.ic_poribohon),
            GridItem("বিল পরিশোধ", "#E8F5E9", "#4CAF50", R.drawable.ic_bill),
            GridItem("নাগরিক অভিযোগ", "#FFF3E0", "#FF9800", R.drawable.ic_obijog),
            GridItem("জন্ম নিবন্ধন", "#F3E5F5", "#9C27B0", R.drawable.ic_bc)
        )

        items.forEach { item ->
            val cardView = layoutInflater.inflate(R.layout.item_nagorik_grid_card, gridLayout, false)

            val params = GridLayout.LayoutParams()
            params.width = 0
            params.height = GridLayout.LayoutParams.WRAP_CONTENT
            params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
            params.setMargins(8, 8, 8, 8)
            cardView.layoutParams = params

            val titleView = cardView.findViewById<TextView>(R.id.item_title)
            val iconBgCard = cardView.findViewById<CardView>(R.id.icon_bg_card)
            val iconView = cardView.findViewById<ImageView>(R.id.item_icon)

            titleView.text = item.title
            iconView.setImageResource(item.iconRes)

            // Apply dynamic colors
            iconBgCard.setCardBackgroundColor(Color.parseColor(item.bgColorHex))
            iconView.setColorFilter(Color.parseColor(item.iconTintHex))

            // --- CLICK LISTENER: NAVIGATE ---
            cardView.setOnClickListener {
                val navController = it.findNavController()
                val bundle = Bundle()

                when (item.title) {
                    "পরিবহন তথ্য" -> {
                        // Goes to the generic list, showing transport options
                        bundle.putString("TYPE", "Transport")
                        navController.navigate(R.id.nagorikListFragment, bundle)
                    }
                    "বিল পরিশোধ" -> {
                        // Goes to the generic list, showing bill options
                        bundle.putString("TYPE", "Bills")
                        navController.navigate(R.id.nagorikListFragment, bundle)
                    }
                    "নাগরিক অভিযোগ" -> {
                        // Goes directly to the complaint form
                        navController.navigate(R.id.complaintFragment)
                    }
                    "জন্ম নিবন্ধন" -> {
                        // Opens the browser for birth registration
                        bundle.putString("URL", "https://bdris.gov.bd")
                        bundle.putString("TITLE", "জন্ম নিবন্ধন")
                        navController.navigate(R.id.webViewFragment, bundle)
                    }
                }
            }

            gridLayout.addView(cardView)
        }
    }

    // 3. INFO LIST SECTION (Navigation)
    private fun setupInfoList(container: LinearLayout) {
        data class InfoItem(val title: String, val iconRes: Int)

        val infoItems = listOf(
            InfoItem("আইন ও নীতি", R.drawable.ic_law),
            InfoItem("নাগরিক অধিকার", R.drawable.ic_nagorikodikar),
            InfoItem("নিরাপত্তা নির্দেশিকা", R.drawable.ic_nirapotta),
            InfoItem("কমিউনিটি গাইডলাইন", R.drawable.ic_community)
        )

        infoItems.forEach { item ->
            val itemView = layoutInflater.inflate(R.layout.item_service_category, container, false)

            val iconView = itemView.findViewById<ImageView>(R.id.item_icon)
            val titleView = itemView.findViewById<TextView>(R.id.item_title)

            titleView.text = item.title
            iconView.setImageResource(item.iconRes)
            iconView.setColorFilter(Color.parseColor("#5C6BC0"))

            // --- CLICK LISTENER: NAVIGATE TO DETAILS ---
            itemView.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("TITLE", item.title)
                it.findNavController().navigate(R.id.citizenInfoFragment, bundle)
            }

            container.addView(itemView)
        }
    }
}
package com.example.pollibondhu

import android.graphics.Color
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

    // 1. EMERGENCY SECTION
    private fun setupEmergencyList(container: LinearLayout) {
        // Data class for Emergency items
        data class EmergencyItem(val title: String, val iconRes: Int)

        // TODO: Replace with your specific icons (e.g., ic_police, ic_ambulance)
        val emergencyItems = listOf(
            EmergencyItem("পুলিশ", android.R.drawable.ic_menu_view),       // Replace with R.drawable.ic_police
            EmergencyItem("ফায়ার সার্ভিস", android.R.drawable.ic_menu_report_image), // Replace with R.drawable.ic_fire
            EmergencyItem("অ্যাম্বুলেন্স", android.R.drawable.ic_menu_add),     // Replace with R.drawable.ic_ambulance
            EmergencyItem("হটলাইন ৯৯৯", android.R.drawable.ic_menu_call)      // Replace with R.drawable.ic_hotline
        )

        emergencyItems.forEach { item ->
            val itemView = layoutInflater.inflate(R.layout.item_emergency_card, container, false)
            val titleView = itemView.findViewById<TextView>(R.id.item_title)
            val iconView = itemView.findViewById<ImageView>(R.id.item_icon)

            titleView.text = item.title
            // Set unique icon
            iconView.setImageResource(item.iconRes)

            // Note: Tint is set to red in XML layout (item_emergency_card.xml).
            // If you want original icon colors, remove 'app:tint' from the XML.

            container.addView(itemView)
        }
    }

    // 2. GRID SECTION
    private fun setupNagorikGrid(gridLayout: GridLayout) {

        // Data class updated to include 'iconRes'
        data class GridItem(val title: String, val bgColorHex: String, val iconTintHex: String, val iconRes: Int)

        // TODO: Replace these with your actual icons
        val items = listOf(
            GridItem("পরিবহন তথ্য", "#E3F2FD", "#2196F3", R.drawable.ic_poribohon), // Blue
            GridItem("বিল পরিশোধ", "#E8F5E9", "#4CAF50", R.drawable.ic_bill),    // Green (Using existing)
            GridItem("নাগরিক অভিযোগ", "#FFF3E0", "#FF9800", R.drawable.ic_obijog),    // Orange
            GridItem("জন্ম নিবন্ধন", "#F3E5F5", "#9C27B0", R.drawable.ic_bc)         // Purple (Using existing)
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

            // Set unique icon
            iconView.setImageResource(item.iconRes)

            // Apply dynamic colors
            iconBgCard.setCardBackgroundColor(Color.parseColor(item.bgColorHex))
            iconView.setColorFilter(Color.parseColor(item.iconTintHex))

            gridLayout.addView(cardView)
        }
    }

    // 3. INFO LIST SECTION
    private fun setupInfoList(container: LinearLayout) {
        // Data class for Info items
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

            // Set unique icon
            iconView.setImageResource(item.iconRes)

            // Set tint to a neutral gray/blue for this list
            iconView.setColorFilter(Color.parseColor("#5C6BC0"))

            container.addView(itemView)
        }
    }
}
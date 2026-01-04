package com.example.pollibondhu

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.navigation.fragment.findNavController // Import this

class ServiceFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_service, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val container = view.findViewById<LinearLayout>(R.id.layoutCategoryContainer)

        val categories = listOf(
            Category("পরিচয়পত্র সেবা", R.drawable.ic_identity),
            Category("সার্টিফিকেট সেবা", R.drawable.ic_certificate),
            Category("ভূমি সেবা", R.drawable.ic_home),
            Category("ভর্তুকি ও সহায়তা", R.drawable.ic_subsidy)
        )

        categories.forEach { category ->
            val itemView = layoutInflater.inflate(R.layout.item_service_category, container, false)

            val iconView = itemView.findViewById<ImageView>(R.id.item_icon)
            val titleView = itemView.findViewById<TextView>(R.id.item_title)

            titleView.text = category.title

            // Uncomment if you have the icons ready, otherwise use default
            // iconView.setImageResource(category.iconRes)

            // --- CLICK LISTENER ---
            itemView.setOnClickListener {
                // Pass the title to the next fragment
                val bundle = Bundle()
                bundle.putString("CATEGORY_TITLE", category.title)

                findNavController().navigate(R.id.serviceDetailFragment, bundle)
            }

            container.addView(itemView)
        }
    }

    data class Category(val title: String, val iconRes: Int)
}
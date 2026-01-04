package com.example.pollibondhu

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.TextView

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val gridServices = view.findViewById<GridLayout>(R.id.gridServices)

        // Using specific colors from the design
        val colorGreen = Color.parseColor("#4CAF50")
        val colorOrange = Color.parseColor("#FF9800")
        val colorBlue = Color.parseColor("#2196F3")
        val colorPurple = Color.parseColor("#9C27B0")

        val services = listOf(
            Service("ডিজিটাল সেবা", "সরকারি ও কমিউনিটি সেবা অ্যাক্সেস করুন", R.drawable.ic_service, colorGreen),
            Service("কৃষি সহায়তা", "ফসলের পরামর্শ ও বিশেষজ্ঞ সহায়তা", R.drawable.ic_krishi, colorOrange),
            Service("ভোট ও মতামত", "স্থানীয় শাসনে অংশগ্রহণ করুন", R.drawable.ic_info, colorBlue),
            Service("শিক্ষা সম্পদ", "ডিজিটাল শিক্ষা উপকরণ", R.drawable.ic_education, colorPurple),
            Service("স্বাস্থ্য সেবা", "স্বাস্থ্য পরামর্শ ও তথ্য", R.drawable.ic_health, colorGreen),
            Service("জরুরি যোগাযোগ", "জরুরি সেবা ও হটলাইন", R.drawable.ic_contact, colorBlue)
        )

        services.forEach { service ->
            val cardView = layoutInflater.inflate(R.layout.item_service_card, gridServices, false)

            // Set Layout Params to ensure 50% width for grid columns
            val params = GridLayout.LayoutParams()
            params.width = 0
            params.height = GridLayout.LayoutParams.WRAP_CONTENT
            params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
            params.setMargins(16, 16, 16, 16)
            cardView.layoutParams = params

            val imageView = cardView.findViewById<ImageView>(R.id.card_image)
            val titleView = cardView.findViewById<TextView>(R.id.card_title)
            val subtitleView = cardView.findViewById<TextView>(R.id.card_subtitle)

            // Set Data
            imageView.setImageResource(service.image)
            imageView.setColorFilter(service.color) // Apply the specific color
            titleView.text = service.title
            subtitleView.text = service.subtitle

            gridServices.addView(cardView)
        }
    }

    // Updated Data Class with Color
    data class Service(val title: String, val subtitle: String, val image: Int, val color: Int)
}
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
import com.google.android.material.bottomnavigation.BottomNavigationView

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

        // রঙের ভেরিয়েবল
        val colorGreen = Color.parseColor("#4CAF50")
        val colorOrange = Color.parseColor("#FF9800")
        val colorBlue = Color.parseColor("#2196F3")
        val colorPurple = Color.parseColor("#9C27B0")

        val services = listOf(
            Service("ডিজিটাল সেবা", "সরকারি ও কমিউনিটি সেবা অ্যাক্সেস করুন", R.drawable.ic_digital_service, colorGreen),
            Service("কৃষি সহায়তা", "ফসলের পরামর্শ ও বিশেষজ্ঞ সহায়তা", R.drawable.ic_service, colorOrange),
            Service("ভোট ও মতামত", "স্থানীয় শাসনে অংশগ্রহণ করুন", R.drawable.ic_info, colorBlue),
            Service("শিক্ষা সম্পদ", "ডিজিটাল শিক্ষা উপকরণ", R.drawable.ic_contact, colorPurple),
            Service("স্বাস্থ্য সেবা", "স্বাস্থ্য পরামর্শ ও তথ্য", R.drawable.ic_service, colorGreen),
            Service("জরুরি যোগাযোগ", "জরুরি সেবা ও হটলাইন", R.drawable.ic_contact, colorBlue)
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

            // --- আপডেটেড ক্লিক লিসেনার ---
            cardView.setOnClickListener {
                val bottomNav = activity?.findViewById<BottomNavigationView>(R.id.bottom_navigation)

                if (service.title == "ডিজিটাল সেবা") {
                    // সেবা ট্যাবে নিয়ে যাবে
                    bottomNav?.selectedItemId = R.id.serviceFragment
                }
                else if (service.title == "কৃষি সহায়তা") {
                    // কৃষি ট্যাবে নিয়ে যাবে
                    bottomNav?.selectedItemId = R.id.agricultureFragment
                }
                else if (service.title == "ভোট ও মতামত") {
                    // নাগরিক ট্যাবে নিয়ে যাবে
                    bottomNav?.selectedItemId = R.id.citizenFragment
                }
            }

            gridServices.addView(cardView)
        }
    }

    data class Service(val title: String, val subtitle: String, val image: Int, val color: Int)
}
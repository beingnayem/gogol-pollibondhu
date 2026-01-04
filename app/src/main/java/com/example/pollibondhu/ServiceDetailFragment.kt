package com.example.pollibondhu

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.navigation.fragment.findNavController

class ServiceDetailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_service_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvPageTitle = view.findViewById<TextView>(R.id.tvPageTitle)
        val container = view.findViewById<LinearLayout>(R.id.containerSubServices)
        val btnBack = view.findViewById<ImageView>(R.id.btnBack)

        // Get the passed category title from the previous screen
        val categoryTitle = arguments?.getString("CATEGORY_TITLE") ?: "সেবা"
        tvPageTitle.text = categoryTitle

        // Back button logic
        btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        // Load specific sub-services based on the category title
        val items = when (categoryTitle) {
            "পরিচয়পত্র সেবা" -> listOf(
                SubService("নতুন ভোটার নিবন্ধন", "১৮ বছর বা তদূর্ধ্বদের জন্য"),
                SubService("এনআইডি সংশোধন", "নাম বা তথ্য পরিবর্তন"),
                SubService("হারানো কার্ড", "ডুপ্লিকেট কপি সংগ্রহ"),
                SubService("স্মার্ট কার্ড স্ট্যাটাস", "বিতরণ তথ্য জানুন")
            )
            "সার্টিফিকেট সেবা" -> listOf(
                SubService("জন্ম নিবন্ধন সনদ", "নতুন শিশুর জন্ম নিবন্ধন"),
                SubService("মৃত্যু সনদ", "মৃত্যু নিবন্ধনের আবেদন"),
                SubService("নাগরিকত্ব সনদ", "চেয়ারম্যান কর্তৃক প্রদত্ত"),
                SubService("ওয়ারিশান সনদ", "উত্তরাধিকার নির্ণয়")
            )
            "ভূমি সেবা" -> listOf(
                SubService("ই-পর্চা", "অনলাইন খতিয়ান আবেদন"),
                SubService("ই-নামজারি", "মালিকানা পরিবর্তন"),
                SubService("ভূমি উন্নয়ন কর", "অনলাইনে কর প্রদান"),
                SubService("মৌজা ম্যাপ", "ডিজিটাল ম্যাপ দেখুন")
            )
            "ভর্তুকি ও সহায়তা" -> listOf(
                SubService("কৃষি ভর্তুকি", "সার ও বীজের জন্য"),
                SubService("বয়স্ক ভাতা", "আবেদন ও স্ট্যাটাস"),
                SubService("বিধবা ভাতা", "সরকার প্রদত্ত সহায়তা"),
                SubService("প্রতিবন্ধী ভাতা", "নিবন্ধিতদের জন্য")
            )
            else -> listOf(
                SubService("সাধারণ তথ্য", "বিস্তারিত জানতে কল করুন")
            )
        }

        // Dynamically populate the list
        items.forEach { item ->
            val itemView = layoutInflater.inflate(R.layout.item_sub_service, container, false)

            val tvSubTitle = itemView.findViewById<TextView>(R.id.tvSubTitle)
            val tvSubDesc = itemView.findViewById<TextView>(R.id.tvSubDesc)

            tvSubTitle.text = item.title
            tvSubDesc.text = item.desc

            // --- UPDATED CLICK LISTENER ---
            // When a sub-service is clicked, go to the Apply Form
            itemView.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("SERVICE_NAME", item.title) // Pass the specific service name (e.g., "কৃষি ভর্তুকি")

                findNavController().navigate(R.id.serviceApplyFragment, bundle)
            }

            container.addView(itemView)
        }
    }

    data class SubService(val title: String, val desc: String)
}
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
        return inflater.inflate(R.layout.fragment_service_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvPageTitle = view.findViewById<TextView>(R.id.tvPageTitle)
        val container = view.findViewById<LinearLayout>(R.id.containerSubServices)
        val btnBack = view.findViewById<ImageView>(R.id.btnBack)

        // Get arguments
        val categoryTitle = arguments?.getString("CATEGORY_TITLE") ?: "সেবা"
        val dataType = arguments?.getString("DATA_TYPE") // New argument

        tvPageTitle.text = categoryTitle

        btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        // --- INTELLIGENT LIST LOADING ---
        // 1. Check if it's a special type (like Doctors)
        // 2. If not, check the Title string

        val items: List<SubService> = if (dataType == "DOCTORS") {
            // Load Doctor List for Health
            listOf(
                SubService("ডা. মো. রফিকুল ইসলাম", "মেডিসিন বিশেষজ্ঞ, জেলা সদর হাসপাতাল"),
                SubService("ডা. নুসরাত জাহান", "গাইনোকোলজি ও প্রসূতি বিশেষজ্ঞ"),
                SubService("উপজেলা স্বাস্থ্য কমপ্লেক্স", "জরুরি বিভাগ ও অ্যাম্বুলেন্স - ২৪/৭"),
                SubService("কমিউনিটি ক্লিনিক", "প্রাথমিক চিকিৎসা ও পরামর্শ")
            )
        } else {
            // Load Standard Services based on Title
            when (categoryTitle) {
                "পরিচয়পত্র সেবা" -> listOf(
                    SubService("নতুন ভোটার নিবন্ধন", "১৮ বছর বা তদূর্ধ্বদের জন্য আবেদন"),
                    SubService("এনআইডি সংশোধন", "নাম, ঠিকানা বা বয়স পরিবর্তন"),
                    SubService("হারানো কার্ড", "ডুপ্লিকেট কপি বা রি-ইস্যু"),
                    SubService("স্মার্ট কার্ড স্ট্যাটাস", "বিতরণ তথ্য ও কেন্দ্র জানুন")
                )
                "সার্টিফিকেট সেবা" -> listOf(
                    SubService("জন্ম নিবন্ধন সনদ", "নতুন শিশুর জন্ম নিবন্ধন আবেদন"),
                    SubService("মৃত্যু সনদ", "মৃত্যু নিবন্ধনের আবেদন ও কপি"),
                    SubService("নাগরিকত্ব সনদ", "চেয়ারম্যান কর্তৃক প্রদত্ত সনদ"),
                    SubService("ওয়ারিশান সনদ", "পারিবারিক উত্তরাধিকার সনদ")
                )
                "ভূমি সেবা" -> listOf(
                    SubService("ই-পর্চা", "অনলাইন খতিয়ান ও পর্চা আবেদন"),
                    SubService("ই-নামজারি", "জমির মালিকানা পরিবর্তন আবেদন"),
                    SubService("ভূমি উন্নয়ন কর", "অনলাইনে জমির খাজনা প্রদান"),
                    SubService("মৌজা ম্যাপ", "ডিজিটাল ম্যাপ ও দাগ নম্বর")
                )
                "ভর্তুকি ও সহায়তা" -> listOf(
                    SubService("কৃষি ভর্তুকি", "সার, বীজ ও কৃষি যন্ত্রপাতির জন্য"),
                    SubService("বয়স্ক ভাতা", "আবেদন, যোগ্যতা ও স্ট্যাটাস"),
                    SubService("বিধবা ভাতা", "সরকার প্রদত্ত মাসিক সহায়তা"),
                    SubService("প্রতিবন্ধী ভাতা", "নিবন্ধিতদের জন্য বিশেষ সুবিধা")
                )
                // New Cases for consistency
                "শিক্ষা সম্পদ" -> listOf(
                    SubService("অনলাইন ক্লাস (১ম-১০ম)", "সরকারি ডিজিটাল পাঠদান"),
                    SubService("উপবৃত্তি তথ্য", "শিক্ষার্থীদের বৃত্তির আবেদন"),
                    SubService("ফলাফল দেখুন", "পিএসসি, জেএসসি, এসএসসি ফলাফল")
                )
                "জরুরি যোগাযোগ" -> listOf(
                    SubService("জাতীয় জরুরি সেবা", "৯৯৯ (পুলিশ, ফায়ার, অ্যাম্বুলেন্স)"),
                    SubService("নারী ও শিশু নির্যাতন", "১০৯ - দ্রুত সহায়তা"),
                    SubService("সরকারি আইন সেবা", "১৬৪৩০ - লিগ্যাল এইড")
                )
                else -> listOf(
                    SubService("সাধারণ তথ্য", "বিস্তারিত জানতে হেল্পলাইনে কল করুন")
                )
            }
        }

        // Populate List
        items.forEach { item ->
            val itemView = layoutInflater.inflate(R.layout.item_sub_service, container, false)

            val tvSubTitle = itemView.findViewById<TextView>(R.id.tvSubTitle)
            val tvSubDesc = itemView.findViewById<TextView>(R.id.tvSubDesc)

            tvSubTitle.text = item.title
            tvSubDesc.text = item.desc

            itemView.setOnClickListener {
                // Determine where to go based on the item
                // For now, sending everyone to Apply Fragment is fine for the prototype
                val bundle = Bundle()
                bundle.putString("SERVICE_NAME", item.title)

                findNavController().navigate(R.id.serviceApplyFragment, bundle)
            }

            container.addView(itemView)
        }
    }

    data class SubService(val title: String, val desc: String)
}
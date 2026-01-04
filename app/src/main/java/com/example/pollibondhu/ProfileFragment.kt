package com.example.pollibondhu

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.button.MaterialButton

class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Set Profile Data
        view.findViewById<TextView>(R.id.tvUserName).text = "মোহাম্মদ করিম উদ্দিন"
        view.findViewById<TextView>(R.id.tvUserRole).text = "কৃষক"

        // 2. Set Stats
        val statServices = view.findViewById<View>(R.id.statServices)
        val statVotes = view.findViewById<View>(R.id.statVotes)
        val statPosts = view.findViewById<View>(R.id.statPosts)

        fun setStatData(statView: View, value: String, label: String) {
            statView.findViewById<TextView>(R.id.tvStatValue).text = value
            statView.findViewById<TextView>(R.id.tvStatLabel).text = label
        }

        setStatData(statServices, "১২", "সেবা গ্রহণ")
        setStatData(statVotes, "০৫", "ভোট প্রদান")
        setStatData(statPosts, "০৩", "ফোরাম পোস্ট")

        // 3. Populate Saved Services List (NEW)
        val savedContainer = view.findViewById<LinearLayout>(R.id.containerSavedServices)
        val savedItems = listOf(
            Pair("জাতীয় পরিচয়পত্র", "পরিচয়পত্র সেবা"),
            Pair("কৃষি ভর্তুকি", "ভর্তুকি ও সহায়তা"),
            Pair("বিশেষজ্ঞ পরামর্শ", "কৃষি সহায়তা")
        )

        savedItems.forEach { (title, subtitle) ->
            val itemView = layoutInflater.inflate(R.layout.item_saved_service, savedContainer, false)
            itemView.findViewById<TextView>(R.id.tvServiceTitle).text = title
            itemView.findViewById<TextView>(R.id.tvServiceSubtitle).text = subtitle
            savedContainer.addView(itemView)
        }

        // 4. Buttons
        view.findViewById<ImageButton>(R.id.btnEditProfile).setOnClickListener {
            Toast.makeText(context, "Edit Profile clicked", Toast.LENGTH_SHORT).show()
        }

        view.findViewById<MaterialButton>(R.id.btnLogout).setOnClickListener {
            Toast.makeText(context, "Logging out...", Toast.LENGTH_SHORT).show()
        }
    }
}
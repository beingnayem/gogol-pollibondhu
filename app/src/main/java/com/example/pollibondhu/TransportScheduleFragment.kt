package com.example.pollibondhu

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.findNavController

class TransportScheduleFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        inflater.inflate(R.layout.fragment_transport_schedule, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val title = arguments?.getString("TITLE") ?: "সময়সূচি"
        view.findViewById<TextView>(R.id.tvTransTitle).text = title
        view.findViewById<ImageView>(R.id.btnBack).setOnClickListener { it.findNavController().popBackStack() }

        val container = view.findViewById<LinearLayout>(R.id.containerSchedule)

        // Mock Data based on selection
        val schedule = when {
            title.contains("বাস") -> listOf("সকাল ০৭:০০ - ঢাকা", "সকাল ০৮:৩০ - চট্টগ্রাম", "দুপুর ০২:০০ - সিলেট", "রাত ১০:০০ - খুলনা")
            title.contains("ট্রেন") -> listOf("সুবর্ণ এক্সপ্রেস - ০৭:০০ AM", "পারাবত এক্সপ্রেস - ০৬:২০ AM", "অগ্নিবীণা - ০৯:৪০ AM", "একতা এক্সপ্রেস - ১০:০০ AM")
            title.contains("লঞ্চ") -> listOf("এম ভি পারাবত - সন্ধ্যা ০৭:০০", "সুরভী ৯ - রাত ০৮:৩০", "সুন্দরবন ১০ - রাত ০৯:০০")
            else -> listOf("তথ্য হালনাগাদ করা হচ্ছে...")
        }

        schedule.forEach { info ->
            val card = CardView(requireContext())
            val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            params.setMargins(0, 0, 0, 16)
            card.layoutParams = params
            card.radius = 12f
            card.setContentPadding(24, 24, 24, 24)
            card.setCardBackgroundColor(Color.WHITE)

            val tv = TextView(requireContext())
            tv.text = info
            tv.textSize = 16f
            tv.setTextColor(Color.BLACK)

            card.addView(tv)
            container.addView(card)
        }
    }
}
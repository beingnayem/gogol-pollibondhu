package com.example.pollibondhu

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.navigation.findNavController // This import requires a View to work

class CitizenInfoFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Reusing the existing generic layout
        return inflater.inflate(R.layout.fragment_agri_info_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val title = arguments?.getString("TITLE") ?: "তথ্য"
        view.findViewById<TextView>(R.id.tvPageTitle).text = title

        // --- FIXED LINE BELOW ---
        // Added "it." before findNavController() to use the clicked view context
        view.findViewById<ImageView>(R.id.btnBack).setOnClickListener {
            it.findNavController().popBackStack()
        }

        val container = view.findViewById<LinearLayout>(R.id.containerInfo)

        val data = when(title) {
            "আইন ও নীতি" -> listOf(
                "ভোক্তা অধিকার আইন" to "পণ্য ক্রয়ে প্রতারিত হলে অভিযোগ করার নিয়ম।",
                "তথ্য অধিকার আইন" to "সরকারি অফিস থেকে তথ্য প্রাপ্তির অধিকার।"
            )
            "নাগরিক অধিকার" -> listOf(
                "ভোট প্রদানের অধিকার" to "সুষ্ঠু নির্বাচনে অংশগ্রহণের অধিকার।",
                "মত প্রকাশের স্বাধীনতা" to "স্বাধীনভাবে নিজের মত প্রকাশ করা।"
            )
            "নিরাপত্তা নির্দেশিকা" -> listOf(
                "জরুরি সেবা ৯৯৯" to "যেকোনো বিপদে পুলিশ, ফায়ার সার্ভিস বা অ্যাম্বুলেন্স পেতে কল করুন।",
                "থানায় জিডি" to "হারানো বা হুমকির ক্ষেত্রে নিকটস্থ থানায় জিডি করার নিয়ম।"
            )
            else -> listOf("সাধারণ তথ্য" to "বিস্তারিত তথ্য শীঘ্রই আসছে...")
        }



        data.forEach { (h, b) ->
            val item = layoutInflater.inflate(R.layout.item_info_expandable, container, false)
            item.findViewById<TextView>(R.id.tvTitle).text = h
            item.findViewById<TextView>(R.id.tvDesc).text = b

            item.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("TITLE", h)
                // Passing a dummy long text description
                bundle.putString("BODY", "$h সম্পর্কে বিস্তারিত আইন এবং নীতিমালা এখানে থাকবে। \n\n১. ধারা ১০১: এই আইন অনুযায়ী...\n২. ধারা ১০২: নাগরিকদের অধিকার রক্ষায়...\n\n(এটি একটি ডেমো টেক্সট)")

                it.findNavController().navigate(R.id.contentReadFragment, bundle)
            }
            container.addView(item)
        }
    }
}
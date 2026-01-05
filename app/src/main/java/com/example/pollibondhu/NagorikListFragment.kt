package com.example.pollibondhu

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.findNavController // This import is required

class NagorikListFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        inflater.inflate(R.layout.fragment_nagorik_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // --- FIXED LINE BELOW ---
        // Added "it." before findNavController() to fix the error
        view.findViewById<ImageView>(R.id.btnBack).setOnClickListener {
            it.findNavController().popBackStack()
        }

        val type = arguments?.getString("TYPE") ?: ""
        val container = view.findViewById<LinearLayout>(R.id.containerList)
        val titleView = view.findViewById<TextView>(R.id.tvPageTitle)

        val items = if (type == "Transport") {
            titleView.text = "পরিবহন তথ্য"
            listOf("বিআরটিসি বাস রুট", "ট্রেনের সময়সূচি", "লঞ্চ টার্মিনাল তথ্য", "বিমান সূচি")
        } else {
            titleView.text = "বিল পরিশোধ"
            listOf("বিদ্যুৎ বিল (NESCO/DESCO)", "গ্যাস বিল (Titas)", "পানি বিল (WASA)", "ইন্টারনেট বিল")
        }



        items.forEach { item ->
            val card = layoutInflater.inflate(R.layout.item_info_expandable, container, false)
            card.findViewById<TextView>(R.id.tvTitle).text = item
            card.findViewById<TextView>(R.id.tvDesc).text = "বিস্তারিত জানতে ট্যাপ করুন >"

            card.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("TITLE", item)

                if (type == "Transport") {
                    // Navigate to Schedule View
                    it.findNavController().navigate(R.id.transportScheduleFragment, bundle)
                } else {
                    // Navigate to Bill Payment Form
                    it.findNavController().navigate(R.id.billPaymentFragment, bundle)
                }
            }
            container.addView(card)
        }
    }
}
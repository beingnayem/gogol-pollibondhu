package com.example.pollibondhu

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.navigation.findNavController

class FieldOfficerFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_field_officer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<ImageView>(R.id.btnBack).setOnClickListener {
            view.findNavController().popBackStack()
        }

        val container = view.findViewById<LinearLayout>(R.id.containerOfficers)

        val officers = listOf(
            Triple("জনাব রফিকুল ইসলাম", "উপ-সহকারী কৃষি কর্মকর্তা (সদর)", "01700000000"),
            Triple("বেগম ফাতেমা আক্তার", "কৃষি সম্প্রসারণ কর্মী (উত্তর)", "01800000000"),
            Triple("জনাব আব্দুল মালেক", "মাঠ পরিদর্শক (দক্ষিণ)", "01900000000")
        )

        officers.forEach { (name, area, phone) ->
            val itemView = layoutInflater.inflate(R.layout.item_officer_card, container, false)
            itemView.findViewById<TextView>(R.id.tvName).text = name
            itemView.findViewById<TextView>(R.id.tvArea).text = area

            itemView.findViewById<View>(R.id.btnCall).setOnClickListener {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:$phone")
                startActivity(intent)
            }
            container.addView(itemView)
        }
    }
}
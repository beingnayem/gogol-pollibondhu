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

class CropDoctorFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_crop_doctor, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // --- Back Button Logic ---
        view.findViewById<ImageView>(R.id.btnBack).setOnClickListener {
            view.findNavController().popBackStack()
        }

        // --- Populate Disease List ---
        val container = view.findViewById<LinearLayout>(R.id.containerDiseases)

        val diseases = listOf(
            Pair("ধানের ব্লাস্ট রোগ", "সমাধান: ট্রাইসাইক্লাজল বা স্ট্রোবিলুরিন গ্রুপের ছত্রাকনাশক স্প্রে করুন।"),
            Pair("আলুর মড়ক", "সমাধান: ম্যানকোজেব বা মেটালোক্সিল জাতীয় ঔষধ স্প্রে করতে হবে।"),
            Pair("বেগুনের ডগা পচা", "সমাধান: আক্রান্ত ডগা কেটে ধ্বংস করুন এবং স্পিনোস্যাড ব্যবহার করুন।")
        )

        diseases.forEach { (name, solution) ->
            val card = CardView(requireContext())
            val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            params.setMargins(0, 0, 0, 24)
            card.layoutParams = params
            card.radius = 16f
            card.cardElevation = 4f
            card.setContentPadding(24, 24, 24, 24)

            val layout = LinearLayout(requireContext())
            layout.orientation = LinearLayout.VERTICAL

            val title = TextView(requireContext())
            title.text = name
            title.textSize = 18f
            title.setTextColor(Color.parseColor("#D32F2F"))
            title.setTypeface(null, android.graphics.Typeface.BOLD)

            val body = TextView(requireContext())
            body.text = solution
            body.textSize = 14f
            body.setTextColor(Color.BLACK)
            body.setPadding(0, 12, 0, 0)

            layout.addView(title)
            layout.addView(body)
            card.addView(layout)
            container.addView(card)
        }
    }
}
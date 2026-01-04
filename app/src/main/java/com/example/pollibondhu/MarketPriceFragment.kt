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

class MarketPriceFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_market_price, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // --- Back Button Logic ---
        view.findViewById<ImageView>(R.id.btnBack).setOnClickListener {
            view.findNavController().popBackStack()
        }

        // --- Populate Price List ---
        val container = view.findViewById<LinearLayout>(R.id.containerPrices)

        val prices = listOf(
            Triple("মিনিকেট চাল", "৬৫ ৳", "প্রতি কেজি"),
            Triple("নাজিরশাইল চাল", "৭৫ ৳", "প্রতি কেজি"),
            Triple("দেশি মসুর ডাল", "১১০ ৳", "প্রতি কেজি"),
            Triple("আলু (ডায়মন্ড)", "২৫ ৳", "প্রতি কেজি"),
            Triple("দেশি পেঁয়াজ", "৯০ ৳", "প্রতি কেজি")
        )

        prices.forEach { (item, price, unit) ->
            val card = CardView(requireContext())
            val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            params.setMargins(0, 0, 0, 16)
            card.layoutParams = params
            card.radius = 12f
            card.setContentPadding(32, 32, 32, 32)

            val row = LinearLayout(requireContext())
            row.orientation = LinearLayout.HORIZONTAL

            val nameTxt = TextView(requireContext())
            nameTxt.text = item
            nameTxt.textSize = 16f
            nameTxt.setTextColor(Color.BLACK)
            nameTxt.layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)

            val priceTxt = TextView(requireContext())
            priceTxt.text = "$price ($unit)"
            priceTxt.textSize = 16f
            priceTxt.setTextColor(Color.parseColor("#4CAF50"))
            priceTxt.setTypeface(null, android.graphics.Typeface.BOLD)

            row.addView(nameTxt)
            row.addView(priceTxt)
            card.addView(row)
            container.addView(card)
        }
    }
}
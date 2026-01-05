package com.example.pollibondhu

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController

class BillPaymentFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        inflater.inflate(R.layout.fragment_bill_payment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val title = arguments?.getString("TITLE") ?: "বিল পরিশোধ"
        view.findViewById<TextView>(R.id.tvBillTitle).text = title

        view.findViewById<ImageView>(R.id.btnBack).setOnClickListener { it.findNavController().popBackStack() }

        view.findViewById<Button>(R.id.btnPay).setOnClickListener {
            // Simulate Payment Success
            AlertDialog.Builder(requireContext())
                .setTitle("সফল!")
                .setMessage("আপনার $title সম্পন্ন হয়েছে। ট্রানজেকশন আইডি: TXN${System.currentTimeMillis()}")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setPositiveButton("ঠিক আছে") { d, _ ->
                    d.dismiss()
                    it.findNavController().popBackStack()
                }
                .show()
        }
    }
}
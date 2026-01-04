package com.example.pollibondhu

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.fragment.findNavController

class AgriLoanFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_agri_loan, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // --- 1. Back Button Logic ---
        val btnBack = view.findViewById<ImageView>(R.id.btnBack)
        btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        // --- 2. Loan Calculator Logic ---
        val etAmount = view.findViewById<EditText>(R.id.etAmount)
        val etRate = view.findViewById<EditText>(R.id.etRate)
        val etDuration = view.findViewById<EditText>(R.id.etDuration)
        val btnCalculate = view.findViewById<Button>(R.id.btnCalculate)
        val tvResult = view.findViewById<TextView>(R.id.tvResult)
        val btnApplyLoan = view.findViewById<Button>(R.id.btnApplyLoan)

        btnCalculate.setOnClickListener {
            val P = etAmount.text.toString().toDoubleOrNull() ?: 0.0
            val rateInput = etRate.text.toString().toDoubleOrNull() ?: 4.0
            val n = etDuration.text.toString().toDoubleOrNull() ?: 12.0

            if (P > 0) {
                val r = rateInput / 12 / 100
                // EMI Calculation: [P x R x (1+R)^N]/[(1+R)^N-1]
                val emi = (P * r * Math.pow(1 + r, n)) / (Math.pow(1 + r, n) - 1)

                tvResult.text = String.format("মাসিক কিস্তি: %.2f টাকা", emi)
                btnApplyLoan.visibility = View.VISIBLE
            }
        }

        // --- 3. Apply Button Logic ---
        btnApplyLoan.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("SERVICE_NAME", "কৃষি ঋণ")
            findNavController().navigate(R.id.serviceApplyFragment, bundle)
        }
    }
}
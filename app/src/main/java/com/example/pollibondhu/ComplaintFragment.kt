package com.example.pollibondhu

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.navigation.fragment.findNavController

class ComplaintFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        inflater.inflate(R.layout.fragment_complaint, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<ImageView>(R.id.btnBack).setOnClickListener { findNavController().popBackStack() }

        view.findViewById<Button>(R.id.btnSubmit).setOnClickListener {
            // Show Success Dialog
            AlertDialog.Builder(requireContext())
                .setTitle("সফল!")
                .setMessage("আপনার অভিযোগ গৃহীত হয়েছে। কর্তৃপক্ষ শীঘ্রই ব্যবস্থা নেবে।")
                .setPositiveButton("ঠিক আছে") { d, _ ->
                    d.dismiss()
                    findNavController().popBackStack()
                }
                .show()
        }
    }
}
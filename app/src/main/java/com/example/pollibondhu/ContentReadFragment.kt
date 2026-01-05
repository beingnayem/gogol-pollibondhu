package com.example.pollibondhu

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController

class ContentReadFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        inflater.inflate(R.layout.fragment_content_read, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val title = arguments?.getString("TITLE") ?: "তথ্য"
        val bodyText = arguments?.getString("BODY") ?: "বিস্তারিত তথ্য শীঘ্রই আসছে..."

        view.findViewById<TextView>(R.id.tvHeader).text = title
        view.findViewById<TextView>(R.id.tvBody).text = bodyText

        view.findViewById<ImageView>(R.id.btnBack).setOnClickListener { it.findNavController().popBackStack() }
    }
}
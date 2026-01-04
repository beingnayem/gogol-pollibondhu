package com.example.pollibondhu

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton

class ServiceApplyFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_service_apply, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnBack = view.findViewById<ImageView>(R.id.btnBack)
        val tvServiceContext = view.findViewById<TextView>(R.id.tvServiceContext)
        val btnSubmit = view.findViewById<MaterialButton>(R.id.btnSubmit)

        // Get the specific service name passed from the previous list
        val serviceName = arguments?.getString("SERVICE_NAME") ?: "সেবা"
        tvServiceContext.text = "আপনি '$serviceName' - এর জন্য আবেদন করছেন"

        btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        btnSubmit.setOnClickListener {
            val name = view.findViewById<EditText>(R.id.etName).text.toString()
            val phone = view.findViewById<EditText>(R.id.etPhone).text.toString()

            if (name.isEmpty() || phone.isEmpty()) {
                Toast.makeText(context, "অনুগ্রহ করে প্রয়োজনীয় তথ্য পূরণ করুন", Toast.LENGTH_SHORT).show()
            } else {
                // Show Success Dialog
                showSuccessDialog(serviceName)
            }
        }
    }

    private fun showSuccessDialog(serviceName: String) {
        AlertDialog.Builder(requireContext())
            .setTitle("সফল!")
            .setMessage("আপনার '$serviceName' এর আবেদন সফলভাবে জমা হয়েছে। আমরা শীঘ্রই আপনার সাথে যোগাযোগ করব।")
            .setIcon(android.R.drawable.ic_dialog_info)
            .setPositiveButton("ঠিক আছে") { dialog, _ ->
                dialog.dismiss()
                // Go back to the previous list
                findNavController().popBackStack()
            }
            .create()
            .show()
    }
}
package com.example.pollibondhu

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.navigation.findNavController

class AgriInfoDetailFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_agri_info_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val title = arguments?.getString("TITLE") ?: "তথ্য"
        view.findViewById<TextView>(R.id.tvPageTitle).text = title

        view.findViewById<ImageView>(R.id.btnBack).setOnClickListener {
            view.findNavController().popBackStack()
        }

        val container = view.findViewById<LinearLayout>(R.id.containerInfo)

        // Load data based on title
        val dataList = when(title) {
            "চাষাবাদ পদ্ধতি" -> listOf(
                Pair("বোরো ধান চাষ", "বোরো ধানের জন্য দোআঁশ ও এঁটেল মাটি সবচেয়ে উপযোগী। বীজতলায় ২-৩টি পাতা হলে চারা রোপণ করুন।"),
                Pair("আলু চাষ প্রযুক্তি", "কার্তিক মাসের শেষার্ধ থেকে অগ্রহায়ণ মাস পর্যন্ত আলু লাগানোর উপযুক্ত সময়। সারি থেকে সারির দূরত্ব ৬০ সেমি হতে হবে।"),
                Pair("পাট চাষ", "ফাল্গুন থেকে চৈত্র মাস পর্যন্ত তোষা পাটের বীজ বোনা যায়। নিড়ানি দিয়ে আগাছা পরিষ্কার রাখা জরুরি।")
            )
            "সার ও কীটনাশক তথ্য" -> listOf(
                Pair("ইউরিয়া সার প্রয়োগ", "গাছের বৃদ্ধির জন্য ইউরিয়া অপরিহার্য। তবে অতিরিক্ত ব্যবহারে ফসল নুইয়ে পড়তে পারে। কিস্তিতে প্রয়োগ করুন।"),
                Pair("জৈব সার তৈরি", "গোবর, কচুরিপানা এবং গৃহস্থালির পচনশীল বর্জ্য গর্তে পচিয়ে উন্নত মানের জৈব সার তৈরি করা যায়।"),
                Pair("কীটনাশক সতর্কতা", "বাতাসের বিপরীতে স্প্রে করবেন না। স্প্রে করার সময় মাস্ক ও গ্লাভস ব্যবহার করুন।")
            )
            "আধুনিক যন্ত্রপাতি" -> listOf(
                Pair("পাওয়ার টিলার", "দ্রুত জমি চাষের জন্য এটি কার্যকর। এতে সময় ও শ্রম উভয়ই বাঁচে।"),
                Pair("ধান কাটার মেশিন (Reaper)", "শ্রমিক সংকট কমাতে রিপার মেশিন ব্যবহার করুন। এটি দিয়ে দ্রুত ধান ও গম কাটা যায়।"),
                Pair("সোলার সেচ পাম্প", "বিদ্যুৎ খরচ বাঁচাতে এবং নিরবচ্ছিন্ন সেচের জন্য সোলার পাম্প পরিবেশবান্ধব সমাধান।")
            )
            "প্রণোদনা কর্মসূচি" -> listOf(
                Pair("বিনামূল্যে বীজ ও সার", "ক্ষুদ্র ও প্রান্তিক কৃষকদের জন্য সরকার বিনামূল্যে আউশ ধানের বীজ ও সার বিতরণ করছে।"),
                Pair("ভর্তুকি মূল্যে যন্ত্রপাতি", "হাওর অঞ্চলের কৃষকদের জন্য ৭০% এবং অন্যান্য অঞ্চলের জন্য ৫০% ভর্তুকিতে কৃষি যন্ত্রপাতি দেওয়া হচ্ছে।")
            )
            else -> emptyList()
        }

        dataList.forEach { (head, body) ->
            val itemView = layoutInflater.inflate(R.layout.item_info_expandable, container, false)
            itemView.findViewById<TextView>(R.id.tvTitle).text = head
            itemView.findViewById<TextView>(R.id.tvDesc).text = body
            container.addView(itemView)
        }
    }
}
package com.example.pollibondhu

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

class KrishiFragment : Fragment() {

    // --- Weather API Configuration ---
    // TODO: REPLACE WITH YOUR ACTUAL OPENWEATHERMAP API KEY
    private val API_KEY = "0087ffe2715f72e0d1cca0b59e1d9481"
    private val BASE_URL = "https://api.openweathermap.org/data/2.5/"
    private val CITY = "Dhaka,bd"

    interface WeatherService {
        @GET("weather")
        suspend fun getCurrentWeather(
            @Query("q") city: String,
            @Query("units") units: String = "metric",
            @Query("lang") lang: String = "bn",
            @Query("appid") apiKey: String
        ): WeatherResponse
    }

    private lateinit var weatherProgressBar: ProgressBar
    private lateinit var weatherDataLayout: LinearLayout
    private lateinit var tvTemp: TextView
    private lateinit var tvWeatherDesc: TextView
    private lateinit var tvHumidity: TextView
    private lateinit var tvWind: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_krishi, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Weather Views init
        weatherProgressBar = view.findViewById(R.id.weatherProgressBar)
        weatherDataLayout = view.findViewById(R.id.weatherDataLayout)
        tvTemp = view.findViewById(R.id.tvTemp)
        tvWeatherDesc = view.findViewById(R.id.tvWeatherDesc)
        tvHumidity = view.findViewById(R.id.tvHumidity)
        tvWind = view.findViewById(R.id.tvWind)

        fetchWeatherData()

        setupFeaturedGrid(view.findViewById(R.id.gridFeatured))

        // আপডেটেড ফাংশন কল করা হচ্ছে
        setupListSection(view.findViewById(R.id.containerResources), getResourceData())
        setupListSection(view.findViewById(R.id.containerGov), getGovData())
    }

    // ... (fetchWeatherData এবং setupFeaturedGrid আগের মতোই থাকবে) ...
    private fun fetchWeatherData() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                val service = retrofit.create(WeatherService::class.java)
                val response = service.getCurrentWeather(CITY, apiKey = API_KEY)

                withContext(Dispatchers.Main) {
                    weatherProgressBar.visibility = View.GONE
                    weatherDataLayout.visibility = View.VISIBLE

                    tvTemp.text = "${response.main.temp.toInt()}°C"
                    tvWeatherDesc.text = response.weather.firstOrNull()?.description?.replaceFirstChar { it.uppercase() } ?: "--"
                    tvHumidity.text = "আর্দ্রতা: ${response.main.humidity}%"
                    tvWind.text = "বাতাস: ${response.wind.speed} কিমি/ঘণ্টা"
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    weatherProgressBar.visibility = View.GONE
                    tvWeatherDesc.text = "তথ্য লোড করা যায়নি"
                    Log.e("WeatherError", e.message.toString())
                }
            }
        }
    }

    private fun setupFeaturedGrid(gridLayout: GridLayout) {
        data class GridItem(val title: String, val bgColorHex: String, val iconTintHex: String, val iconRes: Int)

        val items = listOf(
            GridItem("ক্রপ ডাক্তার", "#E8F5E9", "#4CAF50", R.drawable.ic_cropdoc),
            GridItem("বাজার দর", "#FFF3E0", "#FF9800", R.drawable.ic_bajardor),
            GridItem("আবহাওয়া বার্তা", "#E3F2FD", "#2196F3", R.drawable.ic_abhawabarta),
            GridItem("কৃষি ঋণ", "#F3E5F5", "#9C27B0", R.drawable.ic_krishirin)
        )

        items.forEach { item ->
            val cardView = layoutInflater.inflate(R.layout.item_krishi_grid_card, gridLayout, false)
            val params = GridLayout.LayoutParams()
            params.width = 0
            params.height = GridLayout.LayoutParams.WRAP_CONTENT
            params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
            params.setMargins(8, 8, 8, 8)
            cardView.layoutParams = params

            val titleView = cardView.findViewById<TextView>(R.id.item_title)
            val iconBgCard = cardView.findViewById<CardView>(R.id.icon_bg_card)
            val iconView = cardView.findViewById<ImageView>(R.id.item_icon)

            titleView.text = item.title
            iconView.setImageResource(item.iconRes)
            iconBgCard.setCardBackgroundColor(Color.parseColor(item.bgColorHex))
            iconView.setColorFilter(Color.parseColor(item.iconTintHex))

            gridLayout.addView(cardView)
        }
    }


    // ১. নতুন ডাটা ক্লাস তৈরি করুন (লিস্ট আইটেমের জন্য)
    data class ListItem(val title: String, val iconRes: Int)

    // ২. setupListSection ফাংশন আপডেট করুন যেন এটি ListItem গ্রহণ করে
    private fun setupListSection(container: LinearLayout, items: List<ListItem>) {

        items.forEach { item ->
            val itemView = layoutInflater.inflate(R.layout.item_service_category, container, false)

            val iconView = itemView.findViewById<ImageView>(R.id.item_icon)
            val titleView = itemView.findViewById<TextView>(R.id.item_title)

            titleView.text = item.title

            // নির্দিষ্ট আইকন সেট করা হচ্ছে
            iconView.setImageResource(item.iconRes)

            // চাইলে ডিফল্ট টিন্ট কালার ব্যবহার করতে পারেন অথবা রিমুভ করতে পারেন
            iconView.setColorFilter(Color.parseColor("#4CAF50"))

            container.addView(itemView)
        }
    }

    // ৩. ডাটা সোর্স আপডেট করুন (টাইটেল এবং আইকন সহ)
    private fun getResourceData() = listOf(
        ListItem("চাষাবাদ পদ্ধতি", R.drawable.ic_chash),       // উদাহরণ আইকন
        ListItem("সার ও কীটনাশক তথ্য", R.drawable.ic_shar),      // উদাহরণ আইকন
        ListItem("আধুনিক যন্ত্রপাতি", R.drawable.ic_jontro) // উদাহরণ আইকন
    )

    private fun getGovData() = listOf(
        ListItem("কৃষি ভর্তুকি আবেদন", R.drawable.ic_vurtuki),
        ListItem("প্রণোদনা কর্মসূচি", R.drawable.ic_subsidy),
        ListItem("মাঠ পর্যায়ের কর্মকর্তা", R.drawable.ic_mathkormi)
    )
}
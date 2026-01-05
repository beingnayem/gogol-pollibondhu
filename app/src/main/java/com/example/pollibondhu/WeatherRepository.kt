package com.example.pollibondhu

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object WeatherRepository {

    // PASTE YOUR OPENWEATHERMAP API KEY HERE
    private const val API_KEY = "YOUR_API_KEY_HERE"

    // We use 'suspend' because network calls take time
    suspend fun getWeatherForCity(city: String): WeatherResponse? {
        return withContext(Dispatchers.IO) {
            try {
                // Translate city names to English for API (API works best with English names)
                val queryCity = when(city) {
                    "ঢাকা" -> "Dhaka"
                    "চট্টগ্রাম" -> "Chittagong"
                    "সিলেট" -> "Sylhet"
                    "রাজশাহী" -> "Rajshahi"
                    "খুলনা" -> "Khulna"
                    "বরিশাল" -> "Barisal"
                    "রংপুর" -> "Rangpur"
                    "ময়মনসিংহ" -> "Mymensingh"
                    else -> "Dhaka"
                }

                val response = RetrofitClient.instance.getCurrentWeather(queryCity, API_KEY)

                if (response.isSuccessful) {
                    response.body()
                } else {
                    Log.e("WeatherRepo", "Error: ${response.code()}")
                    null
                }
            } catch (e: Exception) {
                Log.e("WeatherRepo", "Exception: ${e.message}")
                null
            }
        }
    }
}
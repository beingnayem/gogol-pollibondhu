package com.example.pollibondhu

import com.google.gson.annotations.SerializedName

// Main response object
data class WeatherResponse(
    val main: MainStats,
    val weather: List<WeatherDescription>,
    val wind: WindStats,
    val name: String // City name
)

data class MainStats(
    val temp: Float,
    val humidity: Int
)

data class WeatherDescription(
    val description: String, // e.g., "আংশিক মেঘলা"
    val icon: String
)

data class WindStats(
    val speed: Float
)
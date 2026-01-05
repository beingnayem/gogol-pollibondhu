package com.example.pollibondhu

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    // This matches the OpenWeatherMap API URL structure
    @GET("data/2.5/weather")
    suspend fun getCurrentWeather(
        @Query("q") city: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric", // Use "metric" for Celsius
        @Query("lang") lang: String = "bn"       // Response in Bangla
    ): Response<WeatherResponse>
}
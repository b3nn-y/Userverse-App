package com.bennysamuel.weatherify.weatherApi

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    @GET("current.json?key=01037f0b0ac54c50b0050649243001&aqi=yes")
    suspend fun getWeather(@Query("q") q:String): Response<WeatherData>
    //latitude=13.0827&longitude=80.2707&

}
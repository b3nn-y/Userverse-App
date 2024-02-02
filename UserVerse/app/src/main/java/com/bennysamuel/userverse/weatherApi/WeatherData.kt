package com.bennysamuel.weatherify.weatherApi

import com.google.gson.annotations.SerializedName



data class WeatherData(
    @SerializedName("location") val location: Location,
    @SerializedName("current") val current: CurrentWeather
)
data class Location(
    @SerializedName("name") val name: String,
    @SerializedName("region") val region: String,
    @SerializedName("country") val country: String,
    @SerializedName("lat") val lat: Double,
    @SerializedName("lon") val lon: Double
)

data class Condition(
    @SerializedName("text") val text: String,
    @SerializedName("icon") val icon: String,
    @SerializedName("code") val code: Int
)

data class AirQuality(
    @SerializedName("co") val co: Double,
    @SerializedName("no2") val no2: Double,
    @SerializedName("o3") val o3: Double,
    @SerializedName("so2") val so2: Double,
    @SerializedName("pm2_5") val pm2_5: Double,
    @SerializedName("pm10") val pm10: Double,
    @SerializedName("us-epa-index") val usEpaIndex: Int,
    @SerializedName("gb-defra-index") val gbDefraIndex: Int
)

data class CurrentWeather(
    @SerializedName("temp_c") val tempC: Double,
    @SerializedName("condition") val condition: Condition,
    @SerializedName("humidity") val humidity: Int,
    @SerializedName("air_quality") val airQuality: AirQuality
)



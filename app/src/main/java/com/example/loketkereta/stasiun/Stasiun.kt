package com.example.loketkereta.stasiun

import com.google.gson.annotations.SerializedName

data class Stasiun(
    @SerializedName("code") val code: String,
    @SerializedName("name") val name: String,
    @SerializedName("city") val city: String,
    @SerializedName("cityname") val cityName: String
)
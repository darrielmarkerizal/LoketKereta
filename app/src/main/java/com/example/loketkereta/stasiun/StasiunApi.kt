package com.example.loketkereta.stasiun

import retrofit2.Call
import retrofit2.http.GET

interface StationApi {
    @GET("stations2")
    fun getStations(): Call<List<Stasiun>>
}
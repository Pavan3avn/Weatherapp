package com.pavan.weatherapp.ApiInterface


import com.pavan.weatherapp.Modelclass.WeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface Apiinterface {

    @GET("weather")
    fun getweatherdata(
        @Query("q")  city : String,
        @Query("apikey")  apikey : String,
        @Query("units") units:String
    ): Call<WeatherResponse>

}
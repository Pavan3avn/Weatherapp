package com.pavan.weatherapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import com.pavan.weatherapp.ApiInterface.Apiinterface
import com.pavan.weatherapp.Modelclass.WeatherResponse
import com.pavan.weatherapp.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*
import javax.security.auth.callback.Callback
import kotlin.math.min

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        searchcity()



    }

    private fun searchcity() {
        val searchView = binding.searchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                if (p0 != null) {
                    getWeatherdata(p0)
                }
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return true
            }

        })


    }

    private fun getWeatherdata(cityname : String){

        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Constants.BASE_URL)
            .build().create(Apiinterface::class.java)
        val response = retrofit.getweatherdata(cityname,Constants.apikey,"metric")
        response.enqueue(object : retrofit2.Callback<WeatherResponse> {
            override fun onResponse(
                call: Call<WeatherResponse>,
                response: Response<WeatherResponse>
            ) {
                val responsebody = response.body()
                if(response.isSuccessful && responsebody!=null){
                    val temperature =  responsebody.main.temp.toString()
                    val humidity = responsebody.main.humidity.toString()
                    val weather = responsebody.weather.firstOrNull()?.main?:"unknown"
                    val maxtemp = responsebody.main.temp_max.toString()
                    val mintemp = responsebody.main.temp_min.toString()
                    val sunrise = responsebody.sys.sunrise.toLong()
                    val sunset = responsebody.sys.sunset.toLong()
                    val windspeed = responsebody.wind.speed.toString()
                    val sealevel = responsebody.main.pressure.toString()

                    binding.temperature.text = "$temperature \u2103"
                    binding.weather.text = weather
                    binding.humidity.text = "$humidity %"
                    binding.maxtemp.text = "Max Temp:$maxtemp \u2103"
                    binding.mintemp.text  =  "Min Temp:$mintemp \u2103"
                    binding.windspeed.text = "$windspeed m/s"
                    binding.sea.text = "$sealevel hPa"
                    binding.feelslike.text = weather
                    binding.date.text = date()
                    binding.day.text = dayname(System.currentTimeMillis())
                    binding.cityname.text = "$cityname"
                    binding.sunrise.text = "${time(sunrise)}"
                    binding.sunset.text = "${time(sunset)}"

                    ChangeBackground(weather);


                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                Log.e("mainactivity","error occured")

            }

        })


    }

    private fun date(): String {
        val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        return sdf.format(Date())

    }
    private fun time(timestamp: Long): String {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        return sdf.format(Date(timestamp*1000))

    }

    private fun dayname(timestamp: Long):String{
        val sdf = SimpleDateFormat("EEEE", Locale.getDefault())
        return sdf.format(Date())

    }

    private fun ChangeBackground(weather : String){
        when(weather){
            "Partly clouds","Clouds","Overcast","Mist","Foggy","Haze" ->{
                binding.root.setBackgroundResource(R.drawable.colud_background)
                binding.lottieAnimationView.setAnimation(R.raw.cloud);
            }

            "Clear Sky","Sunny","Clear" ->{
                binding.root.setBackgroundResource(R.drawable.sunny_background)
                binding.lottieAnimationView.setAnimation(R.raw.sun)
            }

            "Light Rain","Drizzle","Moderate Rain","Showers","Heavy Rain","Rain" ->{
                binding.root.setBackgroundResource(R.drawable.rain_background)
                binding.lottieAnimationView.setAnimation(R.raw.rain)
            }

            "Light Snow","Moderate Snow","Heavy Snow","Blizzard" ->{
                binding.root.setBackgroundResource(R.drawable.snow_background)
                binding.lottieAnimationView.setAnimation(R.raw.snow)
            }


        }

        binding.lottieAnimationView.playAnimation()

    }

}
package com.weatherapp.api

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import coil.ImageLoader
import coil.request.ImageRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherService(private val context : Context) {
    private val imageLoader = ImageLoader.Builder(context)
        .allowHardware(false).build()
        private var weatherAPI: WeatherServiceAPI
    init {
        val retrofitAPI = Retrofit.Builder().baseUrl(WeatherServiceAPI.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()
        weatherAPI = retrofitAPI.create(WeatherServiceAPI::class.java)
    }
    fun getName(lat: Double, lng: Double, onResponse : (String?) -> Unit ) {
        search("$lat,$lng") { loc -> onResponse (loc?.name) }
    }
    fun getLocation(name: String, onResponse: (lat:Double?, long:Double?) -> Unit) {
        search(name) { loc -> onResponse (loc?.lat, loc?.lon) }
    }
    private fun search(query: String, onResponse : (APILocation?) -> Unit) {
        val call: Call<List<APILocation>?> = weatherAPI.search(query)
        call.enqueue(object : Callback<List<APILocation>?> {
            override fun onResponse(call: Call<List<APILocation>?>,
                                    response: Response<List<APILocation>?>
            ) {
                onResponse(response.body()?.let {if (it.isNotEmpty()) it[0] else null})
            }
            override fun onFailure(call: Call<List<APILocation>?>,t: Throwable) {
                Log.w("WeatherApp WARNING", "" + t.message)
                onResponse(null)
            }
        })
    }

    fun getWeather(name: String, onResponse: (APICurrentWeather?) -> Unit){
        val call: Call<APICurrentWeather?> = weatherAPI.weather(name)
        enqueue(call) { onResponse.invoke(it) }
    }
    fun getForecast(name: String, onResponse : (APIWeatherForecast?) -> Unit) {
        val call: Call<APIWeatherForecast?> = weatherAPI.forecast(name)
        enqueue(call) { onResponse.invoke(it) }
    }
    fun getBitmap(imgUrl: String, onResponse: (Bitmap?) -> Unit) {
        val request = ImageRequest.Builder(context)
            .data(imgUrl).allowHardware(false).target(
                onSuccess = { drawable ->
                    val bitmap = (drawable as BitmapDrawable).bitmap
                    onResponse(bitmap)
                },
                onError = { /* handle failure */ }
            )
            .build()
        imageLoader.enqueue(request)
    }
    private fun <T> enqueue(call: Call<T?>, onResponse: (T?) -> Unit) {
        call.enqueue(object : Callback<T?> {
            override fun onResponse(call: Call<T?>, response: Response<T?>) {
                onResponse(response.body())
            }
            override fun onFailure(call: Call<T?>, t: Throwable) {
                Log.w("WeatherApp WARNING", "" + t.message)
                onResponse(null)
            }
        })
    }
}
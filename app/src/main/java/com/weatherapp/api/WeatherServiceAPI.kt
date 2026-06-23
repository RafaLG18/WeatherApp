package com.weatherapp.api

import android.util.Log
import com.weatherapp.BuildConfig
import com.weatherapp.api.WeatherServiceAPI.Companion.API_KEY
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherServiceAPI {
    companion object {
        const val BASE_URL = "https://api.weatherapi.com/v1/"
        const val API_KEY = BuildConfig.WEATHER_API_KEY
    }
    // Procura a localização baseado no nome ou coordenadas
    @GET("search.json?key=$API_KEY&lang=pt_br")
    fun search(@Query("q") query: String): Call<List<APILocation>?>
    @GET("current.json?key=$API_KEY&lang=pt")
    fun weather(@Query("q") query: String): Call<APICurrentWeather?>
    @GET("forecast.json?key=$API_KEY&days=10&lang=pt")
    fun forecast(@Query("q") name: String): Call<APIWeatherForecast?>
    private fun <T> enqueue(call : Call<T?>, onResponse : ((T?) -> Unit)? = null){
        call.enqueue(object : Callback<T?> {
            override fun onResponse(call: Call<T?>, response: Response<T?>) {
                val obj: T? = response.body()
                onResponse?.invoke(obj)
            }
            override fun onFailure(call: Call<T?>, t: Throwable) {
                Log.w("WeatherApp WARNING", "" + t.message)
            }
        })
    }
}


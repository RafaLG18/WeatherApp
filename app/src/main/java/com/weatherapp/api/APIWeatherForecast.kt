package com.weatherapp.api

import com.weatherapp.model.Forecast

data class APIWeatherForecast (
    var location: APILocation? = null,
    var current: APIWeatherForecast? = null,
    var forecast: APIForecast? = null
)

data class APIForecast ( var forecastday: List<APIForecastDay>? = null )
data class APIForecastDay ( var date: String? = null, var day: APIWeather? = null )
fun APIWeatherForecast.toForecast(): List<Forecast>? {
    return forecast?.forecastday?.map {
        Forecast(
            date = it.date ?: "00-00-0000",
            weather = it.day?.condition?.text ?: "Erro carregando!",
            tempMin = it.day?.mintemp_c ?: -1.0,
            tempMax = it.day?.maxtemp_c ?: -1.0,
            imgUrl = ("https:" + it.day?.condition?.icon)
        )
    }
}
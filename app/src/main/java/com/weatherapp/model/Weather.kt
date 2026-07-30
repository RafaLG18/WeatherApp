package com.weatherapp.model

import android.graphics.Bitmap

data class Weather (
    val date: String, val desc: String,
    val temp: Double, val imgUrl: String,
    var bitmap: Bitmap? = null) {
    companion object {
        val LOADING = Weather(date = "LOADING", desc = "LOADING",
            temp = -1.0, imgUrl = "LOADING", bitmap = null )
        val ERROR = Weather(
            date = "ERROR",
            desc = "ERROR",
            temp = -999.0,
            imgUrl = "ERROR",
            bitmap = null
        )
    }
}
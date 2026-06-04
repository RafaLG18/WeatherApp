package com.weatherapp.model

import com.google.android.gms.maps.model.LatLng
import com.weatherapp.db.fb.FBCity

data class City (
    val name : String,
    val weather: String? = null,
    val location: LatLng? = null
) {
    fun toFBCity(): FBCity {
        val fbCity = FBCity()
        fbCity.name = this.name
        fbCity.lat = this.location?.latitude ?: 0.0
        fbCity.lng = this.location?.longitude ?: 0.0
        return fbCity
    }
}
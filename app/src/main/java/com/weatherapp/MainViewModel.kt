package com.weatherapp

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.model.LatLng
import com.weatherapp.api.WeatherService
import com.weatherapp.api.toForecast
import com.weatherapp.api.toWeather
import com.weatherapp.db.fb.FBCity
import com.weatherapp.db.fb.FBDatabase
import com.weatherapp.db.fb.FBUser
import com.weatherapp.model.City
import com.weatherapp.model.Forecast
import com.weatherapp.model.User
import com.weatherapp.model.Weather
import com.weatherapp.ui.nav.Route

class MainViewModel (private val db: FBDatabase, private val service : WeatherService): ViewModel(),
    FBDatabase.Listener {
    private var _page = mutableStateOf<Route>(Route.Home)
    var page: Route
        get() = _page.value
        set(tmp) { _page.value = tmp }
    private var _city = mutableStateOf<String?>(null)
    var city: String?
        get() = _city.value
        set(tmp) { _city.value = tmp }
    private val _cities = mutableStateMapOf<String, City>()
    private val _forecast = mutableStateMapOf<String, List<Forecast>?>()
    val cities : List<City>
        get() = _cities.values.toList().sortedBy { it.name }
    private val _weather = mutableStateMapOf<String, Weather>()
    private val _user = mutableStateOf<User?>(null)
    val user: User?
        get() = _user.value

    init {
        db.setListener(this)
    }

    fun remove(city: City) {
        db.remove(city.toFBCity())
    }

    fun add(name: String, location: LatLng? = null) {
        db.add(City(name = name, location = location).toFBCity())
    }

    override fun onUserLoaded(user: FBUser) {
        _user.value = user.toUser()
    }

    override fun onUserSignOut() {
        //TODO("Not yet implemented")
    }

    override fun onCityAdded(city: FBCity) {
        _cities[city.name!!] = city.toCity()
    }
    override fun onCityUpdated(city: FBCity) {
        _cities.remove(city.name)
        _cities[city.name!!] = city.toCity()
    }
    override fun onCityRemoved(city: FBCity) {
        _cities.remove(city.name)
    }

    fun addCity(name: String) {
        service.getLocation(name) { lat, lng ->
            if (lat != null && lng != null) {
                db.add(City(name = name, location = LatLng(lat, lng)).toFBCity())
            }
        }
    }

    fun addCity(location: LatLng) {
        service.getName(location.latitude, location.longitude) { name ->
            if (name != null) {
                db.add(City(name = name, location = location).toFBCity())
            }
        }

    }
    fun weather (name: String) = _weather.getOrPut(name) {
        loadWeather(name)
        Weather.LOADING // retorno
    }
    fun forecast (name: String) = _forecast.getOrPut(name) {
        loadForecast(name)
        emptyList() // return
    }
    private fun loadWeather(name: String) {
        service.getWeather(name) { apiWeather ->
            apiWeather?.let {
                _weather[name] = apiWeather.toWeather()
            }
        }
    }
    private fun loadForecast(name: String) {
        service.getForecast(name) { apiForecast ->
            apiForecast?.let {
                _forecast[name] = apiForecast.toForecast()
            }
        }
    }
    class MainViewModelFactory(private val db: FBDatabase, private val service: WeatherService) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                return MainViewModel(db, service) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
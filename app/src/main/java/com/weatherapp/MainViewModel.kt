package com.weatherapp

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.weatherapp.api.WeatherService
import com.weatherapp.api.toForecast
import com.weatherapp.api.toWeather
import com.weatherapp.model.City
import com.weatherapp.model.Forecast
import com.weatherapp.model.User
import com.weatherapp.model.Weather
import com.weatherapp.monitor.ForecastMonitor
import com.weatherapp.repo.Repository
import com.weatherapp.ui.nav.Route
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.collections.emptyMap

class MainViewModel (private val repo: Repository, private val service : WeatherService,
                     private val monitor : ForecastMonitor): ViewModel() {
    private var _city = mutableStateOf<String?>(null)
    var city: String?
        get() = _city.value
        set(tmp) { _city.value = tmp }
    private var _page = mutableStateOf<Route>(Route.Home)
    var page: Route
        get() = _page.value
        set(tmp) { _page.value = tmp }
    private val _cities : Flow<Map<String, City>> = repo.cities.map {
            cityList -> cityList.associateBy { it.name }
    }
    val cities = _cities.stateIn(viewModelScope, SharingStarted.Lazily, emptyMap())
    private val _weather = MutableStateFlow<Map<String, Weather>>(emptyMap())
    val weather = _weather.asSharedFlow()
    private val _forecast = MutableStateFlow<Map<String, List<Forecast>?>>(emptyMap())
    val forecast = _forecast.asSharedFlow()
    val user = repo.user.stateIn(viewModelScope, SharingStarted.Lazily, null)
    fun remove(city: City) {
        repo.remove(city)
        monitor.cancelCity(city)
    }
    fun update(city: City) {
        repo.update(city)
        monitor.updateCity(city)
    }
    fun addCity(name: String) = viewModelScope.launch(Dispatchers.IO) {
        val location = service.getLocation(name)
        repo.add(City( name = name, location = location))
    }
    fun addCity(location: LatLng) = viewModelScope.launch(Dispatchers.IO) {
        val name = service.getName(location.latitude, location.longitude)
        if (name != null) repo.add(City( name = name, location = location))
    }
    fun loadWeather(name: String) {
        if (_weather.value[name] != null) return
        viewModelScope.launch(Dispatchers.Main) {
            // Status temporário: carregando
            _weather.update { current -> current + (name to Weather.LOADING) }
            runCatching {
                service.getWeather(name)?.toWeather()
            }.onSuccess { weather ->
                _weather.update { curr -> curr + (name to (weather?:Weather.ERROR)) }
            }.onFailure { e ->
                _weather.update { curr -> curr + (name to Weather.ERROR) }
            }
        }
    }
    fun loadForecast(name: String) {
        if (_forecast.value[name] != null) return
        viewModelScope.launch(Dispatchers.Main) {
            runCatching {
                service.getForecast(name)?.toForecast()
            }.onSuccess { forecast ->
                _forecast.update { curr -> curr + (name to forecast) }
            }.onFailure { e ->
                _forecast.update { curr -> curr + (name to null) }
            }
        }
    }
    fun loadBitmap(name: String) {
        val weather = _weather.value[name]
        if (weather == null || weather == Weather.LOADING || weather == Weather.ERROR ||
            weather.bitmap != null
        ) return
        viewModelScope.launch(Dispatchers.Main) {
            runCatching {
                service.getBitmap(weather.imgUrl)
            }.onSuccess { bitmap ->
                _weather.update { curr -> curr + (name to weather.copy(bitmap = bitmap)) }
            }.onFailure { e ->
                _weather.update { curr -> curr + (name to Weather.ERROR) }
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    class MainViewModelFactory(
        private val repo: Repository,
        private val service: WeatherService,
        private val monitor: ForecastMonitor
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MainViewModel(repo, service, monitor) as T
        }
    }
}
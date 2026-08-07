package com.weatherapp.ui

import com.weatherapp.MainViewModel
import android.content.pm.PackageManager
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.scale
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.weatherapp.R
import com.weatherapp.model.Weather

@Composable
fun MapPage(modifier: Modifier= Modifier,
            viewModel: MainViewModel){
    val camPosState = rememberCameraPositionState ()
    val context = LocalContext.current
    val hasLocationPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context,
                android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED
        )
    }

    GoogleMap( modifier = modifier.fillMaxSize(), cameraPositionState = camPosState, onMapClick = {
        viewModel.addCity(location = it)},
        properties = MapProperties(isMyLocationEnabled = hasLocationPermission),
        uiSettings = MapUiSettings(myLocationButtonEnabled = true)) {
        val cities = viewModel.cities.collectAsStateWithLifecycle(emptyMap()).value
        val weatherMap = viewModel.weather
            .collectAsStateWithLifecycle(emptyMap()).value
        cities.values.forEach {
            if (it.location != null) {
                val weather = weatherMap[it.name]?:Weather.LOADING
                LaunchedEffect(it.name) {
                    viewModel.loadWeather(it.name)
                }
                LaunchedEffect(weather) {
                    viewModel.loadBitmap(it.name)
                }
                val bitmap = weather.bitmap
                    ?: getDrawable(context, R.drawable.loading)?.toBitmap()
                val icon = bitmap?.let {
                    BitmapDescriptorFactory.fromBitmap(it.scale(120, 120))
                }
                Marker(
                    state = MarkerState(position = it.location!!),
                    title = it.name,
                    snippet = weather.desc,
                    icon = icon
                )
            }
        }
    }
}
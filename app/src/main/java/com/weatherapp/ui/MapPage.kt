package com.weatherapp.ui

import MainViewModel
import android.content.pm.PackageManager
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ModalBottomSheetDefaults.properties
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun MapPage(modifier: Modifier= Modifier,
            viewModel: MainViewModel){
    val recife = remember { MarkerState(LatLng(-8.05, -34.9)) }
    val caruaru = remember { MarkerState(LatLng(-8.27, -35.98)) }
    val joaopessoa = remember { MarkerState( LatLng(-7.12, -34.84)) }
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
        viewModel.add("Cidade@${it.latitude}:${it.longitude}", location = it)},
        properties = MapProperties(isMyLocationEnabled = hasLocationPermission),
        uiSettings = MapUiSettings(myLocationButtonEnabled = true)) {
        viewModel.cities.forEach {
            if (it.location != null) {
                Marker(
                    state = MarkerState(position = it.location),
                    title = it.name,
                    snippet = "${it.location}"
                )
            }
        }
        Marker(
            state = recife,
            title = "Recife",
            snippet = "Marcador em Recife",
            icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)
        )
        Marker(
            state = caruaru,
            title = "Caruaru",
            snippet = "Marcador em Caruaru",
            icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)
        )
        Marker(
            state = joaopessoa,
            title = "João Pessoa",
            snippet = "Marcador em João Pessoa",
            icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
        )
    }

}
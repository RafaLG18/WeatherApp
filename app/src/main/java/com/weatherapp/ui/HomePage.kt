package com.weatherapp.ui

import com.weatherapp.MainViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.weatherapp.R
import com.weatherapp.model.Forecast
import java.text.DecimalFormat

//@Preview(showBackground = true)
@Composable
fun HomePage(modifier: Modifier = Modifier,viewModel: MainViewModel) {
    Column {
        if (viewModel.city == null) {
            Column( modifier = modifier.fillMaxSize()
                .background(Color.Blue).wrapContentSize(Alignment.Center)
            ) {
                Text( text = "Selecione uma cidade!",
                    fontWeight = FontWeight.Bold, color = Color.White,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    textAlign = TextAlign.Center, fontSize = 28.sp )
            }
        } else {
            val cities = viewModel.cities.collectAsStateWithLifecycle(emptyMap()).value
            val city = cities[viewModel.city!!]
            val weather = viewModel.weather.collectAsStateWithLifecycle(emptyMap())
                .value[viewModel.city!!]
            val icon = if (city?.isMonitored == true) Icons.Filled.Notifications else
                Icons.Outlined.Notifications
            val forecasts = viewModel.forecast.collectAsStateWithLifecycle(emptyMap())
                .value[viewModel.city!!]
            LaunchedEffect(viewModel.city!!) {
                viewModel.loadForecast(viewModel.city!!)
            }

            forecasts?.let { forecasts ->
                LazyColumn {
                    items(items = forecasts) { forecast ->
                        ForecastItem(forecast, onClick = { })
                    }
                }
            }
        }
    }
}

@Composable
fun ForecastItem(
    forecast: Forecast,
    modifier: Modifier = Modifier,
    onClick: (Forecast) -> Unit
) {
    val format = DecimalFormat("#.0")
    val tempMin = format.format(forecast.tempMin)
    val tempMax = format.format(forecast.tempMax)
    Row(
        modifier = modifier.fillMaxWidth().padding(12.dp)
            .clickable( onClick = { onClick(forecast) }),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage( // Substitui o Icon
            model = forecast.imgUrl,
            modifier = modifier.size(70.dp),
            error = painterResource(id = R.drawable.loading),
            contentDescription = "Imagem"
        )
        Spacer(modifier = modifier.size(16.dp))
        Column {
            Text(modifier = modifier, text = forecast.weather, fontSize = 24.sp)
            Row {
                Text(modifier = modifier, text = forecast.date, fontSize = 20.sp)
                Spacer(modifier = modifier.size(12.dp))
                Text(modifier = modifier, text = "Min: $tempMin℃", fontSize = 16.sp)
                Spacer(modifier = modifier.size(12.dp))
                Text(modifier = modifier, text = "Max: $tempMax℃", fontSize = 16.sp)
            }
        }
    }
}

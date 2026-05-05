package com.weatherapp.ui.nav

import MainViewModel
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.weatherapp.ui.HomePage
import com.weatherapp.ui.ListPage
import com.weatherapp.ui.MapPage

@Composable
fun MainNavHost(navController: NavHostController, modifier: Modifier = Modifier,viewModel:MainViewModel) {
    NavHost(navController, startDestination = Route.Home) {
        composable<Route.Home> { HomePage(modifier = modifier,viewModel) }
        composable<Route.List> { ListPage(modifier = modifier,viewModel) }
        composable<Route.Map> { MapPage(modifier = modifier,viewModel) }
    }
}
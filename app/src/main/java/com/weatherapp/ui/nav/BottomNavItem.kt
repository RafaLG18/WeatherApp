package com.weatherapp.ui.nav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.weatherapp.ui.HomePage
import com.weatherapp.ui.ListPage
import com.weatherapp.ui.MapPage
import kotlinx.serialization.Serializable // em caso de erro
sealed interface Route {
    @Serializable
    data object Home : Route
    @Serializable
    data object List : Route
    @Serializable
    data object Map : Route
}
sealed class BottomNavItem(
    val title: String,
    val icon: ImageVector,
    val route: Route)
{
    data object HomeButton :
        BottomNavItem("Início", Icons.Default.Home, Route.Home)
    data object ListButton :
        BottomNavItem("Favoritos", Icons.Default.Favorite, Route.List)
    data object MapButton :
        BottomNavItem("Mapa", Icons.Default.LocationOn, Route.Map)
}


package com.mrm.tirewise.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.mrm.tirewise.R

@Composable
fun DrawerMenuItems(menuItems: (List<MenuItem>) -> Unit) {
    menuItems(listOf(
        MenuItem(
            title = "Home",
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Filled.Home,
            destination = NavDestination.Dashboard.route
        ),
        MenuItem(
            title = "Reminders",
            selectedIcon = Icons.Filled.Notifications,
            unselectedIcon = Icons.Filled.Notifications,
            destination = NavDestination.Reminders.route
        ),
        MenuItem(
            title = "My Vehicles",
            selectedIcon = ImageVector.vectorResource(R.drawable.garage_car_24),
            unselectedIcon = ImageVector.vectorResource(R.drawable.garage_car_24),
            destination = NavDestination.VehicleList.route
        ),
        MenuItem(
            title = "Tire Catalog",
            selectedIcon = Icons.Filled.Place,
            unselectedIcon = Icons.Filled.Place,
            destination = NavDestination.TireCatalog.route
        ),



        MenuItem(
            title = "Settings",
            selectedIcon = Icons.Filled.Settings,
            unselectedIcon = Icons.Filled.Settings,
            destination = NavDestination.Settings.route
        ),
    )
    )
}
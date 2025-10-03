package com.mrm.tirewise.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Close
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavDestination(val route: String, val title : String, val icon : ImageVector) {
    data object Splash : NavDestination(route = "splash_screen", title = "", icon = Icons.Rounded.Close)
    data object Welcome : NavDestination(route = "welcome_screen", title = "", icon = Icons.Rounded.Close)
    data object Dashboard : NavDestination(route = "dashboard_screen", title = "Home", icon = Icons.Rounded.Close)
    data object Camera : NavDestination(route = "camera_screen", title = "", icon = Icons.Rounded.Close)
    data object ResultScreen : NavDestination(route = "result_screen", title = "Scan Result", icon = Icons.Rounded.CheckCircle)
    data object VehicleForm : NavDestination(route = "add_vehicle_screen", title = "Add Vehicle Form", icon = Icons.Rounded.Add)
    data object VehicleList: NavDestination(route = "vehicle_list_screen", title = "My Vehicles", icon = Icons.Rounded.Close)
    data object Reminders: NavDestination(route = "reminders_screen", title = "Reminders", icon = Icons.Rounded.Close)
    data object Settings: NavDestination(route = "settings_screen", title = "Settings", icon = Icons.Rounded.Close)
    data object TireCatalog: NavDestination(route = "tire_catalog_screen", title = "Tire Catalog", icon = Icons.Rounded.Close)
    data object TireList: NavDestination(route = "tire_list_screen", title = "Tire List", icon = Icons.Rounded.Close)
    data object ReminderForm: NavDestination(route = "reminder_form_screen", title = "Add Reminder", icon = Icons.Rounded.Add)
    data object TireScanScreen: NavDestination(route = "tire_scan_screen", title = "Tire Scan", icon = Icons.Rounded.Close)
}
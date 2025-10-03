package com.mrm.tirewise.navigation

import androidx.compose.ui.graphics.vector.ImageVector

data class MenuItem(
    val title:String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val badgeCount: Int?=null,
    val destination: String /* e.g.  NavDestination.Dashboard.route */
)
//ther is no index because we have the index built in in main activity drawer
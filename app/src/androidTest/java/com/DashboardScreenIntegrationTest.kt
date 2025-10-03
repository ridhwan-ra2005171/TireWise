package com

import androidx.compose.runtime.State
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.rememberNavController
import com.mrm.tirewise.networkConnectivity.ConnectivityObserver
import com.mrm.tirewise.model.Reminder
import com.mrm.tirewise.model.Vehicle
import com.mrm.tirewise.view.screens.dashboard.DashboardContent
import com.mrm.tirewise.view.screens.dashboard.DashboardScreen
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test

class DashboardScreenIntegrationTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testDashboardScreenDisplayed() {
        // Mock necessary dependencies
        val currentVehicle = MutableStateFlow<Vehicle?>(null)
        // Launch the DashboardScreen
        composeTestRule.setContent {
            DashboardScreen(
                networkState = ConnectivityObserver.Status.Available as State<ConnectivityObserver.Status>, // TODO: Idk if this raises an error
                vehicleList = emptyList(),
                reminderList = emptyList(),
                userData = null,
                currentVehicle = currentVehicle,
                isFetchingVehicles = MutableStateFlow(false),
                isFetchingReminders =  MutableStateFlow(false),
                navController = rememberNavController(),
                onLogout = {},
                changeCurrentVehicle = {},
                onVehicleCardClick = {},
                onCameraClick = {},
                onAddVehicle = {},
                onViewFrontRightTireHistory = {},
                onViewBackRightTireHistory = {},
                onViewFrontLeftTireHistory = {},
                onViewBackLeftTireHistory = {},
                onViewAllTireHistory = {},
                onAddReminderClick = {},
                onDeleteReminder = {},
            )
        }

        // Verify if the DashboardScreen is displayed
        composeTestRule.onNodeWithTag("dashboard_screen").assertIsDisplayed()
        //add other assertionst to check more
    }

    @Test
    fun testDashboardButtonsClick() {
        // Mock data for testing
        val vehicleList = listOf<Vehicle>()
        val reminderList = listOf<Reminder>()
        val currentVehicle = MutableStateFlow<Vehicle?>(null)

        // Launch the DashboardContent with mock data
        composeTestRule.setContent {
            DashboardContent(
                vehicleList = vehicleList,
                reminderList = reminderList,
                isFetchingReminders =  MutableStateFlow(false),
                onCurrentVehicleChange = {},
                onVehicleCardClick = {},
                onCameraClick = {
                    composeTestRule.onNodeWithTag("scan_tire_button").performClick()
                },
                onAddVehicle = {
                               composeTestRule.onNodeWithTag("addVehicle").performClick()
                },
                //for the tires, we used DUO so a single tag was used for 2 composables.
                onViewFrontRightTireHistory = {
                    composeTestRule.onNodeWithTag("right_tire_button").performClick()
                },
                onViewBackRightTireHistory = {
                    composeTestRule.onNodeWithTag("right_tire_button").performClick()
                },
                onViewFrontLeftTireHistory = {
                    composeTestRule.onNodeWithTag("left_tire_button").performClick()
                },
                onViewBackLeftTireHistory = {
                    composeTestRule.onNodeWithTag("left_tire_button").performClick()
                },
                onViewAllTireHistory = {
                    composeTestRule.onNodeWithTag("view_all_tire_history_button").performClick()
                },
                onAddReminderClick = {
                    composeTestRule.onNodeWithTag("add_reminder_button").performClick()
                },
                onDeleteReminder = {},
                currentVehicle = currentVehicle
            )
        }




    }

}
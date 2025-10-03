package com
import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.mrm.tirewise.navigation.NavDestination
import com.test.tirewise.navigation.AppNavigator
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class NavigationIntegrationTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var navController: TestNavHostController

    @Before
    fun setupTireWiseNavHost(){
        composeTestRule.setContent {
            navController = TestNavHostController(LocalContext.current).apply {
                navigatorProvider.addNavigator(ComposeNavigator())
            }
            AppNavigator(navController = navController)
        }
    }

    @Test
    fun verifyStartDestination() {
        navController.assertCurrentRouteName(NavDestination.Welcome.route)
    }

    @Test
    fun navigateToDashboard() {
        composeTestRule.onNodeWithText("Continue with Google").performClick()
        navController.navigate(NavDestination.Dashboard.route)
        navController.assertCurrentRouteName(NavDestination.Dashboard.route)
    }

    @Test
    fun navigateToCamera() {
        composeTestRule.onNodeWithTag("Camera").performClick()
        navController.navigate(NavDestination.Camera.route)
        navController.assertCurrentRouteName(NavDestination.Camera.route)
    }

    @Test
    fun navigateToResultScreen() {
        composeTestRule.onNodeWithTag("ResultScreen").performClick()
        navController.navigate(NavDestination.ResultScreen.route)
        navController.assertCurrentRouteName(NavDestination.ResultScreen.route)
    }

    @Test
    fun navigateToVehicleList() {
        composeTestRule.onNodeWithTag("VehicleList").performClick()
        navController.navigate(NavDestination.VehicleList.route)
        navController.assertCurrentRouteName(NavDestination.VehicleList.route)
    }

    @Test
    fun navigateToAddVehicleForm() {
        composeTestRule.onNodeWithTag("AddVehicleForm").performClick()
        navController.navigate(NavDestination.VehicleForm.route)
        navController.assertCurrentRouteName(NavDestination.VehicleForm.route)
    }

    @Test
    fun navigateToTireScanScreen() {
        composeTestRule.onNodeWithTag("TireScanScreen").performClick()
        navController.navigate(NavDestination.TireScanScreen.route)
        navController.assertCurrentRouteName(NavDestination.TireScanScreen.route)
    }

    @Test
    fun navigateToReminderScreen() {
        composeTestRule.onNodeWithTag("ReminderScreen").performClick()
        navController.navigate(NavDestination.Reminders.route)
        navController.assertCurrentRouteName(NavDestination.Reminders.route)
    }

    @Test
    fun navigateToReminderForm() {
        composeTestRule.onNodeWithTag("ReminderForm").performClick()
        navController.navigate(NavDestination.ReminderForm.route)
        navController.assertCurrentRouteName(NavDestination.ReminderForm.route)
    }

    @Test
    fun navigateToTireCatalog() {
        composeTestRule.onNodeWithTag("TireCatalog").performClick()
        navController.navigate(NavDestination.TireCatalog.route)
        navController.assertCurrentRouteName(NavDestination.TireCatalog.route)
    }

    @Test
    fun navigateToSettings() {
        composeTestRule.onNodeWithTag("Settings").performClick()
        navController.navigate(NavDestination.Settings.route)
        navController.assertCurrentRouteName(NavDestination.Settings.route)
    }

    @Test
    fun navigateToTireScan() {
        composeTestRule.onNodeWithText("or continue as a guest").performClick()
        navController.navigate(NavDestination.TireScanScreen.route)
        navController.assertCurrentRouteName(NavDestination.TireScanScreen.route)
    }







}
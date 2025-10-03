package com

import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.compose.rememberNavController
import com.google.common.truth.Truth.assertThat
import com.mrm.tirewise.model.UserData
import com.mrm.tirewise.model.Reminder
import com.mrm.tirewise.view.screens.ReminderScreen
import com.mrm.tirewise.view.screens.ReminderScreenContent
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test

class ReminderIntegrationTest {

    @get:Rule
    val composeTestRule = createComposeRule()
    @Test
    fun reminderScreenContent_showsNoRemindersText_onEmptyReminders() {

        val mockReminders = emptyList<Reminder>()
        val mockOnAddReminderClick: () -> Unit = {}

        composeTestRule.setContent {
            ReminderScreenContent(mockReminders, MutableStateFlow(false), mockOnAddReminderClick,{})
        }

        // Verify text content
        assertThat(composeTestRule.onNodeWithTag("noRemindertxt").isDisplayed())
        assertThat(composeTestRule.onNodeWithTag("No reminders Found").isDisplayed())

    }

    @Test
    fun reminderScreenContent_showsReminders() {

        val reminderList = listOf(
            Reminder(
                "1",
                "",
                "vehicle1",
                "",
                "Oil Change",
                "2022-02-20 09:00",
                "Maintenance",
                "Change oil every 5000 miles",
            ),
            Reminder(
                "2",
                "",
                "vehicle2",
                "",
                "Tire Rotation",
                "2022-02-25 14:30",
                "Maintenance",
                "Rotate tires every 10000 miles",
            ),
            Reminder(
                "3",
                "",
                "vehicle3",
                "",
                "Insurance Renewal",
                "2022-03-10 10:45",
                "Insurance",
                "Renew vehicle insurance by the due date",
            )
        )

// Assign the reminder list directly to mockReminders
        val mockReminders = reminderList



        val mockOnAddReminderClick: () -> Unit = {}

        composeTestRule.setContent {
            ReminderScreen(reminders = mockReminders, isFetchingReminders =  MutableStateFlow(false), navController = rememberNavController(), userData = UserData( "1", "user1", "",""), onAddReminderClick = mockOnAddReminderClick, onLogout = {}, onDeleteReminder = {})
        }

        // Verify text content
        composeTestRule.onNodeWithText("Insurance Renewal").isDisplayed()
        composeTestRule.onNodeWithText("Tire Rotation").isDisplayed()
        composeTestRule.onNodeWithText("Oil Change").isDisplayed()

    }

}
package com

import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.google.common.truth.Truth.assertThat
import com.mrm.tirewise.model.Reminder
import com.mrm.tirewise.model.Vehicle
import com.mrm.tirewise.view.screens.ReminderForm
import org.junit.Rule
import org.junit.Test

class ReminderFormIntegrationTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun reminderFormBasicScreen_isdisplayed() {
        val vehicleList = listOf(
            Vehicle (
                "1",
                "vehicle1",
                "911",
                "suzuki",
                "Jimny",
                "Taliban Transport",
            ),
            Vehicle (
                "2",
                "vehicle2",
                "12322",
                "LOLO",
                "Honda",
                "Granny big truck",
            ),
        )

        val mockVehicleList= vehicleList


        val mockOnSubmitReminder: (Reminder) -> Unit = {}
        val mockOnCancel: () -> Unit = {}
        val userId = "user123"

        composeTestRule.setContent {
            ReminderForm(
                vehicleList = mockVehicleList,
                onSubmitReminder = mockOnSubmitReminder,
                onCancel = { mockOnCancel },
                userId = userId,
                networkState = networkState
            )
        }

        assertThat(composeTestRule.onNodeWithText("Tap here to choose your vehicle").isDisplayed())
        assertThat(composeTestRule.onNodeWithText("Tire Checkup").isDisplayed())
        assertThat(composeTestRule.onNodeWithText("Tire Rotation").isDisplayed())
        assertThat(composeTestRule.onNodeWithText("Tire Replacement").isDisplayed())
        assertThat(composeTestRule.onNodeWithText("Tire Alignment").isDisplayed())
        assertThat(composeTestRule.onNodeWithText("Oil Change").isDisplayed())
        assertThat(composeTestRule.onNodeWithText("Insurance Renewal").isDisplayed())

        assertThat(composeTestRule.onNodeWithText("Insurance Renewal").isDisplayed())

        assertThat(composeTestRule.onNodeWithText("Select Date").isDisplayed())
        assertThat(composeTestRule.onNodeWithText("Select Date").isDisplayed())



        assertThat(composeTestRule.onNodeWithText("Submit").isDisplayed())
        assertThat(composeTestRule.onNodeWithText("Cancel").isDisplayed())



    }

    @Test
    fun reminderForm_ButtonsTest(){
        val vehicleList = listOf(
            Vehicle (
                "1",
                "vehicle1",
                "911",
                "suzuki",
                "Jimny",
                "Taliban Transport",
            ),
            Vehicle (
                "2",
                "vehicle2",
                "12322",
                "LOLO",
                "Honda",
                "Granny big truck",
            ),
        )

        val mockVehicleList= vehicleList


        val mockOnSubmitReminder: (Reminder) -> Unit = {}
        val mockOnCancel: () -> Unit = {}
        val userId = "user123"

        composeTestRule.setContent {
            ReminderForm(
                vehicleList = mockVehicleList,
                onSubmitReminder = mockOnSubmitReminder,
                onCancel = { mockOnCancel },
                userId = userId,
                networkState = networkState
            )
        }

        assertThat(composeTestRule.onNodeWithText("Select Date").isDisplayed()).isTrue()

        composeTestRule.onNodeWithText("Select Date").performClick()

        assertThat(composeTestRule.onNodeWithText("Select Time").isDisplayed()).isTrue()
        composeTestRule.onNodeWithText("Select Time").performClick()

        assertThat(composeTestRule.onNodeWithText("Submit").isDisplayed()).isTrue()

        composeTestRule.onNodeWithText("Submit").performClick()

        assertThat(composeTestRule.onNodeWithText("Cancel").isDisplayed()).isTrue()

        composeTestRule.onNodeWithText("Cancel").performClick()


    }


    @Test
    fun reminderForm_VehicleSelectionsTest(){
        val vehicleList = listOf(
            Vehicle (
                "1",
                "vehicle1",
                "911",
                "suzuki",
                "Jimny",
                "Subway Surfer Transport",
            ),
            Vehicle (
                "2",
                "vehicle2",
                "12322",
                "LOLO",
                "Honda",
                "Granny big truck",
            ),
        )

        val mockVehicleList= vehicleList


        val mockOnSubmitReminder: (Reminder) -> Unit = {}
        val mockOnCancel: () -> Unit = {}
        val userId = "user123"

        composeTestRule.setContent {
            ReminderForm(
                vehicleList = mockVehicleList,
                onSubmitReminder = mockOnSubmitReminder,
                onCancel = { mockOnCancel },
                userId = userId,
                networkState = networkState
            )
        }

        assertThat(composeTestRule.onNodeWithText("Tap here to choose your vehicle").isDisplayed())

        composeTestRule.onNodeWithTag("chooseVehicle").performClick()

        assertThat(composeTestRule.onNodeWithText("Subway Surfer Transport").isDisplayed())

        assertThat(composeTestRule.onNodeWithText("Granny big truck").isDisplayed())



    }
}
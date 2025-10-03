package com

import android.net.Uri
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.google.common.truth.Truth.assertThat
import com.mrm.tirewise.model.Vehicle
import com.mrm.tirewise.view.screens.VehicleForm
import com.mrm.tirewise.view.screens.VehicleFormScreen
import com.mrm.tirewise.view.theme.TireWiseTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class VehicleFormIntegrationTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Mock
    private lateinit var mockOnRemoveVehicleImage: (Uri) -> Unit

    @Mock
    private lateinit var mockOnDeleteVehicle: () -> Unit

    @Mock
    private lateinit var mockOnSubmit: (Vehicle) -> Unit

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun testVehicleForm_BasicDisplay() {
        val mockVehicle = Vehicle(
            plateNo = "ABC123",
            vehicleBrand = "Model XYZ",
            vehicleMake = "Make ABC",
            yearMake = "2022",
            vehicleColor = "Red",
            additionalInfo = "This car is red my favorite color",
            imageUri = "https://example.com/image.jpg"
        )
        val mockUserId = "user123"

        composeTestRule.setContent {
            TireWiseTheme {
                VehicleForm(
                    vehicle = mockVehicle,
                    userId = mockUserId,
                    onRemoveVehicleImage = {},
                    onDeleteVehicle = {},
                    onSubmit = {},
                    networkState = networkState
                )
            }
        }

        // Assert core elements are displayed
        assertThat(composeTestRule.onNodeWithTag("licensePlate").isDisplayed()).isTrue()
        assertThat(composeTestRule.onNodeWithTag("vehicleBrand").isDisplayed())
        assertThat(composeTestRule.onNodeWithTag("vehicleModel").isDisplayed())
//
        // Assert "Add Picture" button is displayed
        assertThat(composeTestRule.onNodeWithTag("AddPicture").isDisplayed()).isTrue()
//
//
//        // Optional fields container shouldnt be visible with a vehicle provided
        val optionalFieldsContainer = composeTestRule.onNodeWithTag("OptionalFields")
        assertThat(optionalFieldsContainer.isDisplayed()).isFalse()
    }

    @Test
    fun testVehicleForm_DeleteConfirmation() {
        val mockVehicle = Vehicle(
            plateNo = "ABC123",
            vehicleBrand = "Model XYZ",
            vehicleMake = "Make ABC",
            yearMake = "2022",
            vehicleColor = "Red",
            additionalInfo = "This car is red my favorite color",
            imageUri = "https://example.com/image.jpg"
        )
        val mockUserId = "user123"

        composeTestRule.setContent {
            TireWiseTheme {
                VehicleForm(
                    vehicle = mockVehicle,
                    userId = mockUserId,
                    onRemoveVehicleImage = mockOnRemoveVehicleImage,
                    onDeleteVehicle = mockOnDeleteVehicle,
                    onSubmit = mockOnSubmit,
                    networkState = networkState
                )
            }
        }

        // Simulate clicking the delete button (implementation might vary)
        val deleteButton = composeTestRule.onNodeWithText("Delete Vehicle")
        deleteButton.performClick()

        // Verify presence of dialog title and text
        assertThat(composeTestRule.onNodeWithTag("deletevechicle1").isDisplayed()).isFalse()
        assertThat(composeTestRule.onNodeWithText("Are you sure you want to delete this vehicle?").isDisplayed()).isTrue()

        // Simulate clicking the positive button (Delete)
        val positiveButton = composeTestRule.onNodeWithText("Delete")
        positiveButton.performClick()

        // Verify onDeleteVehicle is called
        verify(mockOnDeleteVehicle).invoke()
    }


    //----------------------------------------

    @Test
    fun testVehicleFormScreen_DeleteButton() {
        val mockVehicle = Vehicle(
            plateNo = "ABC123",
            vehicleBrand = "Model XYZ",
            vehicleMake = "Make ABC",
            yearMake = "2022",
            vehicleColor = "Red",
            additionalInfo = "This car is red my favorite color",
            imageUri = "https://example.com/image.jpg"
        )
        val mockUserId = "user123"
        val onNavigateUp: () -> Unit = {}
        val onRemoveVehicleImage: (Uri) -> Unit = {}
        val mockOnDeleteVehicle: (Vehicle) -> Unit = {}
        val onSubmit: (Vehicle) -> Unit = {}

        composeTestRule.setContent {
            TireWiseTheme {
                VehicleFormScreen(
                    vehicle = mockVehicle,
                    userId = mockUserId,
                    onNavigateUp = onNavigateUp,
                    onRemoveVehicleImage = onRemoveVehicleImage,
                    onDeleteVehicle = mockOnDeleteVehicle,
                    onSubmit = onSubmit,
                    networkState = networkState
                )
            }
        }

        // Simulate clicking the delete button (implementation might vary)
         composeTestRule.onNodeWithTag("confirmDelete").performClick()

        // Verify onDeleteVehicle is called with the mock vehicle
        verify(mockOnDeleteVehicle).invoke(mockVehicle)

        // Verify onNavigateUp is called (assuming navigation after delete)
        verify(onNavigateUp).invoke()


    }

    @Test
    fun testVehicleForm_Submit() {
        val mockVehicle = Vehicle(
            plateNo = "ABC123",
            vehicleBrand = "Model XYZ",
            vehicleMake = "Make ABC",
            yearMake = "2022",
            vehicleColor = "Red",
            additionalInfo = "This car is red my favorite color",
            imageUri = "https://example.com/image.jpg"
        )
        val mockUserId = "user123"
        val mockOnSubmit: (Vehicle) -> Unit = {}

        composeTestRule.setContent {
            VehicleForm(
                vehicle = mockVehicle,
                userId = mockUserId,
                onRemoveVehicleImage = {},
                onDeleteVehicle = {},
                onSubmit = mockOnSubmit,
                networkState = networkState
            )
        }

        // Verify "Submit" button is displayed before clicking
        assertThat(composeTestRule.onNodeWithText("Submit").isDisplayed()).isTrue()

        composeTestRule.onNodeWithText("Submit").performClick()

        verify(mockOnSubmit).invoke(mockVehicle) // Verify onSubmit


    }

}

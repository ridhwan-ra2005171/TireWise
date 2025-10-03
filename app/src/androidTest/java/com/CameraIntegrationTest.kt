package com

import androidx.camera.view.LifecycleCameraController
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.core.content.PermissionChecker
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.mrm.tirewise.view.screens.camera.CameraPreview
import com.mrm.tirewise.view.screens.camera.CameraScreen
import com.mrm.tirewise.view.screens.camera.CameraScreenContent
import com.mrm.tirewise.viewModel.CameraViewModel
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.matches
import org.mockito.Mockito
import org.mockito.Mockito.mock

class CameraIntegrationTest {

    @get:Rule
    val composeTestRule = createComposeRule()


    @Test
    fun testCameraScreen_Displayed() {
        // Mock ViewModel and other dependencies if needed
        val mockViewModel = mock(CameraViewModel::class.java)
        // Render the CameraScreen composable
        composeTestRule.setContent {
            CameraScreen(
                cameraViewModel = mockViewModel,
                onConfirmPhoto = { /* Mock action */ }
            )
        }

        // Verify that the CameraScreen is displayed
        composeTestRule.onNodeWithTag("cameraScreenContent").assertIsDisplayed()
    }


    @Test
    fun testCameraScreenContent_Displayed() {
        val mockViewModel = mock(CameraViewModel::class.java)

        composeTestRule.setContent {
            CameraScreenContent(
                cameraViewModel = mockViewModel,
                onConfirmPhoto = {  },
                showPermissionDialog = {  },
                allPermissionsGranted = true
            )
        }

        // important UI elements are displayed
        composeTestRule.onNodeWithTag("cameraView").assertIsDisplayed()
        composeTestRule.onNodeWithTag("captureButton").assertIsDisplayed()
    }

    @Test
    fun testCameraPreview_Displayed() {
        // controller for the CameraPreview
        val mockController = mockLifecycleCameraController()

        composeTestRule.setContent {
            CameraPreview(controller = mockController)
        }

        //AndroidView displaying the camera preview is displayed
        composeTestRule.onNodeWithTag("cameraPreview").assertIsDisplayed()
    }

    // Mock function to create a mock LifecycleCameraController
    private fun mockLifecycleCameraController(): LifecycleCameraController {
        return mock(LifecycleCameraController::class.java)
    }



}
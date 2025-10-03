package com

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.test.tirewise.view.screens.signIn.WelcomeScreen
import junit.framework.TestCase.assertTrue

import org.junit.Rule
import org.junit.Test

class welcomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()




    @Test
    fun testWelcomeScreenisDisplayed() {

        val clickCallback = {
            composeTestRule.runOnUiThread {
                try {
                    val welcomeScreen = composeTestRule.onNodeWithTag("WelcomeScreen")
                    val helloText = composeTestRule.onNodeWithText("Hello!")

                    welcomeScreen.assertExists()
                    helloText.assertIsDisplayed()
                } catch (e: Exception) {
                   (e.printStackTrace())
                }
            }
        }

        composeTestRule.setContent {
            WelcomeScreen(
                onSignInClick = { clickCallback() },
                onGuestClick = {},
                networkState = networkState
            )
        }

        try {
            composeTestRule.runOnUiThread {
                val googleButton = composeTestRule.onNodeWithText("Continue with Google")
                googleButton.performClick()
            }
        } catch (e: Exception) {
             (e.printStackTrace())
        }
    }

    @Test
    fun testSignInButtonClick() {
        var signInClicked = false

        composeTestRule.setContent {
            WelcomeScreen(
                onSignInClick = { signInClicked = true },
                onGuestClick = {},
                networkState = networkState
            )
        }

        val googleButton = composeTestRule.onNodeWithText("Continue with Google")
        googleButton.performClick()

        // Assert that the lambda was called
        assertTrue(signInClicked)
    }

    @Test
    fun testSignInGuestButtonClick() {
        var signInClicked = false

        composeTestRule.setContent {
            WelcomeScreen(
                onSignInClick = {  },
                onGuestClick = {signInClicked = true},
                networkState = networkState
            )
        }

        val googleButton = composeTestRule.onNodeWithText("or continue as a guest")
        googleButton.performClick()

        // Assert that the lambda was called
        assertTrue(signInClicked)
    }



}

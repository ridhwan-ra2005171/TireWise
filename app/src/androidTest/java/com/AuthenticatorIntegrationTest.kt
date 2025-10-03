package com

import com.firebase.ui.auth.data.model.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.mrm.tirewise.testing.Authenticator
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations


class AuthenticatorIntegrationTest {
    // Mock FirebaseAuth instance
    @Mock
    private lateinit var firebaseAuth: FirebaseAuth

    // Instance of the class to be tested
    private lateinit var authenticator: Authenticator

    @Before
    fun setup() {
        // Initialize Mockito annotations
        MockitoAnnotations.initMocks(this)

        // Instantiate the class to be tested with the mocked FirebaseAuth
        authenticator = Authenticator(firebaseAuth)
    }

    @Test
    fun testLoginWithValidCredentials() {
        // Mock Firebase authentication response
        `when`(firebaseAuth.signInWithEmailAndPassword("valid_email@example.com", "valid_password"))
            .thenReturn(mockSuccessResult())

        assertTrue(authenticator.login("valid_email@example.com", "valid_password"))
    }

    @Test
    fun testLoginWithInvalidCredentials() {
        // Mock Firebase authentication response
        `when`(firebaseAuth.signInWithEmailAndPassword("invalid_email@example.com", "invalid_password"))
            .thenReturn(mockFailedResult())

        assertFalse(authenticator.login("invalid_email@example.com", "invalid_password"))
    }

    // Mock success authentication result
    private fun mockSuccessResult(): Task<AuthResult> {
        val mockResult = mock(AuthResult::class.java)
        val mockTask = mock(Task::class.java) as Task<AuthResult>
        `when`(mockTask.isSuccessful).thenReturn(true)
        `when`(mockTask.result).thenReturn(mockResult)
        return mockTask
    }

    // Mock failed authentication result
    private fun mockFailedResult(): Task<AuthResult> {
        val mockTask = mock(Task::class.java) as Task<AuthResult>
        `when`(mockTask.isSuccessful).thenReturn(false)
        return mockTask
    }
}
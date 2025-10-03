package com

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.mrm.tirewise.testing.Authenticator
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class logOutIntegrationTest {
    @Mock
    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var authenticator: Authenticator

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        authenticator = Authenticator(firebaseAuth)
    }

    @Test
    fun test_logout_when_user_is_loggedin() {
        `when`(firebaseAuth.currentUser).thenReturn(mockUser())

        authenticator.logout( true)

    }

    @Test
    fun test_logout_when_user_is_not_logged_in() {
        // Mock Firebase authentication response indicating user is not logged in
        `when`(firebaseAuth.currentUser).thenReturn(null)

        // Call the logout method
        authenticator.logout( false)

    }

    private fun mockUser(): FirebaseUser {
        return mock(FirebaseUser::class.java)
    }
}
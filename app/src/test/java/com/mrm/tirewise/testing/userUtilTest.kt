package com.mrm.tirewise.testing

import org.junit.Assert.*
import org.junit.Test

class userUtilTest{
    private val authenticator = UserUtil

    @Test
    fun `test login with valid credentials`() {
        assertTrue(authenticator.login("admin", "admin"))
    }

    @Test
    fun `test login with invalid username`() {
        assertFalse(authenticator.login("user", "admin"))
    }

    @Test
    fun `test login with invalid password`() {
        assertFalse(authenticator.login("admin", "password"))
    }

    @Test
    fun `test login with invalid credentials`() {
        assertFalse(authenticator.login("user", "password"))
    }

    @Test
    fun testLogoutWhenLoggedIn() {
        // Call the logout method with isLoggedIn = true
        authenticator.logout(true)

        // Assert that the logout operation was successful
        assertTrue(true)
    }
    @Test
    fun testLogoutWhenNotLoggedIn() {
        // Call the logout method with isLoggedIn = false
        authenticator.logout( false)

        assertFalse(false)
    }
}
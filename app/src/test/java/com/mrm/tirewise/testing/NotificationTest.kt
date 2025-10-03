package com.mrm.tirewise.testing

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.mrm.tirewise.reminderManager.RunnerNotifier
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class NotificationTest {
    @Mock
    private lateinit var notificationManager: NotificationManager

    @Mock
    private lateinit var context: Context

    private lateinit var runnerNotifier: RunnerNotifier

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        runnerNotifier = RunnerNotifier(notificationManager, context)
    }

    @Test
    fun testBuildNotification() {
        // Mock behavior of NotificationCompat.Builder
        val expectedNotification = NotificationCompat.Builder(context, "reminder_channel_id")
            .setContentTitle("Time to go for a run 🏃‍️")
            .setContentText("You are ready to go for a run?")
            .setSmallIcon(android.R.drawable.btn_star)
            .build()

        // Invoke buildNotification() method
        val notification = runnerNotifier.buildNotification()

        // Verify that the generated notification matches the expected notification
        assertEquals(expectedNotification.toString(), notification.toString())
    }

    @Test
    fun testGetNotificationTitle() {
        val expectedTitle = "Time to go for a run 🏃‍️"
        assertEquals(expectedTitle, runnerNotifier.getNotificationTitle())
    }

    @Test
    fun testGetNotificationMessage() {
        val expectedMessage = "You are ready to go for a run?"
        assertEquals(expectedMessage, runnerNotifier.getNotificationMessage())
    }
}
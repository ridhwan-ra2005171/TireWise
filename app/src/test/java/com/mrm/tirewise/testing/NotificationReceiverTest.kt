package com.mrm.tirewise.testing

import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationManagerCompat
import com.mrm.tirewise.reminderManager.AlarmReceiver
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

class NotificationReceiverTest {
    @Mock
    private lateinit var notificationManager: NotificationManagerCompat

    @Test
    fun testOnReceive() {

        val mockContext = Mockito.mock(Context::class.java)
        val mockIntent = Mockito.mock(Intent::class.java)



        // Mock Intent with extras
        val intent = mock(Intent::class.java)
        `when`(intent.getStringExtra("id")).thenReturn("12345")
        `when`(intent.getStringExtra("vehicleId")).thenReturn("vehicle123")
        `when`(intent.getStringExtra("vehicleTitle")).thenReturn("Test Vehicle")
        `when`(intent.getStringExtra("userId")).thenReturn("user123")
        `when`(intent.getStringExtra("title")).thenReturn("Test Reminder")
        `when`(intent.getStringExtra("message")).thenReturn("Reminder Message")
        `when`(intent.getStringExtra("date")).thenReturn("2022-04-01")
        `when`(intent.getStringExtra("time")).thenReturn("12:00")
        `when`(intent.getLongExtra("dateTime", 0)).thenReturn(1648777200000L) // Mocking time in milliseconds

        val alarmReceiver = AlarmReceiver()
        alarmReceiver.onReceive(mockContext, mockIntent)






    }
}
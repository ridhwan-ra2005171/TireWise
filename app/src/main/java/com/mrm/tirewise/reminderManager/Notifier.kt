package com.mrm.tirewise.reminderManager

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.annotation.RequiresApi

// The Notifier class is an abstract class that provides the necessary
// information to create a notification and includes abstract methods that must
// be implemented by its subclasses.
//
//We need to create Notification Channel at API LEVEL 26 and above.
// If the application supports Android 13 and above, it is necessary
// to add android.permission.POST_NOTIFICATION permission to our
// Manifest file and get permission from the user at runtime.

abstract class Notifier(
    private val notificationManager: NotificationManager
) {
    abstract val notificationChannelId: String
    abstract val notificationChannelName: String
    abstract val notificationId: Int


    fun showNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = createNotificationChannel()
            notificationManager.createNotificationChannel(channel)
        }
        val notification = buildNotification()
        notificationManager.notify(
            notificationId,
            notification
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    open fun createNotificationChannel(
        importance: Int = NotificationManager.IMPORTANCE_DEFAULT
    ): NotificationChannel {
        return NotificationChannel(
            notificationChannelId,
            notificationChannelName,
            importance
        )
    }

    abstract fun buildNotification(): Notification

    protected abstract fun getNotificationTitle(): String

    protected abstract fun getNotificationMessage(): String

    protected abstract fun getNotificationVehicle(): String

}



package com.mrm.tirewise.reminderManager

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.mrm.tirewise.R
import com.mrm.tirewise.model.Reminder

// This class is used to build the notification and how it looks like
class ReminderNotifierClass(
    private val notificationManager: NotificationManager,
    private val context: Context,
    private val reminder: Reminder
) : Notifier(notificationManager) {

    // Preparing the notification channel for reminder notifications
    override val notificationChannelId: String = context.getString(R.string.reminder_channel_id)
    override val notificationChannelName: String = context.getString(R.string.reminder_notification_channel)
    override val notificationId: Int = reminder.hashCode()

    override fun buildNotification(): Notification {

        val intent = Intent(context, AlarmReceiver::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        return NotificationCompat.Builder(context, notificationChannelId)
            .setContentTitle(getNotificationTitle()+ " for "+ getNotificationVehicle()) //will add the car also
            .setContentText(getNotificationMessage())
            .setSmallIcon(R.drawable.logo_42px)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_REMINDER) // Set the notification as a reminder so that it shows when do not disturb is on
            .setAutoCancel(true) // automatically dismisses the notification when the user taps it
            // when tapped, the app will open
            .setContentIntent(pendingIntent)
            .build()
    }

    override fun getNotificationTitle(): String {
        return reminder.reminderTitle
    }

    override fun getNotificationVehicle(): String {
        return reminder.vehicleTitle
    }

    override fun getNotificationMessage(): String {
        return reminder.reminderNotes ?: ""
    }
}
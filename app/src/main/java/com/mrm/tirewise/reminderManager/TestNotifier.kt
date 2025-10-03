package com.mrm.tirewise.reminderManager

import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import java.util.UUID

// The RunnerNotifier class is a helper class derived from the Notifier class.
// It is used to configure the necessary settings for sending a run notification to the user.
class RunnerNotifier(
    private val notificationManager: NotificationManager,
    private val context: Context,
//    private val reminder: Reminder
) : Notifier(notificationManager) {

    override val notificationChannelId: String = "reminder_channel_id"
    override val notificationChannelName: String = "Reminder Notification"
    override val notificationId: Int = UUID.randomUUID().hashCode()

    override fun buildNotification(): Notification {
        return NotificationCompat.Builder(context, notificationChannelId)
            .setContentTitle(getNotificationTitle())
            .setContentText(getNotificationMessage())
            .setSmallIcon(android.R.drawable.btn_star)
            .build()
    }

    public override fun getNotificationTitle(): String {
        return "Time to go for a run 🏃‍️"
    }

    public override fun getNotificationVehicle(): String {
        TODO("Not yet implemented")
    }

    public override fun getNotificationMessage(): String {
        return "You are ready to go for a run?"
    }
}
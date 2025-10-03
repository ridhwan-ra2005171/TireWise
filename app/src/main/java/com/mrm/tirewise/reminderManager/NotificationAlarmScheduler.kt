package com.mrm.tirewise.reminderManager

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.mrm.tirewise.R
import com.mrm.tirewise.model.Reminder
import com.mrm.tirewise.utils.toLocalDateTime
import com.mrm.tirewise.utils.toReadableDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

// Class to help to schedule the alarm with using the AlarmManager.
class NotificationAlarmScheduler(
    private val context: Context
) : AlarmScheduler {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    // This method is used to create a pending intent for the alarm.
    // A pending intent is an object that contains information about the intent that will be triggered when the alarm goes off.
    // It is a wrapper around an intent. We need this wrapper because intents cannot work in the background.
    override fun createPendingIntent(reminderItem: Reminder): PendingIntent {
        // Create the intent
        val intent = Intent(context, AlarmReceiver::class.java)
        // Put the reminder item in the intent by putting its key and value pairs
        intent.putExtra("id", reminderItem.reminderId)
        intent.putExtra("vehicleId", reminderItem.vehicleId)
        intent.putExtra("vehicleTitle", reminderItem.vehicleTitle)
        intent.putExtra("userId", reminderItem.userId)
        intent.putExtra("title", reminderItem.reminderTitle)
        intent.putExtra("message", reminderItem.reminderNotes)
        intent.putExtra("date", reminderItem.reminderDate)
        intent.putExtra("time", reminderItem.reminderTime)
        intent.putExtra("dateTime", reminderItem.reminderDateTime)

        // Create and return the pending intent by passing the intent
        return PendingIntent.getBroadcast(
            context,
            reminderItem.reminderId.hashCode(), // Unique ID for the pending intent
            intent, // The intent
            PendingIntent.FLAG_UPDATE_CURRENT // Update the pending intent if the same id is used
                    or PendingIntent.FLAG_IMMUTABLE // Make the pending intent immutable
        )
    }

    override fun schedule(reminderItem: Reminder) {
        Log.d( "__notification", "schedule - date time: ${reminderItem.reminderDateTime.toReadableDate()}")
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP, // Wakeup the screen when the alarm goes off
            reminderItem.reminderDateTime, // Pass the intended time in milliseconds
            createPendingIntent(reminderItem) // Pass the pending intent
        )
    }
    override fun cancel(reminderItem: Reminder) {
        alarmManager.cancel(
            createPendingIntent(reminderItem)
        )
    }
    override fun scheduleRepeating(currentDateTime: LocalDateTime) {
        TODO("scheduleRepeating: Not yet implemented")
    }

    override fun cancelRepeating() {
        TODO("cancelRepeating: Not yet implemented")
    }





    fun scheduleEvery2DayNotification() {
        // will be reset when user uses the app, (if daily user, wont see notification)
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 21)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        scheduleRepeating(calendar.timeInMillis.toLocalDateTime())
    }

//    override fun createPendingIntent(reminderItem: Reminder): PendingIntent {
//        TODO("Not yet implemented")
//    }
//
//
//    override fun scheduleRepeating(currentDateTime: LocalDateTime) {
//        val alarmManager = context.getSystemService(AlarmManager::class.java)
//        // set recurring every 2 days
//        val notificationIntent = Intent(context, AlarmReceiver::class.java)
//        notificationIntent.putExtra("notificationId", 2)
//        notificationIntent.putExtra("title", context.getString(R.string.app_name))
//        notificationIntent.putExtra("message", context.getString(R.string.app_name))
//        val pendingIntent = PendingIntent.getBroadcast(
//            context,
//            0,
//            notificationIntent,
//            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
//        )
//
////        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
////        val interval = 2 * 24 * 60 * 60 * 1000 // interval in milliseconds
//        val interval = 60000 // for every minute during testing
//
//        alarmManager.
//        setRepeating(
//            AlarmManager.RTC_WAKEUP,
//            Calendar.getInstance().timeInMillis,
//            interval.toLong(),
//            pendingIntent
//        ) // repeating alarm (repeats every 2 days based on interval
//    }
//
//    private fun scheduleRecurringNotification(calendar: Calendar, title: Int, message: Int){
//
//    }
//
//    override fun cancelRepeating() {
//        TODO("Not yet implemented")
//    }
//
//    override fun schedule(reminderItem: Reminder) {
//        // We need an intent to start the Alarm
//        val intent = Intent(context, AlarmReceiver::class.java).apply {  //We created the AlramReceiver class that extends BroadcastReceiver
//            // putExtra is used to pass data
//            putExtra("id", reminderItem.reminderId)
//            putExtra("title", reminderItem.reminderName)
//            putExtra("message", reminderItem.reminderNotes)
//            putExtra("date", reminderItem.reminderDate)
//            putExtra("time", reminderItem.reminderTime)
//            putExtra("dateTime", reminderItem.reminderDateTime)
//
////            putExtra("D", reminderItem.time.toLocalDateTime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())
//        }
//
//        // For alarms that would work when the device is off, we need to create a pending intent from the normal intent
//        val pendingIntent = PendingIntent.getBroadcast(
//            context,
//            reminderItem.reminderId.hashCode(), // unique id as Int. We use hashCode because reminderId is String & we need Int
//            intent, // Pass the normal intent
//            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
//        )
//
//        // Alarms that need to be shown even when idle (like if the device is on low power mode)
//        alarmManager.setExactAndAllowWhileIdle(
//            AlarmManager.RTC_WAKEUP, // Wakeup the device screen when the alarm goes off
//            reminderItem.reminderDateTime, // The time at which the alarm goes off
//            pendingIntent
//
//        )
//    }
//
//    override fun cancel(reminderItem: Reminder) {
//        val pendingIntent = PendingIntent.getBroadcast(
//            context,
//            reminderItem.reminderId.hashCode(), // unique id as Int. We use hashCode because reminderId is String & we need Int
//            Intent(context, AlarmReceiver::class.java), // Pass the normal intent
//            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
//        )
//
//        alarmManager.cancel(
//            pendingIntent
//        )
//    }

//    override fun createPendingIntent(reminderItem: ReminderItem): PendingIntent {
//        val intent = Intent(context, AlarmReceiver::class.java)
//
//        return PendingIntent.getBroadcast(
//            context,
//            reminderItem.id,
//            intent,
//            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
//        )
//    }

//    override fun schedule(reminderItem: ReminderItem) {
//        alarmManager.Repeating(
//            AlarmManager.RTC_WAKEUP,
//            reminderItem.time,
//            AlarmManager.INTERVAL_DAY,
//            createPendingIntent(reminderItem)
//        )
//    }
//
//    override fun cancel(reminderItem: ReminderItem) {
//        alarmManager.cancel(
//            createPendingIntent(reminderItem)
//        )
//    }
}
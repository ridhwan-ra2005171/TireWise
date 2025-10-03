package com.mrm.tirewise.reminderManager

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.mrm.tirewise.model.Reminder
import com.mrm.tirewise.utils.toReadableDate

// This is class is used when a scheduled alarm is triggered it will perform the function of displaying the notification
class AlarmReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        try {
            context?.let {
                val notificationManager = it.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                // Create a reminder object from the intent
                val reminderObject = Reminder(
                    reminderId = intent?.getStringExtra("id")!!,
                    vehicleId = intent.getStringExtra("vehicleId")!!,
                    vehicleTitle = intent.getStringExtra("vehicleTitle")!!,
                    userId =  intent.getStringExtra("userId")!!,
                    reminderTitle = intent.getStringExtra("title")!!,
                    reminderNotes = intent.getStringExtra("message") ?: "",
                    reminderDate = intent.getStringExtra("date")!!,
                    reminderTime = intent.getStringExtra("time")!!,
                    reminderDateTime = intent.getLongExtra("dateTime", 0)
                )
                Log.d( "__notification", "onReceive - reminder object: $reminderObject")
                Log.d( "__notification", "onReceive - date time: ${reminderObject.reminderDateTime.toReadableDate()}")
                val reminderNotifier = ReminderNotifierClass(notificationManager, it, reminderObject)
                reminderNotifier.showNotification()
//            val runnerNotifier = RunnerNotifier(notificationManager, it)
//            runnerNotifier.showNotification()
            }
        } catch (e: Exception) {
//            Log.d("__notification", "onReceive: $e")
        }
    }

//    override fun onReceive(context: Context?, intent: Intent?) {
//        context?.let {
//            val notificationManager = it.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//            val reminderNotifier = ReminderNotifierClass(notificationManager, it, intent?.getSerializableExtra("reminder") as Reminder)
//            reminderNotifier.showNotification()
//        }
//    }
//    override fun onReceive(context: Context?, intent: Intent?) {
//        val message = intent?.getStringExtra("id")?: return
//        val reminderTitle = intent?.getStringExtra("title")
//        val reminderNotes = intent?.getStringExtra("message")
//        val reminderDate = intent?.getLongExtra("date", 0)
//        val reminderTime = intent?.getLongExtra("time", 0)
//        val reminderDateTime = intent?.getLongExtra("dateTime", 0)
//
//        val reminderItem = Reminder(
//            reminderId = message,
//            userId = "",
//            vehicleId = "",
//            reminderName = reminderTitle!!,
//            reminderNotes = reminderNotes!!,
//            reminderTime = reminderTime.toString(),
//            reminderDate = reminderDate.toString(),
//            reminderDateTime = reminderDateTime!!)
//
//        triggerImmediateNotification( context!!, reminderItem )
//    }
}
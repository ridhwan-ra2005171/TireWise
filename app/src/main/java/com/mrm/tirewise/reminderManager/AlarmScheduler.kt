package com.mrm.tirewise.reminderManager

import android.app.PendingIntent
import com.mrm.tirewise.model.Reminder
import java.time.LocalDateTime

interface AlarmScheduler {

    fun createPendingIntent(reminderItem: Reminder): PendingIntent

    fun scheduleRepeating(currentDateTime: LocalDateTime)
    fun cancelRepeating()

    fun schedule(reminderItem: Reminder)
    fun cancel(reminderItem: Reminder)
}
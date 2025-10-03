package com.mrm.tirewise.viewModel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mrm.tirewise.model.Reminder
import com.mrm.tirewise.reminderManager.NotificationAlarmScheduler
import com.mrm.tirewise.repository.ReminderRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ReminderViewModel(application: Application): AndroidViewModel(application) {

    private val notificationAlarmScheduler by lazy { NotificationAlarmScheduler(application as Context) }

    private val reminderRepository = ReminderRepository(application)

    private var _waitingForResult = MutableStateFlow(false)
    var waitingForResult = _waitingForResult.asStateFlow()

    // Check if reminder is overdue
    fun isReminderOverdue(reminder: Reminder): Boolean {
        return reminder.reminderDateTime < System.currentTimeMillis()
    }

    //get all of the user's reminders
    fun getAllReminders(userId: String): Flow<List<Reminder>> {
        setWaitingForResult(true)
        deleteOverdueReminders(userId)
        return reminderRepository.getReminders(userId)
                .also { setWaitingForResult(false) }
    }

    // get reminders for a specific vehicle
    fun getRemindersPerVehicle(userId: String, vehicleId: String): Flow<List<Reminder>> {
        setWaitingForResult(true)
        deleteOverdueReminders(userId)
        return reminderRepository.getReminders(userId, vehicleId)
            .also { setWaitingForResult(false) }
    }

    fun addReminder(reminder: Reminder) {
        // Set the reminder notification
        notificationAlarmScheduler.schedule(reminder)
        // Add the reminder to the list
        reminderRepository.addReminder(reminder)
    }

    //delete reminders
    fun deleteReminder(reminder: Reminder) {
        // Cancel the reminder notification
        notificationAlarmScheduler.cancel(reminder)
        // Delete the reminder
        reminderRepository.deleteReminder(reminder.reminderId)
    }

    private fun deleteOverdueReminders(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            // Delete overdue reminders
            reminderRepository.getReminders(userId).collect { reminders ->
                reminders.filter { isReminderOverdue(it) }
                    .forEach {
                        reminderRepository.deleteReminder(it.reminderId)
                        Log.d( "__REMINDERVIEWMODEL", "Delete overdue reminder: ${it.reminderTitle} ${it.reminderId}")
                    }
            }
        }
    }
    private fun setWaitingForResult(value: Boolean) {
        _waitingForResult.value = value
    }

}
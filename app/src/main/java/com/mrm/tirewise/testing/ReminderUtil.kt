package com.mrm.tirewise.testing

/**
 * input is not valid if
 * reminder already exist
 * userId does not exist from existing user
 * vehicleId does not exist
 * reminderName is empty
 * reminder Date is empty
 * reminder Time is empty
 *
 */

private val existingReminders =listOf(
    "Check Tires",
    "Oil Change",
)

private val existingUserId = listOf(
    "htrs-fdgh-8793",
    "tjsd-hdff-3454",
)

private val existingRemindersId = listOf(
    "asdf-sadf-1321",
    "ashe-hdff-7562",
)
private val existingVehicleId = listOf(
    "sdfasdf1321",
    "gasdfas2645",
)

object ReminderUtil {

    fun validateReminderInput(
         reminderId: String,
         userId: String,
         vehicleId: String,
         reminderName: String,
         reminderDate: String,
         reminderTime: String,
         reminderNotes: String?
    ): Boolean {
//        return true
        if (reminderId in existingRemindersId){
            return false
        }
        if (userId !in existingUserId){
            return false

        }

        if (vehicleId !in existingVehicleId){
            return false}
        if (reminderName.isEmpty()){
            return false
        }
        if (reminderDate.isEmpty()){
            return false
        }
        if (reminderTime.isEmpty()){
            return false
        }

        return true
    }

    fun validategetReminders(userId: String): Boolean{
        if (userId !in existingUserId){
            return false
        }
        return true
    }

    fun validategetReminder(userId: String): Boolean{
        if (userId !in existingUserId){
            return false
        }
        return true
    }

    fun validateDeleteReminder(reminderId: String): Boolean{
        if (reminderId !in existingRemindersId){
            return false
        }
        return true
    }

}
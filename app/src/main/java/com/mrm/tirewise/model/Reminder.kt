package com.mrm.tirewise.model

import com.google.firebase.firestore.DocumentId
import java.util.UUID

data class Reminder(
    @DocumentId
    val reminderId: String = UUID.randomUUID().toString(),
    var vehicleId: String,
    var vehicleTitle: String,
    var userId: String,
    var reminderTitle: String,
    var reminderDate: String,
    var reminderTime: String,
    var reminderNotes: String?,
    var reminderDateTime: Long = 0
) {
    constructor() : this(
        vehicleId = "",
        vehicleTitle = "",
        userId = "",
        reminderTitle = "",
        reminderDate = "",
        reminderTime = "",
        reminderNotes = "",
    )
}


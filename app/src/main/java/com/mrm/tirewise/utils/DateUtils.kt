package com.mrm.tirewise.utils

import android.icu.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.TimeZone

fun Long.toReadableDate() : String {
    val date = Date( this )
    val format = SimpleDateFormat("dd-MMM-yyyy  •  HH:mm aa")
    return format.format(date)
}

fun Long.toLocalDateTime() : LocalDateTime {
    return LocalDateTime.ofInstant(
        Instant.ofEpochMilli(this),
        TimeZone.getDefault().toZoneId());
}

fun LocalDateTime.toMilliSeconds() : Long {

    // Get the system's default time zone
    val zoneId = ZoneId.systemDefault()

    // Convert LocalDateTime to milliseconds since Unix epoch
    val milliseconds = this.atZone(zoneId).toInstant().toEpochMilli()

    println("Milliseconds since Unix epoch: $milliseconds")
    return milliseconds
}

fun formatLocalDateToString(date: LocalDate): String {
    return date.format(DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm"))
}

fun convert24toAMPM(time: String): String {
    val timeList: List<Int> = time.split(":").map { it.toInt() }
    val hours = timeList[0]; val minutes = timeList[1]
    val hoursAM_PM_Format = if( hours == 0) 12 else if(hours <= 12) hours else hours%12
    val  hoursAM_PM = if(  hours in 1..12 || hours == 24 ) "AM" else "PM"
    val timeInAMandPM = String.format("%02d:%02d %s", hoursAM_PM_Format, minutes, hoursAM_PM)

    return timeInAMandPM
}
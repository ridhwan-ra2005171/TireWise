package com.mrm.tirewise.model

import com.google.firebase.firestore.DocumentId
import java.text.SimpleDateFormat
import java.util.UUID

//data class Tire(
//    val tireId : String, // unique id for the tire
//    val imageUri: Int,  // Tire Image Uri
//    val tireCondition: String,  // Tire condition text
//    val tireConditionDesc: String,  // Tire condition description text
//    val tirePosition: String,  // Tire position text
//    val additionalNotes: String?,  // additional comments
//    val date: String // Placeholder for date text
//)

data class TireScan(
    @DocumentId
    val tireId : String = UUID.randomUUID().toString(), // unique id for the tire
    var vehicleId: String, // The associated vehicle
    var tireImageUri: String,  // Tire Image Uri
    var tireCondition: String,  // Tire condition text
    var tireConditionDesc: String,  // Tire condition description text
    var tirePosition: String,  // Tire position text
    var additionalNotes: String? = null,  // additional comments
    val date: Long  = System.currentTimeMillis()// Placeholder for date
) {
    constructor() : this(
        vehicleId = "",
        tireImageUri = "",
        tireCondition = "",
        tireConditionDesc = "",
        tirePosition = "",
    )
}

val simpleDateFormat = SimpleDateFormat("d-MMM-yyyy • hh:mm aa")
fun main() {
    val date = simpleDateFormat.format(System.currentTimeMillis())
    println(date)
}
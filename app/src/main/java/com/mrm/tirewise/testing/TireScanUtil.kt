package com.mrm.tirewise.testing

import java.util.Date
import java.util.UUID

object TireScanUtil {
    private val existingUserId = listOf(
        "htrs-fdgh-8793",
        "tjsd-hdff-3454",
    )
    private val existingVehicleId = listOf(
        "sdfasdf1321",
        "gasdfas2645",
        "1234567890"
    )

    private val existingTireScanId = listOf(
        "tirescan213",
        "tirescan234",
    )
    /*
    getTirescans
    sortTirescansByPosition
    sortTireScansByDate
    uploadTireScans
    deleteTirescan
     */

    fun validateTireScanInput(
         tireId : String = UUID.randomUUID().toString(), // unique id for the tire
         vehicleId: String, // The associated vehicle
         tireImageUri: String,  // Tire Image Uri
         tireCondition: String,  // Tire condition text
         tireConditionDesc: String,  // Tire condition description text
         tirePosition: String,  // Tire position text
         additionalNotes: String? = null,  // additional comments
         date: Long  = System.currentTimeMillis()// Placeholder for date
    ) :Boolean{
        if(vehicleId !in existingVehicleId){
            return false
        }
        if (tireImageUri.isEmpty()){
            return false
        }
        if (tireCondition.isEmpty()){
            return false
        }
        if (tireCondition!= "Good" && tireCondition!= "Moderate" && tireCondition!= "Poor"){
            return false
        }
        if(tirePosition.isEmpty()){
            return false
        }
        if(tirePosition != "Front Left" && tirePosition != "Front Right" && tirePosition != "Back Left" && tirePosition != "Back Right"){
            return false
        }
        if(date.equals(null)){
            return false
        }
        return true
    }

    fun getTires(userId: String) :Boolean{
        if(userId !in existingUserId){
            return false
        }
//        if(tirePosition != "Front Left" && tirePosition != "Front Right" && tirePosition != "Back Left" && tirePosition != "Back Right"){
//            return false
//        }
        return true
    }


    fun deleteTireScan(tireId: String): Boolean {
        if (tireId !in existingTireScanId){
            return false
        }

        return true

    }
    fun filterTiresByPosition(tirePosition: String) : Boolean{
        if(tirePosition != "Front Left" && tirePosition != "Front Right" && tirePosition != "Back Left" && tirePosition != "Back Right"){
            return false
        }
        return true

    }

//Dummy data
    data class MyTireScan(
    val tireId : String, // unique id for the tire
    var vehicleId: String, // The associated vehicle
    var tireImageUri: String,  // Tire Image Uri
    var tireCondition: String,  // Tire condition text
    var tireConditionDesc: String,  // Tire condition description text
    var tirePosition: String,
    val date: Long, //in firebase its stored as long or string
    )

    fun sortTiresByDate(list: List<MyTireScan>): List<MyTireScan> {
        return list.sortedBy { it.date }
    }




}
package com.mrm.tirewise.testing

object vehicleUtil {

    private val existingUserId = listOf(
        "htrs-fdgh-8793",
        "tjsd-hdff-3454",
    )
    private val existingVehicleId = listOf(
        "sdfasdf1321",
        "gasdfas2645",
    )
    fun validateVehicleInput(
         vehicleId: String,
         userId: String,
         plateNo: String,
         vehicleModel: String,
         vehicleMake: String,
         vehicleName: String? = null?: "$vehicleMake • $vehicleModel",
         yearMake : String? = null,
         vehicleColor: String? = null,
         additionalInfo: String? = null,
         imageUri: String? = null
    ): Boolean {

        if (userId !in existingUserId ){
            return false
        }

        if (plateNo.isEmpty()){
            return false
        }
        if(vehicleMake.isEmpty()){
            return false
        }
        if(vehicleModel.isEmpty()){
            return false
        }

        return true
    }

    fun getVehicles(userId: String): Boolean{
        if ( userId !in existingUserId ){
            return false
        }

        return true
    }

    fun deleteVehicle(vehicleId: String): Boolean {
        if (vehicleId !in existingVehicleId){
            return false
        }
        return true
    }

}


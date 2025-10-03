package com.mrm.tirewise.model

import com.google.firebase.firestore.DocumentId
import java.util.UUID

data class Vehicle(
    @DocumentId
    var vehicleId: String = UUID.randomUUID().toString(),
    var userId: String = UUID.randomUUID().toString(),
    var plateNo: String,
    var vehicleBrand: String,
    var vehicleMake: String,
    var vehicleName: String? = null?: "$vehicleMake • $vehicleBrand",
    var yearMake : String? = null,
    var vehicleColor: String? = null,
    var additionalInfo: String? = null,
//    val vehicleImage: Bitmap? = null,
    var imageUri: String? = null // URI from firebase storage
){
    constructor() : this(
        plateNo = "",
        vehicleBrand = "",
        vehicleMake = "")
}

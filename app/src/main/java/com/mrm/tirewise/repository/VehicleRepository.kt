package com.mrm.tirewise.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.mrm.tirewise.model.Vehicle
import com.mrm.tirewise.utils.uploadImageToFirebaseStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class VehicleRepository(val context: Context) {
       // vehicle collection from firebase
    private val vehicleCollectionRef by lazy {
        Firebase.firestore.collection("vehicles")
    }

    // Get vehicle for a user
    fun getVehicles(userId : String) : Flow<List<Vehicle>> = flow {
        emit( vehicleCollectionRef
            .whereEqualTo("userId", userId)
//            .orderBy("vehicleId", Query.Direction.DESCENDING)
            .get()
            .await()
            .toObjects( Vehicle::class.java))
    }

//    fun findVehicleId()
    // Add a new vehicle
    suspend fun upsertVehicle(vehicle: Vehicle) {
        try {
            Log.d( "__VEHICLEVM", " -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-")
            Log.d("__VEHICLEVM", "upsertVehicle: before ${vehicle.imageUri}")
            // Upload vehicle image to firebase storage
            var vehicleImageUri : String? = vehicle.imageUri

            try {
                if (!vehicle.imageUri.isNullOrBlank() && vehicle.imageUri?.contains("firebasestorage",true) == false) {
                    vehicleImageUri = uploadImageToFirebaseStorage(
                        imageUri = vehicle.imageUri?.toUri()!!,
                        parentFolder = "vehicleImages",
                        documentId = vehicleCollectionRef.id,
                        imagePrefix = "${vehicle.plateNo}"
                    )
                }
            } catch (e: Exception) {
                Log.d("__VEHICLEVM", "URI Error: ${e.message}")
            }


            // Assign the firebase storage image uri to vehicle image uri
            vehicle.imageUri = vehicleImageUri
            Log.d("__VEHICLEVM", "upsertVehicle: after ${vehicle.imageUri}")

            // Add vehicle to firestore
            vehicleCollectionRef.document(vehicle.vehicleId).set(vehicle)
                .addOnSuccessListener { documentReference ->
                    Log.d("__VEHICLE_VM", "DocumentSnapshot added with ID: ${vehicle.vehicleId}")
                }.addOnFailureListener { e ->
                    Log.w("__VEHICLE_VM", "Error adding document", e)
                }
        } catch (e: Exception) {
            Log.d("__VEHICLEVM", "Error: ${e.message}")

        }
    }

//    fun addVehicle(vehicle: Vehicle) {
//        vehicleCollectionRef.document(vehicle.vehicleId).set(vehicle)
//            .addOnSuccessListener { documentReference ->
//                Log.d("__VEHICLE_VM", "DocumentSnapshot added with ID: ${vehicle.vehicleId}")
//            }
//            .addOnFailureListener { e ->
//                Log.w("__VEHICLE_VM", "Error adding document", e)
//            }
//    }

    // Update a vehicle
//    fun updateVehicle(vehicle: Vehicle) {
//        vehicleCollectionRef.document(vehicle.vehicleId).set(vehicle)
//            .addOnSuccessListener { documentReference ->
//                Log.d("__VEHICLE_VM", "DocumentSnapshot added with ID: ${vehicle.vehicleId}")
//            }
//            .addOnFailureListener { e ->
//                Log.w("__VEHICLE_VM", "Error adding document", e)
//            }
//    }

    // Delete a vehicle
    fun deleteVehicle(vehicle: Vehicle) {
//        if (vehicle.vehicleImageUri != null) {
//            deleteImageFromStorage(imageUrl = vehicle.vehicleImageUri!!)
//        }
        Log.d("__DELETE", "deleteVehicle: ${""}")
        vehicleCollectionRef.document(vehicle.vehicleId).delete()
//        refreshVehicleList(userId = vehicle.userId)
    }

    suspend fun uploadImage(imageUri: Uri, parentFolder: String, documentId: String, imagePrefix: String): String {
        return uploadImageToFirebaseStorage(imageUri, parentFolder, documentId, imagePrefix)
    }

//    fun uploadImageToFirebaseStorage(imageUri: Uri, parentFolder: String, documentId: String, imagePrefix : String): String {
        // Get the current date to be used as an ID for the image
//        uploadImageToFirebaseStorage()
//    }



}
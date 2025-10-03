package com.mrm.tirewise.repository

import android.content.Context
import android.util.Log
import androidx.core.net.toUri
import com.google.firebase.Firebase
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import com.mrm.tirewise.model.TirePosition
import com.mrm.tirewise.model.TireScan
import com.mrm.tirewise.utils.deleteImageFromStorage
import com.mrm.tirewise.utils.sendImage
import com.mrm.tirewise.view.screens.tireList.SortBy
import kotlinx.coroutines.tasks.await

class TireScanRepository(val context: Context) {
    // vehicle collection from firebase
    private val tireScanCollectionRef by lazy {
        Firebase.firestore.collection("tireScans")
    }


    // Retrieve tire scans with vehicle id
     suspend fun getTireScans(vehicleId : String, tirePositionAsString: String) : List<TireScan> {
        return try {
            if (tirePositionAsString == TirePosition.ALL.getPositionAsString())
                tireScanCollectionRef
                    // Sort by date in ascending order
//                    .orderBy( "date")
                    .whereEqualTo("vehicleId", vehicleId)
                    .orderBy( "date", Query.Direction.DESCENDING)
                    .get()
                    .await()
                    .toObjects(TireScan::class.java)
            else
                tireScanCollectionRef
                    .whereEqualTo("vehicleId", vehicleId)
                    .whereEqualTo("position", tirePositionAsString)
                    .get()
                    .await()
                    .toObjects(TireScan::class.java)
        } catch (e: Exception) {
            Log.d("__TIRE_SCAN", e.message.toString())
            emptyList()
        }.also {
            Log.d("__GET_TIRES", "tireRepo: $it")
        }
    }

//            Flow<List<TireScan>> = flow {
//        emit( tireScanCollectionRef
//            .whereEqualTo("vehicleId", userId)
//            .get()
//            .await()
//            .toObjects( TireScan::class.java))
//    }
    suspend fun sortTireScans(vehicleId: String, tirePositionAsString: String, sortBy: SortBy ) : List<TireScan> {
        // Retrieve tire scans with vehicle id
        return try {
            if (tirePositionAsString == TirePosition.ALL.getPositionAsString())
                when (sortBy) {
                    // Date from latest to oldest
                    SortBy.DateDescending -> {
                        tireScanCollectionRef
                            .whereEqualTo("vehicleId", vehicleId)
                            .orderBy("date", Query.Direction.DESCENDING)
                            .get()
                            .await()
                            .toObjects(TireScan::class.java)
                    }
                    // Date from oldest to latest
                    SortBy.DateAscending -> {
                        tireScanCollectionRef
                            .whereEqualTo("vehicleId", vehicleId)
                            .orderBy("date", Query.Direction.ASCENDING)
                            .get()
                            .await()
                            .toObjects(TireScan::class.java)
                    }
                    // Condition from good to bad
                    SortBy.ConditionBadFirst -> {
                        tireScanCollectionRef
                            .whereEqualTo("vehicleId", vehicleId)
                            .orderBy("tireCondition", Query.Direction.ASCENDING)
                            .get()
                            .await()
                            .toObjects(TireScan::class.java)
                    }
                    // Condition from bad to good
                    SortBy.ConditionGoodFirst -> {
                        tireScanCollectionRef
                            .whereEqualTo("vehicleId", vehicleId)
                            .orderBy("tireCondition", Query.Direction.DESCENDING)
                            .get()
                            .await()
                            .toObjects(TireScan::class.java)
                    }
                } else {
                    Log.d("__SORT", tirePositionAsString)

                    when (sortBy) {
                        // Date from latest to oldest
                        SortBy.DateDescending -> {
                            Log.d("__SORT KOKO", tirePositionAsString)
                            tireScanCollectionRef
                                .whereEqualTo("vehicleId", vehicleId)
                                .whereEqualTo( "tirePosition", tirePositionAsString)
                                .orderBy("date", Query.Direction.DESCENDING)
                                .get()
                                .await()
                                .toObjects(TireScan::class.java)
                        }
                        // Date from oldest to latest
                        SortBy.DateAscending -> {
                            tireScanCollectionRef
                                .whereEqualTo("vehicleId", vehicleId)
                                .whereEqualTo( "tirePosition", tirePositionAsString)
                                .orderBy("date", Query.Direction.ASCENDING)
                                .get()
                                .await()
                                .toObjects(TireScan::class.java)
                        }
                        // Condition from good to bad
                        SortBy.ConditionBadFirst -> {
                            tireScanCollectionRef
                                .whereEqualTo("vehicleId", vehicleId)
                                .whereEqualTo( "tirePosition", tirePositionAsString)
                                .orderBy("tireCondition", Query.Direction.ASCENDING)
                                .get()
                                .await()
                                .toObjects(TireScan::class.java)
                        }
                        // Condition from bad to good
                        SortBy.ConditionGoodFirst -> {
                            tireScanCollectionRef
                                .whereEqualTo("vehicleId", vehicleId)
                                .whereEqualTo( "tirePosition", tirePositionAsString)
                                .orderBy("tireCondition", Query.Direction.DESCENDING)
                                .get()
                                .await()
                                .toObjects(TireScan::class.java)
                        }

                    }
            }
        } catch (e: Exception) {
                Log.d("__TIRE_SCAN", e.message.toString())
                emptyList()
            }.also {
                Log.d("__GET_TIRES", "tireRepo: $it")
            }
    }

    suspend fun uploadTireScan(tireScan: TireScan) {
        try {
            // Upload the image to Firebase Storage
//            val tireImageUri = uploadImageToFirebaseStorage(
//                imageUri = tireScan.tireImageUri?.toUri()!!,
//                parentFolder = "tireImages",
//                documentId = tireScanCollectionRef.id,
//                imagePrefix = tireScan.tireId
//            )
            val tireImageUri = sendImage(
                imageUri = tireScan.tireImageUri?.toUri()!!,
                parentFolder = "tireImages",
                documentId = tireScanCollectionRef.id,
                imagePrefix = tireScan.tireId,
                uploadTire = {
//
//            // Assign the firebase storage image uri to tire scan image uri
                tireScan.tireImageUri = it

                // Add vehicle to firestore
                tireScanCollectionRef.document(tireScan.tireId).set(tireScan)
                    .addOnSuccessListener { documentReference ->
                        Log.d("__TIRE_REPO", "DocumentSnapshot added with ID: ${it}")
                    }.addOnFailureListener { e ->
                        Log.w("__TIRE_REPO", "Error adding document", e)
                    }
                }
            )

        } catch (e: Exception) {
            Log.d("__TIRE_REPO", "Error: ${e.message}")
        }
    }

    // Delete Tire Scan
    suspend fun deleteTireScan(tireScan: TireScan) {
        deleteImageFromStorage(tireScan.tireImageUri)
        tireScanCollectionRef.document(tireScan.tireId).delete().await()
//        tireScanCollectionRef.orderBy( "tireId").get().await()
    }

}
package com.mrm.tirewise.utils

import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import kotlinx.coroutines.tasks.await
import java.util.Date
import java.util.Locale


const val FIREBASE_STORAGE = "firebasestorage"


suspend fun uploadImageToFirebaseStorage(imageUri: Uri, parentFolder: String, documentId: String, imagePrefix : String): String {
    var mediaUrl = ""
    try {
        Log.d( "__STORAGE", "Firebase Storage.uploadMedia: $imageUri")
        // Get the current date to be used as an ID for the image
        val sdf = android.icu.text.SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
        val currentDateAndTime: String = sdf.format(Date())

        // Get the storage reference for the image
        var storageReference = FirebaseStorage.getInstance().reference.child("$parentFolder/${documentId}_${imagePrefix}_${currentDateAndTime}.jpg")

        // Check if the image URI is local or from firebase storage
        if (imageUri.toString().contains(FIREBASE_STORAGE)) {
            storageReference = FirebaseStorage.getInstance().reference.child(imageUri.toString())
        }

        // Upload the image to Firebase Storage
        storageReference.putFile(imageUri).await()

        // Get the URL of the uploaded image
        mediaUrl = storageReference.downloadUrl.await().toString()
        Log.d("__STORAGE", "Firebase Storage.uploadMedia: $mediaUrl")
    } catch (e : Exception) {
        Log.d("__STORAGE", "Firebase Storage.uploadMedia: $e")

    }
    return mediaUrl
}

suspend fun sendImageToFirebaseStorage(imageUrl: String) : UploadTask {
    // Get the storage reference for the image
    val storageReference = FirebaseStorage.getInstance().reference.child(imageUrl)

    // Upload the image to Firebase Storage
    return storageReference.putFile(imageUrl.toUri())

}

suspend fun sendImage(imageUri: Uri, parentFolder: String, documentId: String, imagePrefix : String, uploadTire : (String) -> Unit)  {
    Log.d( "__STORAGE", "Firebase Storage.uploadMedia: $imageUri")
    // Get the current date to be used as an ID for the image
    val sdf = android.icu.text.SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
    val currentDateAndTime: String = sdf.format(Date())

    // Get the storage reference for the image
    var storageReference = FirebaseStorage.getInstance().reference.child("$parentFolder/${documentId}_${imagePrefix}_${currentDateAndTime}.jpg")

    // Check if the image URI is local or from firebase storage
    if (imageUri.toString().contains(FIREBASE_STORAGE)) {
        storageReference = FirebaseStorage.getInstance().reference.child(imageUri.toString())
    }

    // Upload the image to Firebase Storage
    val uploadTask = storageReference.putFile(imageUri)

    uploadTask.continueWithTask { task ->
        if (!task.isSuccessful) {
            task.exception?.let {
                throw it
            }
        }
        // Continue with getting the download URL
        storageReference.downloadUrl
    }.addOnCompleteListener { task ->
        if (task.isSuccessful) {
            val downloadUri = task.result
            // Handle the download URL here
            Log.d("__STORAGE", "Download URL: $downloadUri")
            uploadTire(downloadUri.toString())
        } else {
            // Handle failures
            Log.d("__STORAGE", "Failed to get download URL: ${task.exception}")
        }
    }
//        addOnSuccessListener {
//            val mediaUri = storageReference.downloadUrl.toString()
//            Log.d( "__STORAGE", "Firebase Storage.uploadMedia: $mediaUri")
//            // gs://tirewise-42388.appspot.com/tireImages/tireScans_34344ae5-7a17-44d3-8664-7ce647165421_20240330_003920.jpg
//            if(mediaUri.contains("gs://"))
//                uploadTire(mediaUri)
//        }.await()

    // Get the URL of the uploaded image
//    mediaUrl = storageReference.downloadUrl.await().toString()
//    Log.d("__STORAGE", "Firebase Storage.uploadMedia: $mediaUrl")

}


suspend fun deleteImageFromStorage(imageUrl: String) {
    try {
        if (imageUrl.contains(FIREBASE_STORAGE, true)) {
            FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl).delete().await()
        }
    } catch (e: Exception) {
        Log.d("__STORAGE", "deleteImageFromStorage: $e")
    }
}



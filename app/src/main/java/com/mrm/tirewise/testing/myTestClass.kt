package com.mrm.tirewise.testing

import android.content.Context
import com.google.firebase.Firebase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.mrm.tirewise.model.TireScan
import kotlinx.coroutines.flow.Flow

class MyTestClass(val context: Context, firestore: FirebaseFirestore) {

    val tireScanCollectionRef by lazy { firestore.collection("tireScans") }
    // ... other methods

    fun fetchTireScans(): CollectionReference {
        return tireScanCollectionRef
    }
}
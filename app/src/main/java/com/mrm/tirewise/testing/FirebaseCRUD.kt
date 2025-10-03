package com.mrm.tirewise.testing

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirebaseCRUD {
    class FirebaseCRUD(
        private val db: FirebaseFirestore
    ) {
        val collectionId = "vehicles"

        fun createDocument(document: MutableMap<String, Any>) {
            val path = "$collectionId"

            db.collection(path)
                .document(document.get("Id").toString())
                .set(document)
        }

        suspend fun readDocument(id: String, property: String = "Id"): String {
            val path = "$collectionId/$id"

            val propertyValue =
                db.document(path)
                    .get()
                    .await()
                    .data?.get(property)

            return propertyValue.toString()
        }

        fun updateDocument(id: String, property: String = "Id", propertyValue: Any) {
            val path = "$collectionId/$id"

            db.document(path)
                .update(property, propertyValue)
        }

        fun deleteDocument(id: String) {
            val path = "$collectionId/$id"

            db.document(path)
                .delete()
        }
    }
}
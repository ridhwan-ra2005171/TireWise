package com.mrm.tirewise.repository

import android.content.Context
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.mrm.tirewise.model.Reminder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class ReminderRepository(val context: Context) {

    // reminder collection from firebase
    private val reminderCollectionRef by lazy {
        Firebase.firestore.collection("reminders")
    }

    //get reminders for a user
    fun getReminders(userId:String, vehicleId: String = ""): Flow<List<Reminder>> = try {
        flow {

            // Get all reminders if no vehicle id was passed
            if (vehicleId.isNullOrEmpty()) {
                emit( reminderCollectionRef
                    .whereEqualTo("userId", userId)
                    .get()
                    .await()
                    .toObjects( Reminder::class.java))
            } else {
                // Get reminders for a specific vehicle
                emit( reminderCollectionRef
                    .whereEqualTo("userId", userId)
                    .whereEqualTo("vehicleId", vehicleId)
                    .get()
                    .await()
                    .toObjects( Reminder::class.java))
            }
        }
    } catch ( e: Exception) {
        Log.d("__reminder_repo", e.toString())
        flow {
            emit(emptyList<Reminder>())
        }
    }

    //add reminder to user
    fun addReminder(reminder: Reminder) {
        reminderCollectionRef.add(reminder)
            .addOnSuccessListener { documentReference ->
//                Log.d("__REMINDER_VM", "DocumentSnapshot added with ID: ${reminder.reminderId}")
            }
            .addOnFailureListener { e ->
                Log.w("__REMINDER_VM", "Error adding document", e)
            }
    }

    //delete reminder
    fun deleteReminder(reminderId: String) {
        reminderCollectionRef.document(reminderId).delete()
    }

}
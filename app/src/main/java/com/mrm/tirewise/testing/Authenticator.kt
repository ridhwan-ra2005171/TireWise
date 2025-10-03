package com.mrm.tirewise.testing

import com.google.firebase.auth.FirebaseAuth

class Authenticator(private val firebaseAuth: FirebaseAuth) {

    fun login(email: String, password: String): Boolean {
        // Perform authentication using Firebase Auth
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Authentication successful
                    // You may perform additional actions here if needed
                } else {
                    // Authentication failed
                    // You may handle the failure case here
                }
            }
        // Return true for demonstration purpose only
        // In a real application, you would handle the result of the authentication task properly
        return true
    }

    fun logout( isLoggedin: Boolean) : Boolean {
        if(isLoggedin!=true){
            return false
        }
        return true

    }
}

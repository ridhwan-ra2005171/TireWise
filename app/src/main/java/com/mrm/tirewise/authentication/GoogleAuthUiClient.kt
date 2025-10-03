package com.mrm.tirewise.authentication

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import androidx.core.content.ContextCompat.getString
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInRequest.GoogleIdTokenRequestOptions
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.mrm.tirewise.R
import com.mrm.tirewise.model.SignInResult
import com.mrm.tirewise.model.UserData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.tasks.await
import java.util.concurrent.CancellationException

class GoogleAuthUiClient(
    private val context : Context,
    private val oneTapClient: SignInClient // used to show the sign in dialog
) {
    private val auth = Firebase.auth

    // User Data
    private val _userData : MutableStateFlow<UserData?> = MutableStateFlow(null)
    val userData : StateFlow<UserData?> = _userData.asStateFlow()

    suspend fun signIn() : IntentSender? { // Intent Sender sends out an intent to the sign in API to tell it what our intention is.
        val result = try {
            oneTapClient.beginSignIn( // Begin a sign in request
                buildSignInRequest() // Implemented down below
            ).await()
        } catch (e :Exception) {
            e.printStackTrace()
            if (e  is CancellationException) throw e
            null // return null as the result if there is an issue
        }
        // Assign the user
        _userData.update {
            auth.currentUser?.run {
                UserData(
                    userId = uid,
                    userName = displayName,
                    profilePictureUrl = photoUrl?.toString(),
                    userEmail = email,
                    isGuestUser = false
                )
            }
        }
        return result?.pendingIntent?.intentSender
    }

    suspend fun signOut() {
        try {
            oneTapClient.signOut().await() // Logout from Google
            auth.signOut()
            FirebaseAuth.getInstance().signOut()// Logout from Firebase
            _userData.update {
                null
            }
        } catch (e:Exception) {
            e.printStackTrace()
            if ( e  is CancellationException) throw e
        }
    }

    // Used to decode the intent from the intentSender from the signIn function
    // and to sign in with the intent received.
    suspend fun getSignInResultFromIntent(intent: Intent) : SignInResult {
        // Google sign in specific variables
        val credential = oneTapClient.getSignInCredentialFromIntent(intent) // credential is returned from the intent
        val googleIdToken = credential.googleIdToken
        val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken, null) // used to get the user credentials
        return try {
            // Firebase specific code inside the try block
            // Log in using firebase
            val user = auth.signInWithCredential(googleCredentials).await().user
            SignInResult(
                data = user?.run {
                    UserData(
                        userId = uid,
                        userName = displayName,
                        profilePictureUrl = photoUrl?.toString(),
                        userEmail = email,
                        isGuestUser = false
                    )
                },
                errorMessage = null
            )
        } catch (e:Exception) {
            e.printStackTrace()
            if ( e  is CancellationException) throw e
            SignInResult(
                data = null, // make data null if an exception is thrown
                errorMessage = e.message
            ) // return null
        }
    }

    fun getSignedIntUser() : UserData? = auth.currentUser?.run{
        UserData(
            userId = uid,
            userName = displayName,
            profilePictureUrl = photoUrl?.toString(),
            userEmail = email,
            isGuestUser = false
        )
    }
    private fun buildSignInRequest() : BeginSignInRequest {
        return BeginSignInRequest.Builder()
            .setGoogleIdTokenRequestOptions(
                GoogleIdTokenRequestOptions.builder()
                    .setSupported(true) // Means google auth is supported in our app
                    .setFilterByAuthorizedAccounts(false) // Makes sure that all the list of google accounts are displayed
                    .setServerClientId(getString(context, R.string.google_auth_server_ID))
                    .build()
            )
            // .setAutoSelectEnabled(true) // If there is only one account, automatically sign in with that account.
            .build()
    }
}
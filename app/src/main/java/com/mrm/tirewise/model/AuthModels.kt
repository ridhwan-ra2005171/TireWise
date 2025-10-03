package com.mrm.tirewise.model

data class SignInResult(
    val data: UserData?, // data from the user account
    val errorMessage: String?
)

data class UserData (
    val userId: String, // comes from firebase
    val userName: String?,
    val userEmail: String?,
    val profilePictureUrl: String?,
    val isGuestUser: Boolean = false,
)

data class SignInState(
    val isSignInSuccessful: Boolean = false,
    val signInError: String? = null
)

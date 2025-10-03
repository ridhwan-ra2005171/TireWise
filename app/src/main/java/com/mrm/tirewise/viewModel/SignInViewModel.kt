package com.mrm.tirewise.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.mrm.tirewise.model.SignInResult
import com.mrm.tirewise.model.SignInState
import com.mrm.tirewise.navigation.NavDestination
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignInViewModel(application: Application) : AndroidViewModel(application) {

    // Sign In State
    private val _signInState = MutableStateFlow(SignInState())
    val signInState : StateFlow<SignInState> = _signInState.asStateFlow()


    fun onSignInResult(result: SignInResult) { // called when we ge the sign in result
        // Update the sign in state
        _signInState.update {
            it.copy(
                isSignInSuccessful = result.data != null, // if we get a user (data), then it's successful.
                signInError = result.errorMessage
            )
        }
    }

    fun resetState() { // for logging out
        // Reset the sign in state
        _signInState.update {
            SignInState()
        }
    }

    fun checkAuth(navController: NavHostController) {
        viewModelScope.launch {
            if (FirebaseAuth.getInstance().currentUser == null)
                navController.navigate(NavDestination.Welcome.route) {
                    popUpTo(NavDestination.Welcome.route) {
                        inclusive = true
                    }
                }
            else navController.navigate(NavDestination.Dashboard.route) {
                popUpTo(NavDestination.Dashboard.route) {
                    inclusive = false
                }
            }
        }
    }
}

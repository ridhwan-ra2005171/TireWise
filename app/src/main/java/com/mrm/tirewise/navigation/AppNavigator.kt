package com.test.tirewise.navigation

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.android.gms.auth.api.identity.Identity
import com.mrm.tirewise.authentication.GoogleAuthUiClient
import com.mrm.tirewise.model.SignInResult
import com.mrm.tirewise.viewModel.SignInViewModel
import com.mrm.tirewise.networkConnectivity.ConnectivityObserver
import com.mrm.tirewise.networkConnectivity.NetworkConnectivityObserver
import com.mrm.tirewise.model.Reminder
//import com.mrm.tirewise.viewModel.SignInViewModel
import com.mrm.tirewise.model.TirePosition
import com.mrm.tirewise.navigation.NavDestination
import com.mrm.tirewise.view.reusablecomponents.CustomLoadingDialogBar
import com.mrm.tirewise.view.screens.ReminderForm
import com.mrm.tirewise.view.screens.ReminderScreen
import com.mrm.tirewise.view.screens.SplashScreen
import com.mrm.tirewise.view.screens.tireList.TireListScreen
import com.mrm.tirewise.view.screens.VehicleFormScreen
import com.mrm.tirewise.view.screens.VehicleListScreen
import com.mrm.tirewise.view.screens.camera.CameraScreen
import com.mrm.tirewise.view.screens.dashboard.DashboardScreen
import com.mrm.tirewise.view.screens.ResultScreen
import com.mrm.tirewise.view.screens.SettingsScreen
import com.mrm.tirewise.view.screens.TireCatalogScreen
import com.mrm.tirewise.view.screens.TireScanScreen
import com.mrm.tirewise.viewModel.CameraViewModel
import com.mrm.tirewise.viewModel.ReminderViewModel
import com.mrm.tirewise.viewModel.TireScanViewModel
import com.mrm.tirewise.viewModel.VehicleViewModel
import com.test.tirewise.view.screens.signIn.WelcomeScreen
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("SuspiciousIndentation")
@Composable
fun AppNavigator(
    navController: NavHostController,
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val startingDestination = NavDestination.Splash.route
    // ViewModels -------
    val signInViewModel = viewModel<SignInViewModel>()
    val signInState by signInViewModel.signInState.collectAsStateWithLifecycle()

    //// View Models ====================== ////
    // Camera View Model
    val cameraViewModel = viewModel<CameraViewModel>()
    // Vehicle View Model
    val vehicleViewModel = viewModel<VehicleViewModel>()
    // Reminder View Model
    val reminderViewModel = viewModel<ReminderViewModel>()
    // TireScan View Model
    val tireScanViewModel = viewModel<TireScanViewModel>()
    //// Dialog popups ==================== ////


    // the auth client
    val googleAuthUiClient by lazy { GoogleAuthUiClient(
        context = context,
        oneTapClient = Identity.getSignInClient(context))
    }

    var userData by remember {
        mutableStateOf(googleAuthUiClient.getSignedIntUser())
    }

    // We store the launcher that launches the google sign in intent/pop up for later use.
    // This laucnher will contact Google services to display the sign in pop up
    val launcher = rememberLauncherForActivityResult(
        // contract states what we wanna do
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = { result ->
            if (result.resultCode == RESULT_OK) {
                coroutineScope.launch {
                    val signInResult = googleAuthUiClient.getSignInResultFromIntent(
                        intent = result.data ?: return@launch
                    )
//                    Log.d("__SIGNIN?", " Sign In Success: ${signInResult.data?.userEmail}")
                    signInViewModel.onSignInResult(signInResult)
                    // Update user data
                    userData = signInResult.data
                }
            }
        }
    )



//    if (userData == null) {
//        userData = if (userData == null) UserData( "", "John Doe", "email@gmail.com", "https://i.pravatar.cc/300?u=a042581f4e29026707d") else userData
//    }

    var isWaitingForSignIn by remember { mutableStateOf(false) }
    AnimatedVisibility(isWaitingForSignIn) {
        CustomLoadingDialogBar()
    }

    //If the user already registered before, the signInState must be true.
    LaunchedEffect(key1 = userData?.userEmail) {
        signInViewModel.onSignInResult(
            SignInResult(
                data = userData,
                errorMessage = null
            )
        )
    }


    fun signInWithGoogle() {
        coroutineScope.launch {
            isWaitingForSignIn = true
            val signInIntentSender = googleAuthUiClient.signIn()
            launcher.launch(
                IntentSenderRequest.Builder(signInIntentSender ?: return@launch).build()
            ).also {
                isWaitingForSignIn = false
            }
//            signInViewModel.resetState() // reset the sign in state
            // When the sign in is successful, the LaunchedEffect(key1 = signInState.isSignInSuccessful) will be triggered
        }
    }

    fun signOutWithGoogle() {

        signInViewModel.resetState() // reset the sign in state

        coroutineScope.launch {
            googleAuthUiClient.signOut().also {
                userData = null
            }
        }

        navController.navigate(NavDestination.Welcome.route) {
            popUpTo(startingDestination) { // Clear the whole stack using the start destination
//                popUpTo(navController.graph.id) { // Clear the whole stack using the start destination
                inclusive = true
//                Log.d("__SIGNIN?", "Back stack trace: ${navController.graph.route}")
            }
        }
    }



    // Get vehicles list once the user is signed in
    LaunchedEffect(key1 = userData) {
        if (userData != null) {
            vehicleViewModel.getVehicles(userData!!.userId)
        }
    }


    // Network state/connectivity
    val  networkState = NetworkConnectivityObserver(context).observe().collectAsStateWithLifecycle(
        initialValue = ConnectivityObserver.Status.Unavailable
    )

    NavHost(
        navController = navController,
        startDestination = startingDestination,
    ) {
        // Splash Screen
        composable(NavDestination.Splash.route) {
            SplashScreen(
                checkAuthState = {
                    // Take care of navigating to Dashboard or Welcome screen depending on the user sign in status
                    if (userData != null) {
                        navController.navigate(NavDestination.Dashboard.route) {
                            popUpTo(NavDestination.Splash.route) {
                                inclusive = false
                            }
                        }
                    } else {
                        navController.navigate(NavDestination.Welcome.route) {
                            popUpTo(NavDestination.Splash.route) {
                                inclusive = false
                            }
                        }
                    }
//                    signInViewModel.checkAuth(navController)
                }
            )
        }

        // Welcome Screen
        composable(NavDestination.Welcome.route) {

            // This launcher only gets triggered when the sign in successful value changes
            // meaning when the sign in state becomes true
            LaunchedEffect(key1 = signInState.isSignInSuccessful) {
//                Log.d("__SIGNIN? 1", " Sign In Success: ${signInState.isSignInSuccessful}, user: ${userData?.userName}")

                if ( userData != null) {
//                    Log.d("__SIGNIN? 2", " Sign In Success: ${signInState.isSignInSuccessful}, user: ${userData?.userName}")
                    navController.navigate(NavDestination.Dashboard.route)
                }
//                Log.d( "__USER", "Key1: ${userData?.userEmail}" )
            }

            WelcomeScreen(
                networkState = networkState,
                onSignInClick = {
                    signInWithGoogle()
                },
                onGuestClick = {
                    navController.navigate(NavDestination.Camera.route)
                },
            )
        }

        composable(NavDestination.Camera.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Up,
                    animationSpec = tween(280)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Down,
                    animationSpec = tween(300)
                )
            }

        ) {
            CameraScreen(
                cameraViewModel = cameraViewModel,
                onConfirmPhoto = {
//                    Log.d("ConfirmPhoto", "onConfirmPhoto: Before ")
                    navController.navigate(NavDestination.ResultScreen.route) {
                        popUpTo(NavDestination.ResultScreen.route) {
                            inclusive = true
                        }
                    }
//                    Log.d("ConfirmPhoto", "onConfirmPhoto: After")
                },
            )
        }

        composable(NavDestination.Dashboard.route) {
//            Log.d( "__USER", "AppNavigator: ${userData?.userEmail}" )
//            Log.d( "__USER", "AppNavigator: ${signInState.isSignInSuccessful}" )
            val vehicleList = vehicleViewModel.vehicleList.collectAsStateWithLifecycle().value
            var reminderList: List<Reminder>? = emptyList<Reminder>()
            val currentVehicle = vehicleViewModel.currentVehicle.collectAsStateWithLifecycle().value?.let {
                reminderList = reminderViewModel.getRemindersPerVehicle(userData!!.userId, it.vehicleId).collectAsStateWithLifecycle(null).value
//                Log.d( "__CURRENT_VEHICLE", "AppNavigator: ${it.plateNo}" )
                it
            }
            DashboardScreen(
                vehicleList = vehicleList,
                reminderList = currentVehicle?.let {
                    reminderList = reminderViewModel.getRemindersPerVehicle(userData!!.userId, it?.vehicleId!!).collectAsStateWithLifecycle(null).value
//                    Log.d( "__CURRENT_VEHICLE", "AppNavigator: ${it.plateNo}" )
                    reminderList
                },
                userData = userData,
                currentVehicle = vehicleViewModel.currentVehicle,
                networkState = networkState,
                isFetchingVehicles = vehicleViewModel.waitingForResult,
                isFetchingReminders = reminderViewModel.waitingForResult,
                navController = navController,
                onLogout = {signOutWithGoogle()},
                changeCurrentVehicle = {
                    vehicleViewModel.assignCurrentVehicle(vehicleId = it)
                    currentVehicle?.let {
                        reminderList?.let {
                            reminderViewModel.getRemindersPerVehicle(userData!!.userId, currentVehicle.vehicleId)
                        }
                    }
                  },
                onCameraClick = {
                    navController.navigate(NavDestination.Camera.route)
                },
                onVehicleCardClick = {
                     currentVehicle?.let {
                         vehicleViewModel.setEditVehicle(true)
//                         Log.d( "__CURRENT_VEHICLE", "AppNavigator: ${it.plateNo}" )
                         navController.navigate(NavDestination.VehicleForm.route) {
                             popUpTo(NavDestination.Dashboard.route) {
                                 inclusive = true
                             }
                         }
                     }
                },
                onAddVehicle = {
                    vehicleViewModel.setEditVehicle(false)
                    navController.navigate(NavDestination.VehicleForm.route) {
                        popUpTo(NavDestination.VehicleForm.route) {
                            inclusive = true
                        }
                    }
                },
                onViewFrontRightTireHistory = {
                    tireScanViewModel.setCurrentTirePosition(TirePosition.FRONT_RIGHT)
                    navController.navigate(NavDestination.TireList.route) },
                onViewBackRightTireHistory = {
                    tireScanViewModel.setCurrentTirePosition(TirePosition.BACK_RIGHT)
                    navController.navigate(NavDestination.TireList.route) },
                onViewFrontLeftTireHistory = {
                    tireScanViewModel.setCurrentTirePosition(TirePosition.FRONT_LEFT)
                    navController.navigate(NavDestination.TireList.route) },
                onViewBackLeftTireHistory = {
                    tireScanViewModel.setCurrentTirePosition(TirePosition.BACK_LEFT)
                    navController.navigate(NavDestination.TireList.route) },
                onViewAllTireHistory = {
                    tireScanViewModel.setCurrentTirePosition(TirePosition.ALL)
                    navController.navigate(NavDestination.TireList.route)
                },
                onAddReminderClick = {
                    navController.navigate(NavDestination.ReminderForm.route)
                },
                onDeleteReminder = {
                    reminderViewModel.deleteReminder(it)
                    currentVehicle?.let {
                        reminderList?.let {
                            reminderViewModel.getRemindersPerVehicle(userData!!.userId, currentVehicle.vehicleId)
                        }
                    }

                }
            )
        }

        composable(NavDestination.ResultScreen.route) {
            ResultScreen(
                cameraViewModel = cameraViewModel,
                userIsRegistered = signInState.isSignInSuccessful, /*(googleAuthUiClient.getSignedIntUser() == null)*/
                vehicleId = vehicleViewModel.currentVehicle.value?.vehicleId,
                networkState = networkState,
                onClose = {
                    navController.navigate(NavDestination.Camera.route) {
                        popUpTo(NavDestination.Camera.route) {
                            inclusive = true
                        }
                    }
                },
                onSaveScan = { newTireScan ->
                    // Save the tire scan to firebase
                    tireScanViewModel.uploadTireScan(newTireScan)
                },
                onSetReminder = {
                    navController.navigate(NavDestination.ReminderForm.route) {
                        popUpTo(NavDestination.Camera.route) {
                            inclusive = true
                        }
                    }
                },
            )
        }


        // Add Vehicle Form
        composable(NavDestination.VehicleForm.route) {
            val currentVehicle = vehicleViewModel.currentVehicle.collectAsStateWithLifecycle().value
            VehicleFormScreen(
                // If edit vehicle is true, set the vehicle to be edited, else set it to null so it is like adding a new vehicle
                vehicle = if (vehicleViewModel.editVehicleState.value) currentVehicle else null,
                networkState = networkState,
                userId = userData?.userId!!,
                onNavigateUp = {
                    vehicleViewModel.setEditVehicle(false)
                    navController.navigateUp()
                },
                onSubmit = {
                    vehicleViewModel.upsertVehicle(it)
                    navController.navigateUp()
//                    Log.d("__VEHICLE_SUBMIT", "onSubmit: ${it.vehicleId}")
                },
                onRemoveVehicleImage = {
                    vehicleViewModel.deleteImageFromFirebaseStorage(it.toString())
                    navController.navigateUp()
                },
                onDeleteVehicle = {
                    vehicleViewModel.deleteVehicle(it)
                }
            )
        }

        // Vehicle List Screen "My Vehicles"
        composable(NavDestination.VehicleList.route) {
            val vehicleList = vehicleViewModel.vehicleList.collectAsStateWithLifecycle().value
            VehicleListScreen(
                vehicleList = vehicleList,
                networkState = networkState,
                userData = userData!!,
                navController = navController,
                onLogout =  {signOutWithGoogle()},
                onAddVehicle = {
                    vehicleViewModel.setEditVehicle(false)
                    navController.navigate(NavDestination.VehicleForm.route) {
                        popUpTo(NavDestination.VehicleForm.route) {
                            inclusive = true
                        }
                    }
                },
                navigateToVehicleDetails = {
                    navController.navigate(NavDestination.VehicleForm.route)
                }
            )
        }

        composable(NavDestination.Reminders.route)  {
//            val reminderList = reminderViewModel.getAllReminders( userData?.userId!!).collectAsStateWithLifecycle(null).value
            ReminderScreen(
                reminders = reminderViewModel.getAllReminders(userData?.userId!!).collectAsStateWithLifecycle(null).value,
                networkState = networkState,
                isFetchingReminders = reminderViewModel.waitingForResult,
                navController = navController,
                onLogout =  {signOutWithGoogle()},
                userData = userData!!,
                onAddReminderClick = {
                    navController.navigate(NavDestination.ReminderForm.route) {
                        popUpTo(NavDestination.ReminderForm.route)
                    }
                },
                onDeleteReminder = {
                    reminderViewModel.deleteReminder(it)
                }
            )
        }

        composable(NavDestination.ReminderForm.route) {
            val vehicleList = vehicleViewModel.vehicleList.collectAsStateWithLifecycle().value
            ReminderForm(
                vehicleList = vehicleList,
                networkState = networkState,
                userId =  userData?.userId!!,
                onSubmitReminder = {
                    reminderViewModel.addReminder(it);
//                    Log.d("__REMINDER_SUBMIT", "onSubmit: ${it.reminderId}")
                    navController.navigateUp()
                                   },
                onCancel = { navController.navigateUp() },
            )
        }

        composable(
            NavDestination.TireList.route,
            enterTransition = {fadeIn(animationSpec = tween(400)) },
            exitTransition = {slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(200)) },
        ) {
            val currentVehicleId = vehicleViewModel.currentVehicle.value?.vehicleId!!
            val tireList = tireScanViewModel.tireScansList.collectAsStateWithLifecycle().value
            val currentTirePosition = tireScanViewModel.currentTirePosition
//            Log.d( "__TIRE_LIST_POSITION", "currentTirePosition: ${currentTirePosition}")
            TireListScreen(
                tires = tireList,
                currentTirePosition = tireScanViewModel.currentTirePosition,
                networkState = networkState,
                onSortByClicked = {
                    tireScanViewModel.sortTireScans(currentVehicleId,it)
                },
                onNavigateUp = {navController.navigateUp()},
                onTireCardClicked = {
                    tireScanViewModel.setCurrentTireScan(it)
                    navController.navigate(NavDestination.TireScanScreen.route)
                }
            )
        }

        composable(
            NavDestination.TireScanScreen.route,
            enterTransition = { fadeIn(animationSpec = tween(200)) },
            exitTransition = {slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(300)) },
        ) {
            val currentTire = tireScanViewModel.currentTireScan.collectAsStateWithLifecycle().value
            TireScanScreen(
                tireScan = currentTire,
                networkState = networkState,
                onNavigateUp = {navController.navigateUp()},
                onSetReminder = {
                    navController.navigate(NavDestination.ReminderForm.route) },
                onDeleteScan = {
                    tireScanViewModel.deleteTireScan(currentTire)
                    navController.navigate(NavDestination.TireList.route) {
                        popUpTo(NavDestination.TireList.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        // Settings Screen
        composable(
            NavDestination.Settings.route,
            enterTransition = { fadeIn(animationSpec = tween(200)) },
            exitTransition = {slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(300)) },
        ) {
            SettingsScreen(
                onNavigateUp = {navController.navigateUp()},
            )
        }


        // Tire Catalog Screen
        composable(
            NavDestination.TireCatalog.route,
            enterTransition = { fadeIn(animationSpec = tween(200)) },
            exitTransition = {slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(300)) },
        ) {
            TireCatalogScreen(
                onNavigateUp = {navController.navigateUp()},
            )
        }

    }
}


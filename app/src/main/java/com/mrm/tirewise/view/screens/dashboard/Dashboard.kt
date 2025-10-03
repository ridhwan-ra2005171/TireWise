
package com.mrm.tirewise.view.screens.dashboard

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.mrm.tirewise.R
import com.mrm.tirewise.networkConnectivity.ConnectivityObserver
import com.mrm.tirewise.model.UserData
import com.mrm.tirewise.model.Reminder
import com.mrm.tirewise.model.Vehicle
import com.mrm.tirewise.navigation.NavDestination
import com.mrm.tirewise.view.reusablecomponents.CarInfoCard
import com.mrm.tirewise.navigation.DrawerContainer
import com.mrm.tirewise.view.reusablecomponents.LoadingBar
import com.mrm.tirewise.view.reusablecomponents.NetworkConnectivityScreen
import com.mrm.tirewise.view.screens.ReminderList
import com.mrm.tirewise.view.theme.BORDER_WIDTH
import com.mrm.tirewise.viewModel.VehicleViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.math.abs

@Composable
fun DashboardScreen(
    vehicleList: List<Vehicle>,
    reminderList: List<Reminder>?,
    userData: UserData?,
    currentVehicle: StateFlow<Vehicle?>,
    networkState: State<ConnectivityObserver.Status>,
    isFetchingVehicles: StateFlow<Boolean>,
    isFetchingReminders: StateFlow<Boolean>,
    navController: NavController,
    onLogout: () -> Unit,
    changeCurrentVehicle: (vehicleId: String) -> Unit,
    onVehicleCardClick: (vehicleId: String) -> Unit,
    onCameraClick: () -> Unit,
    onAddVehicle: () -> Unit,
    onViewFrontRightTireHistory: () -> Unit,
    onViewBackRightTireHistory: () -> Unit,
    onViewFrontLeftTireHistory: () -> Unit,
    onViewBackLeftTireHistory: () -> Unit,
    onViewAllTireHistory: () -> Unit,
    onAddReminderClick: () -> Unit,
    onDeleteReminder: (Reminder) -> Unit,
) {
    val isFetchingVehicles = isFetchingVehicles.collectAsStateWithLifecycle()

        DrawerContainer(
            navController = navController,
            userData = userData,
            onLogout = onLogout,
            topBarTitle = NavDestination.Dashboard.title
        ) {
            if ( networkState.value == ConnectivityObserver.Status.Unavailable) {
                NetworkConnectivityScreen(message = if (vehicleList.size == 0) "view homescreen." else "view your vehicles.",
                    showCameraButton = true,
                    onCameraClick = onCameraClick
                )
            } else if (isFetchingVehicles.value) {
                CustomLoadingScreen()
                return@DrawerContainer
            } else {
                DashboardContent(
                    vehicleList = vehicleList,
                    reminderList = reminderList,
                    currentVehicle = currentVehicle,
                    isFetchingReminders = isFetchingReminders,
                    onCurrentVehicleChange = changeCurrentVehicle,
                    onVehicleCardClick = onVehicleCardClick,
                    onCameraClick = onCameraClick,
                    onAddVehicle = onAddVehicle,
                    onViewFrontRightTireHistory = onViewFrontRightTireHistory,
                    onViewBackRightTireHistory = onViewBackRightTireHistory,
                    onViewFrontLeftTireHistory = onViewFrontLeftTireHistory,
                    onViewBackLeftTireHistory = onViewBackLeftTireHistory,
                    onViewAllTireHistory = onViewAllTireHistory,
                    onAddReminderClick = onAddReminderClick,
                    onDeleteReminder = onDeleteReminder
                )
            }
        }

}

@Composable
fun CustomLoadingScreen() {
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
//        LoadingCircle(modifier = Modifier.size(80.dp))
        LoadingBar(modifier = Modifier
            .height(50.dp)
            .clip(MaterialTheme.shapes.medium)
            .fillMaxWidth(0.5f) )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DashboardContent(
    vehicleList: List<Vehicle>,
    reminderList: List<Reminder>?,
    currentVehicle: StateFlow<Vehicle?>,
    isFetchingReminders: StateFlow<Boolean>,
    onCurrentVehicleChange: (vehicleId: String) -> Unit,
    onVehicleCardClick: (vehicleId: String) -> Unit,
    onCameraClick: () -> Unit,
    onAddVehicle: () -> Unit,
    onViewFrontRightTireHistory: () -> Unit,
    onViewBackRightTireHistory: () -> Unit,
    onViewFrontLeftTireHistory: () -> Unit,
    onViewBackLeftTireHistory: () -> Unit,
    onViewAllTireHistory: () -> Unit,
    onAddReminderClick: () -> Unit,
    onDeleteReminder: (Reminder) -> Unit,

) {
    //    val currentVehicle = currentVehicle.collectAsState()
//    Log.d( "__DASHBOARD", "DashboardContent: current vehicle: ${currentVehicle.value?.vehicleName}")

    val coroutineScope = rememberCoroutineScope()

    // Decoration related
    val arrowBackCorner = RoundedCornerShape(topEnd = 100.dp, bottomEnd = 100.dp)
    val arrowForwardCorner = RoundedCornerShape(topStart = 100.dp, bottomStart = 100.dp)

    // Pager state
    val pagerState = rememberPagerState(pageCount = { vehicleList.size + 1 })

    // Set the pagerState as the last viewed vehicle Iff the current vehicle is not null
//    LaunchedEffect(key1 = true) {
//        if (currentVehicle != null) {
//            coroutineScope.launch {
//                pagerState.scrollToPage(vehicleList.map { it.vehicleId }.indexOf(currentVehicle.value?.vehicleId))
//            }
//        }
//    }


    val byondBoundsPageCount = 2

    Box(modifier = Modifier
        .testTag("dashboard_screen")
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background)) {

        HorizontalPager(state = pagerState, beyondBoundsPageCount = byondBoundsPageCount) { page: Int ->
            // Change the current vehicle
            if (pagerState.settledPage < pagerState.pageCount-1) {
                onCurrentVehicleChange(vehicleList[pagerState.settledPage].vehicleId)
            }


            // Adding the "Add Vehicle" page at the end of the list
            if (pagerState.pageCount - 1 == page) {
                AddVehicleScreen( isVehicleListEmpty = vehicleList.size == 0, onAddVehicle = onAddVehicle)
                return@HorizontalPager
            }

            /// To help the dashboard from lagging when scrolling
            val page by rememberUpdatedState(newValue = page)

            val authorizedPage = { // authorized page means dont show the loading screen
                abs(pagerState.settledPage - page) <= byondBoundsPageCount
            }

            // Create a time period to show content
            val authorizedTiming by produceState(initialValue = false) {
                while (pagerState.isScrollInProgress) delay(50)
                if (abs(pagerState.settledPage - page) > 0) {
                    delay(1000)
                    while (pagerState.isScrollInProgress) delay(50)
                }
                value = true
            }
            // Conditions to show content
            val showContent by remember {
                derivedStateOf {
                    authorizedPage() && authorizedTiming
                }
            }

            // Show loading screen if the content is not authorized
            if (!showContent) {
                Box (Modifier.fillMaxSize()) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            } else {
                // The page content
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp),
                    topBar = {
                        CarInfoCard(
                            vehicle = vehicleList[page],
                            modifier = Modifier
                                .padding(top = 5.dp)
                                .then(
                                    if (pagerState.currentPage == 0)
                                        Modifier.padding(end = 40.dp)
                                    else
                                        Modifier.padding(horizontal = 40.dp)
                                )
                                .animateContentSize(),
                            onClick = { onVehicleCardClick(vehicleList[page].vehicleId) })
                    },
                    floatingActionButtonPosition = FabPosition.End,
                    floatingActionButton = {
                        FloatingActionButton(
                            modifier = Modifier
                                .testTag("scan_tire_button")
                                .size(72.dp)
                                .border(
                                    BORDER_WIDTH,
                                    MaterialTheme.colorScheme.tertiary,
                                    MaterialTheme.shapes.extraLarge
                                ),
                            contentColor = MaterialTheme.colorScheme.tertiary,
                            containerColor = MaterialTheme.colorScheme.primary,
                            shape = MaterialTheme.shapes.extraLarge,
                            onClick = {
                                onCameraClick()
                            }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_camera), modifier = Modifier
                                    .padding(8.dp)
                                    .size(46.dp),
                                contentDescription = "Scan Tire Button"
                            )
                        }
                    },
                ) { paddingValues ->

                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                            .padding(top = 10.dp)
                    ) {
                        // Tire History -----------------------------
                        DashboardDivider()
                        Text(
                            text = stringResource(id = R.string.title_view_tire_history),
                            fontSize = MaterialTheme.typography.titleLarge.fontSize,
                            fontWeight = FontWeight.Bold,
                        )

                        // Tire History Composable
                        FourWheelVehicleTireHistory(
                            onViewFrontRightTireHistory,
                            onViewBackRightTireHistory,
                            onViewFrontLeftTireHistory,
                            onViewBackLeftTireHistory,
                            onViewAllTireHistory
                        )

                        // Reminders -----------------------------
                        DashboardDivider()

                        Row(horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = stringResource(id = R.string.title_current_reminders),
                                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                                fontWeight = FontWeight.Bold
                            )
                            // Add Reminder Button
                            OutlinedButton(
                                modifier = Modifier
                                    .wrapContentWidth()
                                    .testTag("add_reminder_button"),
                                colors = ButtonDefaults.buttonColors(
                                    contentColor = MaterialTheme.colorScheme.onBackground,
                                    containerColor = MaterialTheme.colorScheme.primary),
                                border = BorderStroke( BORDER_WIDTH, MaterialTheme.colorScheme.onBackground),
                                shape = MaterialTheme.shapes.extraLarge,
                                onClick = {
                                    onAddReminderClick()
                                }
                            ) {
                                Text(text = "Add", fontWeight = FontWeight.Bold)
                                Icon(
                                    imageVector = Icons.Rounded.Add,
                                    contentDescription = "Create a new reminder"
                                )
                            }
                        }

                        // List of reminders
                        if ( reminderList == null
                            || pagerState.settledPage != page
                            // Check if any reminder in the lsit doesnt have the same vehicleId as the current vehicle
                            // then show loading screen
                            || (reminderList.isNotEmpty()
                                    && reminderList.first().vehicleId != vehicleList[pagerState.settledPage].vehicleId)
                            ) {
                            CircularProgressIndicator(modifier = Modifier.padding(top = 10.dp) )
                        } else {
                            ReminderList(
                                reminders = reminderList,
                                onDeleteReminder = onDeleteReminder
                            )
                        }
                    }
                }
            }

        }


        // Left swipe button
        // Hide the button when on the first page
        Box(
            modifier = Modifier
                .padding(vertical = 15.dp)
                .wrapContentSize()
                .align(Alignment.TopStart)
        ) {
            AnimatedVisibility(visible = pagerState.currentPage != 0,
                enter = slideInHorizontally(),
                exit = slideOutHorizontally() { -it }) {
                // Content of the left swipe button
                ElevatedButton(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.tertiary),
                    shape = arrowBackCorner,
                    modifier = Modifier
                        .defaultMinSize(minHeight = 1.dp, minWidth = 1.dp)
                        .border(BORDER_WIDTH, MaterialTheme.colorScheme.tertiary, arrowBackCorner)
                        .height(105.dp)
                        .width(40.dp),
                    contentPadding = PaddingValues(0.dp),
                    onClick = {
                        coroutineScope.launch {
                            // Call scroll to on pagerState
                            pagerState.animateScrollToPage(pagerState.currentPage - 1)
                        }
                    }) {
                    Icon(modifier = Modifier.fillMaxSize(),
                        imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowLeft,
                        contentDescription = "View Next Vehicle")
                }
            }
        }


        // Right swipe button
        // Hide the button when on the last page
        Box(
            modifier = Modifier
                .padding(vertical = 15.dp)
                .wrapContentSize()
                .align(Alignment.TopEnd)
        ) {
            AnimatedVisibility(visible = pagerState.currentPage != pagerState.pageCount - 1,
                enter = slideInHorizontally(initialOffsetX = { it }),
                exit = slideOutHorizontally(targetOffsetX = { it })
            ) {
                // Content of the right swipe button
                // ▶ Button that can be hidden
                ElevatedButton(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.tertiary),
                    shape = arrowForwardCorner,
                    modifier = Modifier
                        .defaultMinSize(minHeight = 1.dp, minWidth = 1.dp)
                        .border(
                            BORDER_WIDTH,
                            MaterialTheme.colorScheme.tertiary,
                            arrowForwardCorner
                        )
                        .height(105.dp)
                        .width(40.dp),
                    contentPadding = PaddingValues(0.dp),
                    onClick = {
                        coroutineScope.launch {
                            // Call scroll to on pagerState
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    }) {
                    Icon(
                        modifier = Modifier.fillMaxSize(),
                        imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                        contentDescription = "View Previous Vehicle"
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun DashboardPreview() {
    val carList  = viewModel<VehicleViewModel>().vehicleList.collectAsState().value

//    TireWiseTheme {
//        DashboardScreen(
//            carsList = carList,
//            currentVehicle = carList[0],
//            onAddVehicle = {},
//            changeCurrentVehicle = {},
//            onViewAllTireHistory = {},
//            onViewFrontRightTireHistory = {}, onViewBackRightTireHistory = {},
//            onViewFrontLeftTireHistory = {}, onViewBackLeftTireHistory = {}
//        )
//    }
}

@Composable
fun DashboardDivider( dividerFraction : Float = 1f, modifier: Modifier = Modifier) {
    HorizontalDivider(
        modifier = modifier
            .fillMaxWidth(dividerFraction)
            .padding(vertical = 3.dp)
            .background(MaterialTheme.colorScheme.onBackground)
    )
}

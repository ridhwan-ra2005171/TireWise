package com.mrm.tirewise.view.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.clock.ClockDialog
import com.maxkeppeler.sheets.clock.models.ClockConfig
import com.maxkeppeler.sheets.clock.models.ClockSelection
import com.mrm.tirewise.R
import com.mrm.tirewise.networkConnectivity.ConnectivityObserver
import com.mrm.tirewise.model.Reminder
import com.mrm.tirewise.model.Vehicle
import com.mrm.tirewise.utils.convert24toAMPM
import com.mrm.tirewise.utils.toMilliSeconds
import com.mrm.tirewise.view.reusablecomponents.CustomOutlinedButton
import com.mrm.tirewise.view.reusablecomponents.CustomTextField
import com.mrm.tirewise.view.reusablecomponents.NetworkConnectivityDialog
import com.mrm.tirewise.view.reusablecomponents.RadioButtonGroup2
import com.mrm.tirewise.view.reusablecomponents.VehicleListDropDownMenu
import com.mrm.tirewise.view.theme.BORDER_WIDTH
import com.mrm.tirewise.view.theme.SCREEN_PADDING
import com.mrm.tirewise.view.theme.TireWiseTheme
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReminderForm(
    vehicleList: List<Vehicle>,
    networkState: State<ConnectivityObserver.Status>,
    userId: String,
    onSubmitReminder: (Reminder) -> Unit,
    onCancel: () -> Unit,
    ) {
    TireWiseTheme {
        val context = LocalContext.current

        var selectedOption by remember { mutableStateOf("Tire Replacement") }
        var reminderTitle by remember { mutableStateOf("") }
        var reminderNotes by remember { mutableStateOf("") }
        var reminderTime by remember { mutableStateOf("") }
        var reminderDate by remember { mutableStateOf("") }

        var calendarState = rememberSheetState()
        var reminderTimeInAMandPM by remember { mutableStateOf("") }

        var selectedVehicle by remember { mutableStateOf<Vehicle?>(null) }

        var showNetworkDialog by remember { mutableStateOf(false) }
        if (showNetworkDialog) {
            NetworkConnectivityDialog{ showNetworkDialog = false }
        }

        //calendar popup
        // Don't allow user to select past dates than today, only allow dates from today onwards
        val dateBoundary = LocalDate.now().let { now -> now..LocalDate.MAX }

        CalendarDialog(
            state = calendarState,
            selection = CalendarSelection.Date { date ->
                val formattedDate = date.format(DateTimeFormatter.ofPattern("dd-MMM-yyyy"))
                reminderDate = formattedDate.toString()
                Log.d("AddReminder", "ReminderDate: $reminderDate")
            },
            config = CalendarConfig(
                monthSelection = true,
                yearSelection = true,
                boundary = dateBoundary
            )
        )

        //clock popup
        val clockState = rememberSheetState().also {
            ClockDialog(
                state = it,
                config = ClockConfig(
                    is24HourFormat = false
                ),
                selection = ClockSelection.HoursMinutes { hours, minutes ->
    //            reminderTime = "$hours:$minutes"
                    // if hours is 12 or less then  it is am. If hours is more than 12 then it is am
                    // if hours = 12 then keep it without modulus cause it is pm
                    // if hours = 24 then hours = 0, so it is am. We need to keep hours = 12
                    val hoursAM_PM_Format = if( hours == 0) 12 else if(hours <= 12) hours else hours%12
                    val  hoursAM_PM = if(  hours in 1..12 || hours == 24 ) "AM" else "PM"
                    reminderTimeInAMandPM = String.format("%02d:%02d %s", hoursAM_PM_Format, minutes, hoursAM_PM)

                    Log.d( "AddReminder", "Remindertime: hours: $hours mins: $minutes " )
                    reminderTime = String.format("%02d:%02d", hours, minutes)

                    Log.d( "AddReminder", "Remindertime: $reminderTime vs current time: ${LocalDateTime.now().toLocalTime()}")
                    Log.d("Reminder", "Remindertime: $hoursAM_PM_Format:$minutes $hoursAM_PM")
                }
            )
        }

        //get cars for drop down selection of cars
        Log.d("ReminderForm", "ReminderForm: ${vehicleList.size}")
        Scaffold(
            topBar = {
                TopAppBar(
                    modifier = Modifier.shadow(5.dp),
                    title = { Text(text = "Set Reminder", fontWeight = FontWeight.Bold, textAlign = TextAlign.Start, modifier = Modifier.fillMaxWidth()) },
                    navigationIcon = {
                        FilledIconButton(
                            onClick = onCancel,
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = MaterialTheme.colorScheme.background,
                                contentColor = MaterialTheme.colorScheme.onBackground
                            )) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                                contentDescription = null
                            )
                        }
                    }
                )
            },

            bottomBar = {
                Row(modifier = Modifier
                    .padding(horizontal = SCREEN_PADDING)
                    .padding(bottom = SCREEN_PADDING),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(SCREEN_PADDING)) {

                    // Cancel Button
                    Row(modifier = Modifier.weight(1f)) {
                        CustomOutlinedButton(
                            onClick = { onCancel() },
                            modifier = Modifier
                                .height(48.dp)
                                .weight(1f)
                        ) {
                            Text(text = "Cancel", fontWeight = FontWeight.Bold)
                        }
                    }


                    // Submit Button
                    Row(modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.spacedBy(SCREEN_PADDING)) {
                        CustomOutlinedButton(
                            backgroundColor = MaterialTheme.colorScheme.primary,
                            onClick = {
                                if (selectedOption == "Custom"){
                                    reminderTitle = reminderTitle
                                }else{
                                    reminderTitle = selectedOption
                                }
                                Log.d("ReminderAdd", "ReminderForm: $reminderTitle")

                                // Convert date and time to milliseconds
                                val dateTimeInMilli =
                                    getDateTimeInMilli(reminderDate, reminderTime)


//                                Log.d("ReminderAdd", "ReminderForm: $reminderTitle")
                                Log.d("AddReminderAdd", "ReminderForm: $dateTimeInMilli  " +
                                        "\nvs system millis: ${System.currentTimeMillis()} " +
                                        "\nvs zone date millis: ${ZonedDateTime.now().toInstant().toEpochMilli()}")
                                // Check if the date selected is not past the current date
                                if (networkState.value == ConnectivityObserver.Status.Unavailable) {
                                    showNetworkDialog = true
                                } else if ( selectedVehicle == null ) {
                                    Toast.makeText(context,"Please select a vehicle", Toast.LENGTH_LONG).show()
                                    return@CustomOutlinedButton
                                } else if ( dateTimeInMilli < System.currentTimeMillis() ) {
                                    Toast.makeText(
                                        context,
                                        "Date/Time cannot be in the past",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    return@CustomOutlinedButton
                                } else if (reminderTitle.isNotEmpty() && reminderDate.isNotEmpty() && reminderTime.isNotEmpty()) {

                                    reminderNotes

                                    val reminder = Reminder(
                                        vehicleId = selectedVehicle?.vehicleId!!, // vehicle Id
                                        vehicleTitle =  selectedVehicle?.vehicleName!!,
                                        userId = userId,
                                        reminderDate = reminderDate,
                                        reminderTitle = reminderTitle,
                                        reminderTime = reminderTime,
                                        reminderNotes = reminderNotes,
                                        reminderDateTime = dateTimeInMilli // using 24 hour format
                                        )
                                    Log.d("ReminderAdd", "ReminderForm: $reminder") //this works

                                    onSubmitReminder(reminder)
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Insufficient Information",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            },
                            modifier = Modifier
                                .height(48.dp)
                                .weight(1f)
                        ) {
                            Text(text = "Submit", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(it)
                    .padding(bottom = SCREEN_PADDING, start = SCREEN_PADDING, end = SCREEN_PADDING)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Spacer(modifier = Modifier.height(SCREEN_PADDING))
//                Text(text = "Set a reminder for this vehicle")

                VehicleListDropDownMenu(vehicleList = vehicleList) { selectedVehicle = it }

                Row(modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    HorizontalDivider(modifier = Modifier
                        .height(BORDER_WIDTH)
                        .weight(1f))

                    Text(
                        text = "Reminder Type",
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .background(MaterialTheme.colorScheme.background)
                    )
                    HorizontalDivider(modifier = Modifier
                        .height(BORDER_WIDTH)
                        .weight(1f))

                }//End of spacer that says 'Set a time'

                //make a radiobox with {tire replacement, tire Checkup, Custom}

                Column( modifier = Modifier.wrapContentHeight()) {
                    RadioButtonGroup2(
                        listTitle = "Reminder Type:",
                        options = listOf(
                            "Tire Replacement", "Tire Rotation",
                            "Tire Checkup", "Tire Alignment",
                            "Oil Change", "Insurance Renewal",
                        ),
                        selectedOption = selectedOption,
                        onOptionSelected = {
                            selectedOption = it
                        }
                    )
                }

                Column(modifier =
                Modifier
                    .clickable(onClick = { selectedOption = "Custom"; reminderTitle = "" })
                    .background(
                        MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.25f),
                        MaterialTheme.shapes.large
                    )) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Row {
                            RadioButton(
                                selected = "Custom" == selectedOption,
                                onClick = { selectedOption = "Custom"; reminderTitle = "" },
                                modifier = Modifier
                                    .size(24.dp)
                                    .padding(end = 4.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Custom*",
                                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                                fontWeight = FontWeight.Black,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }


                    AnimatedVisibility(selectedOption == "Custom") {
                        //Reminder Title
                        CustomTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = reminderTitle,
                            onValueChange = { reminderTitle = it },
                            label = "Reminder Title"
                        )

                    }
                }

                Row( // SPACER
                    modifier = Modifier
                        .fillMaxWidth()
//                        .padding(vertical = 16.dp)
                    ,
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    HorizontalDivider(modifier = Modifier
                        .height(BORDER_WIDTH)
                        .weight(1f))

                    Text(
                        text = "Please Set the Date and the Time",
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )

                    HorizontalDivider(modifier = Modifier
                        .height(BORDER_WIDTH)
                        .weight(1f))

                }

                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    //Date Picker
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .clip(MaterialTheme.shapes.large)
                        .clickable { calendarState.show() }
                        .clickable { calendarState.show() }) {
                        Row(verticalAlignment =  Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .border(
                                    BORDER_WIDTH,
                                    color = if (!reminderDate.isNullOrEmpty()) {
                                        MaterialTheme.colorScheme.secondaryContainer
                                    } else {
                                        MaterialTheme.colorScheme.error
                                    },
                                    MaterialTheme.shapes.large
                                )
                                .background(
                                    if (!reminderDate.isNullOrEmpty()) {
                                        MaterialTheme.colorScheme.background
                                    } else {
                                        MaterialTheme.colorScheme.errorContainer
                                    }, MaterialTheme.shapes.large
                                )
                        )
                        {
                            Spacer(modifier = Modifier.width(155.dp))

                            Text(text = if (!reminderDate.isNullOrEmpty()) {
                                reminderDate
                            } else {
                                "Date Not Set"
                            },
                                color = if (!reminderDate.isNullOrEmpty()) {
                                    MaterialTheme.colorScheme.tertiary
                                } else {
                                    MaterialTheme.colorScheme.error
                                },
                                fontWeight = FontWeight.Medium)
                        }

                        ExtendedFloatingActionButton(
                            modifier = Modifier
                                .width(150.dp)
                                .border(
                                    BORDER_WIDTH,
                                    color = MaterialTheme.colorScheme.tertiary,
                                    MaterialTheme.shapes.large
                                ),
                            elevation = FloatingActionButtonDefaults.elevation(0.dp),
                            containerColor = MaterialTheme.colorScheme.primary,
                            onClick = { calendarState.show() },
                            text = { Text(text = "Select Date", fontWeight = FontWeight.Bold) },
                            icon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_calendar_month_24),
                                    contentDescription = "Time Picker Icon"
                                )
                            }
                        )
                    }

                    //Time Picker Component --------------------------
                    Box(modifier = Modifier
                        .clip(MaterialTheme.shapes.large)
                        .clickable { clockState.show() }
                        .fillMaxWidth()) {
                        Row(verticalAlignment =  Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .border(
                                    BORDER_WIDTH,
                                    color = if (!reminderTime.isNullOrEmpty()) {
                                        MaterialTheme.colorScheme.secondaryContainer
                                    } else {
                                        MaterialTheme.colorScheme.error
                                    },
                                    MaterialTheme.shapes.large
                                )
                                .background(
                                    if (!reminderTime.isNullOrEmpty()) {
                                        MaterialTheme.colorScheme.background
                                    } else {
                                        MaterialTheme.colorScheme.errorContainer
                                    }, MaterialTheme.shapes.large
                                )
                        ) {
                            Spacer(modifier = Modifier.width(155.dp))

                            Text(text = if (!reminderTime.isNullOrEmpty()) { convert24toAMPM(reminderTime) }
                            else { "Time Not Selected" },
                                color = if (!reminderTime.isNullOrEmpty()) { MaterialTheme.colorScheme.tertiary } else {MaterialTheme.colorScheme.error },
                                fontWeight = FontWeight.Medium
                            )
                        }

                        ExtendedFloatingActionButton(
                            modifier = Modifier
                                .width(150.dp)
                                .border(
                                    BORDER_WIDTH,
                                    color = MaterialTheme.colorScheme.tertiary,
                                    MaterialTheme.shapes.large
                                ),
                            elevation = FloatingActionButtonDefaults.elevation(0.dp),
                            containerColor = MaterialTheme.colorScheme.primary,
                            onClick = { clockState.show() },
                            text = { Text(text = "Select Time", fontWeight = FontWeight.Bold) },
                            icon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_clock_24),
                                    contentDescription = "Time Picker Icon"
                                )
                            }
                        )
                    }
//
                }
//

                CustomTextField(label = "Add Notes (Optional)" , value = reminderNotes , onValueChange = { reminderNotes = it })


            }
        }
    }
}

private fun getDateTimeInMilli(reminderDate: String, reminderTimeIn24hourFormat: String): Long {
    return try {
        val localDateTime = LocalDateTime
            .parse("$reminderDate $reminderTimeIn24hourFormat",
                DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm")) // format example: 01-Jan-2022 21:00
        val deleteMeLaterDate = localDateTime.atZone(ZoneId.systemDefault()).toLocalDateTime()
            Log.d ( "AddReminderAdd", "getDateTimeInMilli: $localDateTime deleteMe: $deleteMeLaterDate" )
           val miliBoi = localDateTime.toMilliSeconds()
        Log.d( "AddReminderAdd", " ============NOW============= " )
        Log.d( "AddReminderAdd", "getDateTimeInMilli: ${miliBoi} VS ${LocalDateTime.now().toMilliSeconds()}" )
        miliBoi
    } catch ( e: Exception ) {
        Log.d( "ReminderAdd", "getDateTimeInMilli: $e" )
        0
    }

}


//composable to choose the vehicle

//for testing radio button
//@Composable
//fun RadioButtonExample() {
//    var selectedOption by remember { mutableStateOf("Option 1") }
//    Log.d("Testradio", "RadioButtonExample: $selectedOption")
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        RadioButtonGroup(
//            options = listOf("Tire Checkup", "Tire Replacement", "Tire Alignment", "Custom"),
//            selectedOption = selectedOption,
//            onOptionSelected = { selectedOption = it }
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        Text("Selected Option: $selectedOption", style = MaterialTheme.typography.bodyMedium)
//    }
//}



//@Preview
//@Composable
//fun RadioButtonExamplePreview() {
//    RadioButtonExample()
//}

@Preview
@Composable
fun ReminderFormPreview() {
    val reminder = Reminder(
        reminderId = "",
        vehicleId = "",
        vehicleTitle = "",
        userId = "",
        reminderTitle = "",
        reminderDate = "",
        reminderTime = "",
        reminderNotes = "",
        reminderDateTime = 0
    )

//    val vehicleViewModel = viewModel<VehicleViewModel>()
//    vehicleViewModel.getVehicles("FH4IpZRu32YAZJB7VCsAeYdMoyB3")
    val vehicleList = listOf(
        Vehicle (
            "1",
            "vehicle1",
            "911",
            "suzuki",
            "jemni",
            " Transport",
        ),
        Vehicle (
            "2",
            "vehicle2",
            "12322",
            "Hando",
            "Honda",
            "Granny big truck",
        ),
    )
//    ReminderForm(
//        vehicleList = vehicleList,
//        onSubmitReminder = {},
//        onCancel = {},
//        userId = "",
//        networkState = networkState,
//        )

}


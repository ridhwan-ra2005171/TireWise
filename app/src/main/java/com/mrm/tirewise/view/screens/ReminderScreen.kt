package com.mrm.tirewise.view.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.mrm.tirewise.R
import com.mrm.tirewise.model.Reminder
import com.mrm.tirewise.model.UserData
import com.mrm.tirewise.navigation.DrawerContainer
import com.mrm.tirewise.navigation.NavDestination
import com.mrm.tirewise.networkConnectivity.ConnectivityObserver
import com.mrm.tirewise.utils.convert24toAMPM
import com.mrm.tirewise.view.reusablecomponents.NetworkConnectivityScreen
import com.mrm.tirewise.view.theme.BORDER_WIDTH
import com.mrm.tirewise.view.theme.TireWiseTheme
import kotlinx.coroutines.flow.StateFlow

@Composable
fun ReminderScreen(
    reminders: List<Reminder>?,
    networkState: State<ConnectivityObserver.Status>,
    isFetchingReminders: StateFlow<Boolean>,
    userData: UserData, onAddReminderClick: () -> Unit,
    onLogout: () -> Unit,
    navController: NavController,
    onDeleteReminder: (Reminder) -> Unit
) {
    DrawerContainer(
        navController = navController,
        userData = userData,
        onLogout = onLogout,
        topBarTitle = NavDestination.Reminders.title) {
        if (networkState.value == ConnectivityObserver.Status.Unavailable) {
            NetworkConnectivityScreen(" view your reminders.")
        } else {
            ReminderScreenContent(
                reminders,
                isFetchingReminders,
                onAddReminderClick,
                onDeleteReminder
            )
        }
    }
}
@Composable
fun ReminderScreenContent(
    reminders: List<Reminder>?,
    isFetchingReminders: StateFlow<Boolean>,
    onAddReminderClick: () -> Unit,
    onDeleteReminder: (Reminder) -> Unit
) {
        val isFetchingReminders = isFetchingReminders.collectAsStateWithLifecycle()

//        Log.d ("__REMINDERSCREEN", "ReminderScreenContent: ${isFetchingReminders.value}")
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            floatingActionButton = {
                // // Add Reminder Button // //
                ExtendedFloatingActionButton(
                    modifier = Modifier.border(
                        width = BORDER_WIDTH,
                        color = MaterialTheme.colorScheme.tertiary,
                        shape = MaterialTheme.shapes.extraLarge
                    ),
                    contentColor = MaterialTheme.colorScheme.tertiary,
                    containerColor = MaterialTheme.colorScheme.primary,
                    shape = MaterialTheme.shapes.extraLarge,
                    text = { Text(text = "Add Reminder") },
                    icon = {
                        Icon(
                            imageVector = Icons.Rounded.Add,
                            contentDescription = "Create a new reminder"
                        )
                    },
                    onClick = {
                        onAddReminderClick()
                    })
            },
            floatingActionButtonPosition = FabPosition.Center
        ) {
//            if (reminders == null) {
//                Text("No reminders found", modifier = Modifier.testTag("noRemindertxt"))
//            }
//            else {
                Column(modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .padding(10.dp)) {
//                    if (reminders.isNullOrEmpty()) {
//                        Text(
//                            text = "There are no reminders",
//                            style = MaterialTheme.typography.titleMedium,
//                            color = MaterialTheme.colorScheme.onBackground,
//                            textAlign = TextAlign.Center,
//                            fontWeight = FontWeight.Bold,
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .fillMaxHeight(0.9f)
//                                .padding(top = 50.dp)
//                        )
//                    } else {
//                        if (isFetchingReminders.value) {
//                            Log.d ("__REMINDERSCREEN", "ReminderScreenContent: ${isFetchingReminders.value}")
//                            CustomLoadingScreen()
//                        } else {
                            ReminderList(
                                reminders,
                                onDeleteReminder = onDeleteReminder)
//                        }
//                    }
//                }
//            }
        }
    }
}


@Composable
fun ReminderList(
    reminders: List<Reminder>?,
    onDeleteReminder: (Reminder) -> Unit
) {
    if (reminders.isNullOrEmpty()) {
        Text("No reminders found",
            modifier = Modifier
                .padding(top = 20.dp)
                .fillMaxWidth(),
            color = Color.LightGray,
            textAlign =  TextAlign.Center,
            fontSize = MaterialTheme.typography.bodyMedium.fontSize)
        return
    }
    LazyColumn(
//        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background),
    ) {
        items(reminders.size) { index ->
            ReminderCard(reminders[index], onDeleteReminder = onDeleteReminder)
        }
        item {
            Spacer(modifier = Modifier.height(70.dp))
        }
    }
}


@Composable
fun ReminderCard(
    reminder: Reminder,
    onDeleteReminder: (Reminder) -> Unit,
) {

    var isExpanded by remember { mutableStateOf(false) }
    var deleteClicked by remember { mutableStateOf(false) }
    var deleteConfirmed by remember { mutableStateOf(false) }
    val context = LocalContext.current

    if (deleteConfirmed) {
        return
    }

    if (deleteClicked) {
        AlertDialog(
            modifier = Modifier.border(BORDER_WIDTH, MaterialTheme.colorScheme.onBackground,MaterialTheme.shapes.extraLarge),
            onDismissRequest = { deleteClicked = false },
            dismissButton = {
                TextButton(onClick = { deleteClicked = false }) {
                    Text(text = "Cancel", color = MaterialTheme.colorScheme.onBackground)
                }
            },
            confirmButton = {
                TextButton(onClick = {

                    deleteClicked = false
                    deleteConfirmed = true
                    onDeleteReminder( reminder )
                    Toast.makeText(context, "Deleting reminder may take some time...", Toast.LENGTH_LONG).show()
                }) {
                    Text(text = "Delete", color = MaterialTheme.colorScheme.error, modifier = Modifier.testTag("confirmDelete"))
                }
            },
            title = { Text(text = "Delete Reminder") },
            text = { Text(text = "Are you sure you want to delete this reminder?") })
    }

    Row(modifier = Modifier.animateContentSize().padding(vertical = 8.dp)) {
        OutlinedCard(
            shape = MaterialTheme.shapes.large,
            border =  BorderStroke(BORDER_WIDTH, MaterialTheme.colorScheme.onBackground),
            colors = CardDefaults.cardColors(
                contentColor = MaterialTheme.colorScheme.tertiary,
                containerColor = MaterialTheme.colorScheme.secondaryContainer),
            modifier = Modifier
                .clip(MaterialTheme.shapes.large)
                .clickable { isExpanded = !isExpanded }
                .animateContentSize()
                .fillMaxWidth()
//            .border(BORDER_WIDTH, Color.Black, RoundedCornerShape(16.dp))
        ) {
            Box {
                // Content of the card goes here
                Column(
                    modifier = Modifier
//                        .fillMaxWidth()
                        .padding(10.dp)
                ) {
                    Row {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_bell),
                            contentDescription = null)
                        Text(
                            text = " ${reminder.reminderTitle} for ${reminder.vehicleTitle?:""}",
                            style = MaterialTheme.typography.titleSmall)
                    }

                    Spacer(modifier = Modifier.height(5.dp))

                    if (!reminder.reminderNotes.isNullOrEmpty()) {
                        Text(text = buildAnnotatedString {
                                withStyle( style = SpanStyle(fontWeight = FontWeight.Bold)){
                                    append("Notes: ")
                                }
                                append(reminder.reminderNotes)
                            },
                            style = MaterialTheme.typography.bodyMedium)
                    }

                    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "set to ${convert24toAMPM(reminder.reminderTime)} on ${reminder.reminderDate}",
                            style = MaterialTheme.typography.bodySmall,
                        )
                        AnimatedVisibility(!isExpanded) {
                            Text(
                                text = "Tap to Delete",
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Medium)
                        }

                    }
                }
            }

            if (isExpanded) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    ExtendedFloatingActionButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.errorContainer)
                            .border(
                                BORDER_WIDTH,
                                color = MaterialTheme.colorScheme.tertiary,
//                                MaterialTheme.shapes.large
                            ),
                        onClick = { deleteClicked = true },
                        text = { Text(text = "Delete Reminder") },
                        icon = { Icon(imageVector = Icons.Rounded.Delete, contentDescription = "") },
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                }
            }
        }



    }

}

@Composable
@Preview
fun ReminderScreenPreview() {
    val reminderList = listOf(
        Reminder(
            "1",
            "vehicle1",
            "","",
            "Oil Change",
            "2022-02-20 09:00",
            "Maintenance",
            "Change oil every 5000 miles",
        ),
        Reminder(
            "2",
            "vehicle2",
            "","",
            "Tire Rotation",
            "2022-02-25 14:30",
            "Maintenance",
            "Rotate tires every 10000 miles",
        ),
        Reminder(
            "3",
            "vehicle3","",
            "",
            "Insurance Renewal",
            "2022-03-10 10:45",
            "Insurance",
            "Renew vehicle insurance by the due date",
        ),
        Reminder(
            "4",
            "vehicle4",
            "","",
            "Brake Inspection",
            "2022-03-15 12:00",
            "Maintenance",
            "Inspect brakes every 15000 miles",
        ),
        Reminder(
            "5",
            "vehicle5",
            "","",
            "Air Filter Replacement",
            "2022-03-20 08:00",
            "Maintenance",
            "Replace air filter every 12000 miles",

        ),
    )


    TireWiseTheme {
//        ReminderScreen(reminders = reminderList,
//            navController = rememberNavController(), userData = UserData( "1", "user1", "",""), onAddReminderClick = {}, onLogout = {})
    }
}

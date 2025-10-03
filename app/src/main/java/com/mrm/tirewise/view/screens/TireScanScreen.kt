package com.mrm.tirewise.view.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.mrm.tirewise.R
import com.mrm.tirewise.model.ClassificationResult
import com.mrm.tirewise.model.TireScan
import com.mrm.tirewise.networkConnectivity.ConnectivityObserver
import com.mrm.tirewise.view.reusablecomponents.CustomOutlinedButton
import com.mrm.tirewise.view.reusablecomponents.LoadingBar
import com.mrm.tirewise.view.reusablecomponents.NetworkConnectivityScreen
import com.mrm.tirewise.view.theme.BORDER_WIDTH
import com.mrm.tirewise.view.theme.SCREEN_PADDING

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TireScanScreen(
    tireScan: TireScan,
    networkState: State<ConnectivityObserver.Status>,
    onDeleteScan: () -> Unit,
    onNavigateUp: () -> Unit,
    onSetReminder: () -> Unit,
) {

    val context = LocalContext.current

    // Set the result colors
    val resultColors = ClassificationResult().getResult(tireScan.tireCondition)

    val cornerVal = MaterialTheme.shapes.large.topStart
    val cornerShape = RoundedCornerShape( topStart = cornerVal, topEnd = cornerVal, bottomStart = cornerVal, bottomEnd = cornerVal )

    var deleteClicked by remember {
        mutableStateOf(false)
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
                    onDeleteScan()
                    Toast.makeText(context, "Scan Deleted Successfully", Toast.LENGTH_SHORT).show()
                }) {
                    Text(text = "Delete", color = MaterialTheme.colorScheme.error)
                }
            },
            title = { Text(text = "Delete Scan") },
            text = { Text(text = "Are you sure you want to delete this tire scan?") })
    }

    Scaffold(
        topBar = {

            TopAppBar(
                modifier = Modifier.shadow(10.dp),
                title = { Text(text = "Tire Scan", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    FilledIconButton(
                        onClick = onNavigateUp,
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = MaterialTheme.colorScheme.background,
                            contentColor = MaterialTheme.colorScheme.onBackground)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = null
                        )
                    }
                }

            )
        },

        bottomBar = {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(SCREEN_PADDING),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(30.dp)) {

                    // Delete Scan Button
                    Row(modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                        CustomOutlinedButton(
                            onClick = {deleteClicked = true},
                            backgroundColor = MaterialTheme.colorScheme.error
                        ) {
                            Text(
                                text = "Delete Scan",
                                color = Color.White,
//                                    fontWeight = FontWeight.Bold,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    Row(modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                        // Close Button
                        CustomOutlinedButton(
                            onClick = onNavigateUp,
                        ) {
                            Text(
                                text = "Close",
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    ) {
        // Check if network is unavailable
        if (networkState.value != ConnectivityObserver.Status.Available) {
            NetworkConnectivityScreen()
            return@Scaffold
        }
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(it)
                    .padding(SCREEN_PADDING)
                    .fillMaxWidth()
                    .scrollable(rememberScrollState(), Orientation.Vertical)
                    .background(MaterialTheme.colorScheme.background)
            ) {
                Box(
                    modifier = Modifier
                        .clip(cornerShape)
                        .padding(10.dp)
                        .aspectRatio(1f)
                        .border(BORDER_WIDTH, resultColors.color, cornerShape)
                ) {

                    SubcomposeAsyncImage(
                        contentScale = ContentScale.Crop,
                        loading = { LoadingBar(Modifier.fillMaxSize())},
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(cornerShape),
                        model = tireScan.tireImageUri,
                        contentDescription = "tire image"
                    )
                }

                // Result Title
                Text(
                    text = resultColors.title,
                    textAlign = TextAlign.Center,
                    color = resultColors.color,
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    fontWeight = FontWeight.Bold,
                )

                // Result Description
                Text(
                    text = resultColors.description,
                    textAlign = TextAlign.Center,
                    color = resultColors.color,
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(8.dp)
                )

                 // A text box for adding notes
                if (!tireScan.additionalNotes.isNullOrEmpty()) {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        Text(
                            text = "Notes:",
                            color = MaterialTheme.colorScheme.tertiaryContainer,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = tireScan.additionalNotes!!,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }


                // Reminder button if the condition is not good
                if (!tireScan.tireCondition.startsWith("good", ignoreCase = true)) {
                    Column {
                        Text(
                            text =
                            if (tireScan.tireCondition.startsWith("bad", ignoreCase = true))
                                "Set Reminder to Replace the Tire:"
                            else
                                "Set Reminder to Checkup on Tire",
                            color = MaterialTheme.colorScheme.tertiary,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                                .fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                        CustomOutlinedButton(
                            onClick = onSetReminder,
                            backgroundColor = MaterialTheme.colorScheme.tertiaryContainer,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Set Reminder",
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp),
                                textAlign = TextAlign.Center
                            )
                            Icon(
                                painter = painterResource(id = R.drawable.ic_bell),
                                contentDescription = ""
                            )
                        }
                    }
                }
            }
        }
    }
}
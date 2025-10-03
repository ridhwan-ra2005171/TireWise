package com.mrm.tirewise.view.screens

import android.util.Log
import androidx.compose.animation.animateContentSize
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.mrm.tirewise.R
import com.mrm.tirewise.model.ClassificationResult
import com.mrm.tirewise.model.Result
import com.mrm.tirewise.model.TirePosition
import com.mrm.tirewise.model.TireScan
import com.mrm.tirewise.networkConnectivity.ConnectivityObserver
import com.mrm.tirewise.utils.classifyImage
import com.mrm.tirewise.utils.saveBitmapToFile
import com.mrm.tirewise.view.reusablecomponents.CustomOutlinedButton
import com.mrm.tirewise.view.reusablecomponents.CustomTextField
import com.mrm.tirewise.view.reusablecomponents.NetworkConnectivityDialog
import com.mrm.tirewise.view.reusablecomponents.TirePositionDialog
import com.mrm.tirewise.view.theme.BORDER_WIDTH
import com.mrm.tirewise.view.theme.SCREEN_PADDING
import com.mrm.tirewise.view.theme.TireWiseTheme
import com.mrm.tirewise.viewModel.CameraViewModel

//@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultScreen(
    cameraViewModel: CameraViewModel,
    userIsRegistered: Boolean,
    vehicleId: String?,
    onClose: () -> Unit,
    onSaveScan: (TireScan) -> Unit,
    onSetReminder: () -> Unit,
    networkState: State<ConnectivityObserver.Status>,
) {
    // Display "No Internet Connection" popup if network is unavailable
    var showNetworkDialog by remember { mutableStateOf(false) }
    if (showNetworkDialog) {
        NetworkConnectivityDialog{
            showNetworkDialog = false
        }
    }
    // The Tire Object
    val tireObject = TireScan()

    var expandImage by remember {
        mutableStateOf(false)
    }

    // The Tire Image
    val tireImageBitmap by cameraViewModel.bitmap.collectAsState()

    val context = LocalContext.current

    var additionalNotes by remember {
        mutableStateOf("")
    }
    var loadingClassification by remember {
        mutableStateOf( true)
    }

    var imageClassificationResult by remember {
        mutableStateOf("")
    }

    var resultColors : Result by remember {
        mutableStateOf(Result(Color.Magenta, "Amazing!", "Yo change ya tires brother!"))
    }

    var showTirePositionDialog by remember {
        mutableStateOf(false)
    }
    var chosenPosition: TirePosition? by remember {
        mutableStateOf(null)
    }

    fun assignTireData() {
        tireObject.vehicleId = vehicleId!!
        tireObject.tirePosition = chosenPosition?.getPositionAsString()!!
        tireObject.additionalNotes = additionalNotes
        tireObject.tireCondition = imageClassificationResult
        tireObject.tireConditionDesc = resultColors.description
        tireObject.tireImageUri = saveBitmapToFile(context, tireImageBitmap).toString()
    }


    if (showTirePositionDialog) {
        TirePositionDialog(onDismissRequest = { showTirePositionDialog = false },
            onSave = {
                chosenPosition = it
                showTirePositionDialog = false
                if (chosenPosition != null) {
                    assignTireData()
                    onSaveScan(tireObject);
                    onClose()
                    Log.d("__STORAGE_TAG", "Save Scan: ${tireObject.tireImageUri}")
                }
            }
        )
    }

    LaunchedEffect(key1 = Unit ) {
        // the classification label e.g. Good, Poor, etc...
        imageClassificationResult = classifyImage(tireImageBitmap, context)
        loadingClassification = false
        resultColors = ClassificationResult().getResult(imageClassificationResult)
    }

    val cornerVal = MaterialTheme.shapes.large.topStart
    val cornerShape = RoundedCornerShape( topStart = cornerVal, topEnd = cornerVal, bottomStart = cornerVal, bottomEnd = cornerVal )

    Scaffold(
        topBar = {
            TopAppBar(modifier = Modifier.shadow(16.dp),
                title = { Text(text = "Scan Result",
                    fontWeight = FontWeight.Bold,
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center)
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

                    // Change the buttons depending on the user's sign-in status
                    if (userIsRegistered) {
                        // Ignore Scan Button
                        Row(modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                            CustomOutlinedButton(
                                onClick = onClose,
                                backgroundColor = MaterialTheme.colorScheme.tertiaryContainer
                            ) {
                                Text(
                                    text = "Ignore Scan",
//                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }

                        Row(modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                            // Save Button
                            CustomOutlinedButton(
                                backgroundColor = MaterialTheme.colorScheme.primary,
                                onClick = {
                                    if (networkState.value == ConnectivityObserver.Status.Unavailable) {
                                        showNetworkDialog = true
                                    } else if (chosenPosition == null) {
                                        showTirePositionDialog = true
                                    }
                                    // The save function is done in the tire position dialog
                                },
                            ) {
                                Text(
                                    text = "Save Scan",
//                                    fontWeight = FontWeight,
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }

                    } else {
                        CustomOutlinedButton(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = onClose,
                            backgroundColor = MaterialTheme.colorScheme.primary
                        ) {
                            Text(text = "Close", modifier = Modifier.fillMaxWidth(0.3f), textAlign = TextAlign.Center)
                        }
                    }
                }
            }
        }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(it)
                    .padding(SCREEN_PADDING)
                    .fillMaxWidth()
                    .scrollable(rememberScrollState( ), Orientation.Vertical)
                    .background(MaterialTheme.colorScheme.background)
            ) {
                Box(
                    modifier = Modifier
//                        .clickable{ expandImage = !expandImage }
                        .animateContentSize()
                        .clip(cornerShape)
                        .padding(10.dp)
                        .fillMaxWidth(if (expandImage) 1f else 0.85f)
                        .aspectRatio(1f)
                        .border(BORDER_WIDTH, resultColors.color, cornerShape)
                ) {
                    if (loadingClassification) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .padding(20.dp)
                                .fillMaxSize()
                        )
                    } else {
                        AsyncImage(
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(cornerShape),
                            model = tireImageBitmap,
                            contentDescription = "tire image"
                        )
                    }
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
                
                //// The components below would get hidden when the user is a registered user
                if (userIsRegistered) {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        // A text box for adding notes
                        CustomTextField(
                            value = additionalNotes,
                            onValueChange = {additionalNotes = it},
                            singleLine = false,
                            label = "Add Notes",
                            minLines = 2,
                            maxLines = 3,
                            modifier = Modifier
                                .fillMaxWidth()
//                                .padding(8.dp)
                        )

                        // Reminder button if the condition is not good
                        if (!imageClassificationResult.startsWith("good", ignoreCase = true)) {
                            Column {
                                Text(
                                    text =
                                    if (imageClassificationResult.startsWith("bad", ignoreCase = true))
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
                                    onClick = {
                                        if (networkState.value == ConnectivityObserver.Status.Unavailable) {
                                            showNetworkDialog = true
                                        } else {
                                            onSetReminder()
                                        }
                                    },
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
    }
}

@Preview
@Composable
fun ResultScreenPreview() {
    TireWiseTheme {
//        ResultScreen(
//            cameraViewModel = CameraViewModel(),
//            userIsRegistered = true,
//            vehicleId = "",
//            uploadingTireScan =  StateFlow(false),
//            onClose = {},
//            onSaveScan = {},
//            onSetReminder = {},
//        )
    }
}
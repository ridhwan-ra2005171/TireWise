package com.mrm.tirewise.view.screens

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import coil.compose.SubcomposeAsyncImage
import com.mrm.tirewise.R
import com.mrm.tirewise.model.Vehicle
import com.mrm.tirewise.networkConnectivity.ConnectivityObserver
import com.mrm.tirewise.utils.createImageFile
import com.mrm.tirewise.view.reusablecomponents.CustomOutlinedButton
import com.mrm.tirewise.view.reusablecomponents.CustomTextField
import com.mrm.tirewise.view.reusablecomponents.LoadingBar
import com.mrm.tirewise.view.reusablecomponents.NetworkConnectivityDialog
import com.mrm.tirewise.view.theme.BORDER_WIDTH
import com.mrm.tirewise.view.theme.TireWiseTheme
import java.util.Objects

private val testVehicle = Vehicle("test", "test", "test", "test", "test")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehicleFormScreen(
    vehicle: Vehicle?,
    networkState: State<ConnectivityObserver.Status>,
    userId: String,
    onNavigateUp: () -> Unit,
    onRemoveVehicleImage: (imageUri: Uri) -> Unit,
    onDeleteVehicle: (vehicle: Vehicle) -> Unit,
    onSubmit: (Vehicle) -> Unit,
) {
    TireWiseTheme {
        Scaffold(
            topBar = {
                 TopAppBar(
                    modifier = Modifier
                        .shadow(5.dp)
                        .testTag("vehicleformtopappbar"),
                    title = { Text(text = "Vehicle Form", fontWeight = FontWeight.Bold) },
                    navigationIcon = {
                        FilledIconButton(
                            onClick = onNavigateUp,
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
            }
        ) { paddingValues ->
//            Log.d("__VEHICLE_FORM", "VehicleFormScreen: ${vehicle?.imageUri}")
            VehicleForm(
                vehicle = vehicle,
                networkState = networkState,
                userId = userId,
                paddingValues = paddingValues,
                onRemoveVehicleImage = onRemoveVehicleImage,
                onDeleteVehicle = {
                    onDeleteVehicle(vehicle!!)
                    onNavigateUp()
                },
                onSubmit =  { onSubmit(it); onNavigateUp() }
            )
        }
    }
}
//@Preview
@SuppressLint("SuspiciousIndentation")
@Composable
fun VehicleForm(
    vehicle: Vehicle?,
    userId: String,
    paddingValues: PaddingValues = PaddingValues(0.dp),
    onRemoveVehicleImage: (imageUri: Uri) -> Unit,
    onDeleteVehicle: () -> Unit,
    onSubmit: (Vehicle) -> Unit,
    networkState: State<ConnectivityObserver.Status>,
) {

//    Log.d("__VEHICLE_FORM", "VehicleForm: ${vehicle?.plateNo} userId: $userId")
    val context = LocalContext.current

    // --- For Image Capture ---
    // Temporary file to store the image Uri from camera
    val file = context.createImageFile()
    // The captured image temporary Uri
    val capturedImageTempUri = FileProvider.getUriForFile(
        Objects.requireNonNull(context),
        "com.mrm.tirewise.provider", file
    )

    // The final image URI that needs to be saved to firebase
//    var imageUri : Uri? by remember { mutableStateOf(vehicle?.imageUri?.toUri()) }
    // If there is a vehicle passed, assign the imageUri to the vehicles imageUri
//    if ( vehicle?.imageUri != null || vehicle?.imageUri != "") {
//        imageUri = vehicle?.imageUri?.toUri()
//    }

    // The temporary URI that will be used until the user clicks on "Save"
//    var tempImageUri: Uri? by remember { mutableStateOf(imageUri)}
    var tempImageUri: Uri? by remember { mutableStateOf(vehicle?.imageUri?.toUri())}

    if (vehicle?.imageUri == null) tempImageUri = Uri.EMPTY


//    Log.d( "__URI", "imageUri: $imageUri,\n tempImageUri: $tempImageUri")
//    Log.d( "__URI", "vehicle Image Uri: ${vehicle?.imageUri}")
    // Launch the camera
    val imageCaptureLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                tempImageUri = capturedImageTempUri
                Log.d("__URI_Success", "Take Photo: $tempImageUri")
                // Assigning the image bitmap
//                imageBitmap = getBitmapFromUri(context, imageUri!!)
                // Cropping the image to square aspect ratio
//                image         Bitmap = cropImageToSquare(imageBitmap!!)
                // Convert the image bitmap to uri
//                imageUri = getUriFromBitmap(context, imageBitmap!!)
            }
        }

    // Launch the photo gallery
    val imagePickerLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                tempImageUri = it
//                // Assigning the image bitmap
//                imageBitmap = getBitmapFromUri(context, imageUri!!)
//                // Cropping the image to square aspect ratio
//                imageBitmap = cropImageToSquare(imageBitmap!!)
                // Convert the image bitmap to uri
//                imageUri = getUriFromBitmap(context, imageBitmap!!)
            }
        }

    fun launchImageCapture() {
        // Create the file name to store the image in
//        if (tempImageUri == null)
//        tempImageUri = createMediaUri(context, "VEHICLE_IMAGE_")
        imageCaptureLauncher.launch(capturedImageTempUri)
    }

    fun launchImagePicker() {
        // Create the file name to store the image in
//        if (tempImageUri == null)
//        tempImageUri = createMediaUri(context, "VEHICLE_IMAGE_")
        imagePickerLauncher.launch("image/*") // we specify we only want images using image/*
    }



    // License Plate Number
    var licensePlate by remember { mutableStateOf(vehicle?.plateNo ?: "") }
    // Vehicle Make/Brand
    var brand by remember { mutableStateOf(vehicle?.vehicleMake ?: "") }
    // Vehicle Model
    var model by remember { mutableStateOf(vehicle?.vehicleBrand ?: "") }
    // ------- optional ------- //
    // Vehicle Name
    var name by remember { mutableStateOf(vehicle?.vehicleName?:"") }
    // Model Year
    var year : String by remember { mutableStateOf(vehicle?.yearMake?:"") }
    // Vehicle Color
    var color by remember { mutableStateOf(vehicle?.vehicleColor?:"") }
    // Additional Information
    var additionalInfo by remember { mutableStateOf(vehicle?.additionalInfo?:"") }
    // Vehicle Image
    var oldVehicleImage by remember { mutableStateOf(vehicle?.imageUri?.toUri()) }

    val spacedByPadding = 5.dp

    val errorColor = MaterialTheme.colorScheme.error
    val successColor = MaterialTheme.colorScheme.primary

    var submitClicked by remember {
        mutableStateOf(false)
    }

    var deleteClicked by remember {
        mutableStateOf(false)
    }

    var isUploadingImageToFirebase by remember {
        mutableStateOf(false)
    }

    var showNetworkDialog by remember {
        mutableStateOf(false)
    }

    if (showNetworkDialog) {
        NetworkConnectivityDialog {
            showNetworkDialog = false
        }
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
                // Proceed to delete the vehicle
                TextButton(onClick = {
                    deleteClicked = false
                    onDeleteVehicle()
                    Toast.makeText(context, "Vehicle Deleted Successfully", Toast.LENGTH_LONG).show()
                }) {
                    Text(text = "Delete", color = MaterialTheme.colorScheme.error, modifier = Modifier.testTag("confirmDelete"))
                }
        },
            title = { Text(text = "Delete Vehicle") },
            text = { Text(text = "Are you sure you want to delete this vehicle?") })
    }

//    if (isUploadingImageToFirebase) {
//        CustomLoadingDialogBar()
//    }


    Scaffold(
        bottomBar = {
            Box(modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .padding(top = BORDER_WIDTH)) {
                Row(modifier = Modifier
//                    .shadow(elevation = 30.dp)
//                    .border(2.dp, MaterialTheme.colorScheme.onTertiary, MaterialTheme.shapes.large)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Delete Button
                    if (vehicle != null) {
                        Row(modifier = Modifier.weight(1f)) {
                            // Delete Vehicle Button
                            CustomOutlinedButton(
                                onClick = {
                                    if (networkState.value != ConnectivityObserver.Status.Available){
                                        showNetworkDialog = true
                                    } else {
                                        deleteClicked = true
                                    }
                                          },
                                backgroundColor = MaterialTheme.colorScheme.error
                            ) {
                                Text(
                                    text = "Delete Vehicle",
                                    color = MaterialTheme.colorScheme.onError,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("deletevechicle1"),
                                    textAlign = TextAlign.Center,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Row(modifier = Modifier.weight(1f)) {
                        // Submit Button
                        CustomOutlinedButton(
                            backgroundColor = MaterialTheme.colorScheme.primary,
                            onClick = {
                                Log.d(
                                    "__URI_IMAGE",
                                    "Vehicle image removed: ${tempImageUri.toString() != vehicle?.imageUri} \n" +
                                            "Car Image Uri: ${vehicle?.imageUri}\n tempImageUri: $tempImageUri"
                                )

                                // Check if network is available to submit
                                if (networkState.value != ConnectivityObserver.Status.Available) {
                                    showNetworkDialog = true
                                }
                                // Field checking
                                if (licensePlate.isBlank() || brand.isBlank() || model.isBlank()) {
                                    submitClicked = true
                                    isUploadingImageToFirebase = true
                                    Toast.makeText(
                                        context,
                                        "Please fill in all the required fields",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    return@CustomOutlinedButton
                                }

                                // Assign the tempImageUri to the imageUri
//                                imageUri = tempImageUri

                                // If is in edit mode & the imageUri variable is not the same as the vehicle's imageUri,
                                // delete the old imageUri
                                if (vehicle != null) {
                                    if (!vehicle.imageUri.isNullOrEmpty()
                                        && tempImageUri.toString() != vehicle.imageUri.toString()) {
                                        onRemoveVehicleImage(vehicle.imageUri!!.toUri())
                                    }
                                }

                                // Final image uri to be saved to the vehicle object
                                var finalImageUri = tempImageUri.toString()
                                // If the tempImageUri is the same as the old vehicle imageUri,
                                // assign the final image Uri to the original vehicle's imageUri
                                Log.d( "__FINAL_URI", "${ vehicle != null }")
                                Log.d( "__FINAL_URI", "${ tempImageUri == oldVehicleImage }")

                                if ( tempImageUri == oldVehicleImage ) {
//                                    Log.d( "__FINAL_URI", "${ tempImageUri == oldVehicleImage }")
                                    Log.d( "__FINAL_URI", "Bruh!!!!, ${ vehicle?.imageUri }")
                                    finalImageUri = vehicle?.imageUri!!
                                }

                                onSubmit(
                                    Vehicle(
                                        userId = userId,
                                        plateNo = licensePlate,
                                        vehicleMake = brand,
                                        vehicleBrand = model,
                                        vehicleName = if (name.isBlank()) "$brand • $model" else name,
                                        yearMake = year,
                                        vehicleColor = color,
                                        additionalInfo = additionalInfo,
                                        imageUri = finalImageUri.also {
                                            Log.d( "__WHY", it)
                                        }
                                    ).apply {
                                        // Assign a new vehicle id if it is null, otherwise keep the old one
                                        if (vehicle != null) {
                                            vehicleId = vehicle.vehicleId
                                        }
                                    }
                                )
//                                Log.d( "__URI", "submitClicked ==============")
//                                Log.d( "__URI", "imageUri: $imageUri,\n tempImageUri: $tempImageUri")
//                                Log.d( "__URI", "vehicle Image Uri: ${vehicle?.imageUri}")
                                Log.d("__URI", "vehicle Image Uri: ${vehicle?.imageUri}")

                                Toast.makeText(context,"Your vehicle is being saved...", Toast.LENGTH_LONG).show()
                            },
                        ) {
                            Text(
                                text = "Submit",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("submitButton"),
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Bold
                            )
                        }
//                        }
                    }
                }
            }
        }
    ) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
                .padding(it)
                .padding(paddingValues)
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(spacedByPadding)
        ) {
            // TODO: Add Vehicle Form
            CustomTextField(
                modifier = Modifier.testTag("licensePlate"),
                label = "License Plate Number",
                value = licensePlate,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                onValueChange = { licensePlate = it },
                outlineColor = if (submitClicked && licensePlate.isBlank()) errorColor else successColor)
            CustomTextField(
                modifier = Modifier.testTag("vehicleBrand"),
                label = "Vehicle Brand",
                placeholder = "e.g. Honda",
                value = brand,
                onValueChange = { brand = it },
                outlineColor = if (submitClicked && brand.isBlank()) errorColor else successColor)
            CustomTextField(
                modifier = Modifier.testTag("vehicleModel"),
                label = "Vehicle Model",
                placeholder = "e.g. Civic",
                value = model,
                onValueChange = { model = it },
                outlineColor = if (submitClicked && model.isBlank()) errorColor else successColor)


            // Optional fields
            Box {
                OutlinedCard(
                    modifier = Modifier.padding(top = 10.dp),
                    shape = MaterialTheme.shapes.extraLarge,
                    border = BorderStroke(2.dp, MaterialTheme.colorScheme.tertiary)
                ) {
                    Column(
                        modifier = Modifier.padding(11.dp),
                        verticalArrangement = Arrangement.spacedBy(spacedByPadding)
                    ) {
                        CustomTextField(
                            label = "Vehicle Name",
                            placeholder = "e.g. Tyrone's Car",
                            value = name,
                            onValueChange = { name = it })
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(modifier = Modifier.weight(1f)) {
                                CustomTextField(
                                    label = "Model Year",
                                    value = year,
                                    onValueChange = { year = it })
                            }
                            Row(modifier = Modifier.weight(1f)) {
                                CustomTextField(
                                    label = "Vehicle Color",
                                    value = color,
                                    onValueChange = { color = it })
                            }
                        }
                        CustomTextField(label = "Additional Information",
                            maxLines = 3,
                            singleLine = false,
                            value = additionalInfo, onValueChange = { additionalInfo = it })

                        Spacer(modifier = Modifier.height(3.dp))
                        /// Take a picture of the vehicle ============= ////

                        Box(modifier = Modifier
                            .fillMaxSize()
                            .testTag("AddPicture")) {
                            AddPictureButton(
                                onCapturePicture = { launchImageCapture() },
                                onPickPictureFromGallery = { launchImagePicker() },
                            )
                                this@Column.AnimatedVisibility(tempImageUri.toString().isNotEmpty()
                                        || tempImageUri.toString().isNotBlank() ) {
                                    OutlinedCard(
                                        border = BorderStroke(BORDER_WIDTH, color = MaterialTheme.colorScheme.tertiary),
                                        shape = MaterialTheme.shapes.large,
                                        modifier = Modifier
                                            .wrapContentSize()
                                            .animateContentSize()
                                    ) {
                                        Box {
                                            SubcomposeAsyncImage(loading = {LoadingBar(Modifier.fillMaxSize()) },
                                                modifier = Modifier.aspectRatio(1f),
                                                model = tempImageUri,
                                                contentScale = ContentScale.Crop,
                                                contentDescription = "New vehicle image")
                                            IconButton(onClick = {
                                                tempImageUri = Uri.EMPTY
                                                Log.d( "__URI", "Removing tempImageUri: ${tempImageUri}")
                                            },
                                                modifier = Modifier
                                                    .padding(8.dp)
                                                    .align(Alignment.TopEnd),
                                                colors = IconButtonDefaults.iconButtonColors(contentColor = MaterialTheme.colorScheme.onBackground, containerColor = MaterialTheme.colorScheme.background)) {
                                                Icon(imageVector = Icons.Rounded.Close, contentDescription = "Remove Vehicle Image")
                                            }
                                        }
                                        //                                    Row(modifier = Modifier
                                        //                                        .fillMaxWidth()
                                        //                                        .background(MaterialTheme.colorScheme.primaryContainer)) {
                                        //                                        TextButton(
                                        //                                            onClick = {launchImageCapture()}
                                        //                                        ) {
                                        //                                            Text(color = MaterialTheme.colorScheme.tertiary, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "Retake Vehicle Image", textDecoration = TextDecoration.Underline)
                                        //                                        }
                                        //                                    }
                                }
                            }
                        }
                    }
                }
                Text(
                    text = " Optional ", modifier = Modifier
                        .align(Alignment.TopCenter)
                        .background(color = MaterialTheme.colorScheme.background)
                )
            }
        }
    }
}



@Composable
fun AddPictureButton(onCapturePicture: () -> Unit, onPickPictureFromGallery: () -> Unit) {
    var expand by remember { mutableStateOf(false) }
    OutlinedCard(
        colors = CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.background),
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.tertiary),
//        elevation = ButtonDefaults.elevatedButtonElevation(1.dp),
        shape = MaterialTheme.shapes.large,
    ) {

        Column(  modifier = Modifier
            .animateContentSize()
            .fillMaxWidth()
            .wrapContentHeight(),
            horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(0.dp) ) {

            TextButton(onClick = { expand = !expand }, modifier = Modifier.fillMaxWidth(), shape = RectangleShape, colors = ButtonDefaults.textButtonColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)) {
                Row(
                    horizontalArrangement = if (expand) Arrangement.SpaceBetween else Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Add an Image of Your Vehicle",
                        color = if (expand) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.tertiary,
                        fontWeight = FontWeight.Bold
                    )
                    AnimatedVisibility(visible = expand) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "",
                            tint = if (expand) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.tertiary
                        )
                    }
                }
            }

//            if (expand) {
//                TextButton(onClick = onCapturePicture, modifier = Modifier.fillMaxWidth()) {
//                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
//                        Icon(modifier = Modifier
//                            .size(36.dp)
//                            .padding(end = 8.dp),
//                            painter = painterResource(id = R.drawable.ic_camera_add),
//                            contentDescription = "",
//                            tint = MaterialTheme.colorScheme.tertiary
//                        )
//                        Text(
//                            text = "Take a picture of Your Vehicle",
//                            fontSize = MaterialTheme.typography.bodyLarge.fontSize,
//                            color = MaterialTheme.colorScheme.tertiary,
//                            modifier = Modifier.padding(start = 5.dp)
//                        )
//                    }
//                }
//                DashboardDivider()

                TextButton(onClick = onPickPictureFromGallery, modifier = Modifier.fillMaxWidth()) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                        Icon(modifier = Modifier
                            .size(36.dp)
                            .padding(end = 8.dp),
                            painter = painterResource(id = R.drawable.icon_iamge),
                            contentDescription = "",
                            tint = MaterialTheme.colorScheme.tertiary
                        )
                        Text(
                            text = "Choose from Photo Gallery",
                            fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                            color = MaterialTheme.colorScheme.tertiary,
                            modifier = Modifier.padding(start = 5.dp),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
//        }
//        Row(horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically, modifier = Modifier
//            .fillMaxWidth()
//            .height(40.dp)
////            .padding(horizontal = 8.dp, vertical = 16.dp)
//        ) {
//            Text(text = "Take a picture of Your Vehicle", fontSize = MaterialTheme.typography.bodyLarge.fontSize, color = MaterialTheme.colorScheme.tertiary, modifier = Modifier.padding(start = 5.dp))
//            Icon(
//                painter = painterResource(id = R.drawable.add_a_photo_fill1_wght400_grad0_opsz24),
//                contentDescription = "Add",
//                tint = MaterialTheme.colorScheme.tertiary
//            )
//        }
    }
}

@Preview
@Composable
fun VehicleFormPreview() {
    TireWiseTheme {
//        VehicleFormScreen(null, "", {}, {}, {})
    }
}

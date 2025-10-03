package com.mrm.tirewise.view.reusablecomponents

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.mrm.tirewise.model.Vehicle
import com.mrm.tirewise.view.theme.BORDER_WIDTH
import com.mrm.tirewise.view.theme.TireWiseTheme

//@Preview
@Composable
fun VehicleListDropDownMenu(vehicleList: List<Vehicle>, onVehicleSelected: (Vehicle) -> Unit) {


    var expanded by remember { mutableStateOf(false) }
    var selectedVehicle by remember { mutableStateOf<Vehicle?>(null) }

    fun selectVehicle(vehicle: Vehicle) {
        expanded = false
        selectedVehicle = vehicle
        onVehicleSelected(vehicle)
    }
    Column(modifier = Modifier) {

        AnimatedVisibility(visible = selectedVehicle != null) {
            Column(
                Modifier
                    .clip(MaterialTheme.shapes.large)
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .border(
                        BORDER_WIDTH,
                        color = MaterialTheme.colorScheme.tertiary,
                        shape = MaterialTheme.shapes.large
                    )) {

                CarInfoCard(vehicle = selectedVehicle!!, onClick =  { expanded = !expanded },
                    modifier = Modifier
                        .height(100.dp)
                )
                Row(horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .clickable { expanded = !expanded }
                        .fillMaxWidth()
                        .padding(5.dp),) {
                    Text(text = "Switch Vehicle", textDecoration = TextDecoration.Underline, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(10.dp))
                    Icon(imageVector = Icons.Filled.Edit, contentDescription = "Edit Icon")

                }
            }
        }


        AnimatedVisibility(visible = selectedVehicle == null) {
            OutlinedButton(
                shape = MaterialTheme.shapes.extraLarge,
                border = BorderStroke(2.dp, MaterialTheme.colorScheme.error),
                onClick = { expanded = true },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.error
                ),
                modifier = Modifier
                    .height(100.dp)
                    .fillMaxWidth()
                    .testTag("chooseVehicle")
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    Text(
                        textDecoration = TextDecoration.Underline,
                        fontWeight = FontWeight.Bold,
                        fontSize = MaterialTheme.typography.titleMedium.fontSize,
                        textAlign = TextAlign.Center,
                        text = "Tap here to choose your Vehicle"
                    )
                    Text(
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        text = "You must choose a Vehicle to set the Reminder to."
                    )

                }
            }
        }


        // Vehicle List Dropdown
        MaterialTheme(shapes = MaterialTheme.shapes.copy(extraSmall = RoundedCornerShape(20.dp))) {
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .animateContentSize()
                    .background(MaterialTheme.colorScheme.background)
                    .border(
                        BORDER_WIDTH,
                        color = MaterialTheme.colorScheme.tertiary,
                        shape = RoundedCornerShape(20.dp)
                    )
                    .padding(vertical = 2.dp, horizontal = 10.dp)
            ) {
                if (vehicleList.isEmpty()) {
                    Text(text = "No Vehicles Found", modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp), fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                } else {
                    vehicleList.forEach { vehicle ->
                        Row(horizontalArrangement = Arrangement.spacedBy(16.dp),
                            modifier = Modifier
                                .clip(RoundedCornerShape(13.dp))
                                .clickable { selectVehicle(vehicle) }
                                .background(MaterialTheme.colorScheme.secondaryContainer)
                                .fillMaxWidth()
                                .height(100.dp)
                        ) {
                            // Vehicle Image
                            SubcomposeAsyncImage(
                                model = vehicle.imageUri, // Replace with your actual image resource
                                contentScale = ContentScale.Crop,
                                loading = {
                                    LoadingCircle(
                                        Modifier
                                            .fillMaxSize()
                                            .padding(10.dp)
                                    )
                                },
                                contentDescription = "vehicle Image",
                                modifier = Modifier.aspectRatio(1f)
                            )
                            Column(verticalArrangement = Arrangement.SpaceEvenly,
                                modifier = Modifier.fillMaxHeight().padding(8.dp)
                                    .padding(end = 16.dp)
                            ) {
                                vehicle.vehicleName?.let {
                                    Text(
                                        text = it,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = MaterialTheme.typography.titleMedium.fontSize
                                    )
                                }

                                Text(text = buildAnnotatedString {
                                        withStyle(
                                            style = SpanStyle(
                                                fontWeight = FontWeight.Normal,
                                                fontSize = MaterialTheme.typography.bodyMedium.fontSize
                                            )
                                        ) {
                                        append("Plate Number: ")
                                        }
                                        withStyle(
                                            style = SpanStyle(
                                                fontWeight = FontWeight.Black,
                                            )
                                        ) {
                                            append(vehicle.plateNo)
                                        }
                                    }
                                )
                            }

                        }
                        if (vehicleList.lastIndex != vehicleList.indexOf(vehicle)) {
                            Spacer(modifier = Modifier.height(15.dp))
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewVehicleListDropDownMenu() {
    val vehicleList = listOf(
        Vehicle (
            "1",
            "vehicle1",
            "911",
            "suzuki",
            "jemni",
            "Taliban Transport",
        ),
        Vehicle (
            "2",
            "vehicle2",
            "12322",
            "Hando",
            "Honda",
            "Granny big toe",
        ),
    )
    TireWiseTheme {
        Column(
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)) {
            VehicleListDropDownMenu(vehicleList, {})
//            { selectedVehicle = it }
        }
    }
}
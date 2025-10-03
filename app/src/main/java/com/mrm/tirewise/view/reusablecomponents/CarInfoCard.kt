package com.mrm.tirewise.view.reusablecomponents

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mrm.tirewise.model.Vehicle
import com.mrm.tirewise.view.theme.BORDER_WIDTH
import com.mrm.tirewise.view.theme.TireWiseTheme

//@Preview
@Composable
fun CarInfoCard(vehicle : Vehicle, modifier: Modifier = Modifier, onClick : () -> Unit) {
    OutlinedButton(
        contentPadding = PaddingValues(0.dp),
        onClick = onClick,
        border = BorderStroke(BORDER_WIDTH, color = MaterialTheme.colorScheme.onBackground),
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.background, contentColor = MaterialTheme.colorScheme.onBackground),
        shape = MaterialTheme.shapes.large,
        modifier = modifier
//            .fillMaxWidth()
            .height(110.dp)
//            .shadow(10.dp, shape = MaterialTheme.shapes.large)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Start
        ) {
            // Car Image
            VehicleImage(imageUri = vehicle.imageUri, plateNumber = vehicle.plateNo)
            //// Car Info
            Column(modifier = Modifier
                    .padding(vertical = 0.dp, horizontal = 6.dp)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.Start
            ) {
                // if the vehicle name is blank, we show the vehicle model + make only

                vehicle.vehicleName?.let {
                    if (!vehicle.vehicleName?.contains("•")!!
                        && !vehicle.vehicleName?.isBlank()!!) {
                        Text(
                            text = vehicle.vehicleName!!,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                fontSize = MaterialTheme.typography.titleMedium.fontSize
                            )
                        )
                    }
                }

                Text(
                    text = vehicle.vehicleMake+" • "+vehicle.vehicleBrand,
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = MaterialTheme.typography.titleSmall.fontSize
                    )
                )

                vehicle.yearMake?.let {
                    if(!vehicle.yearMake?.isBlank()!!) {
                        Text(
                            text = "Model Year: " + vehicle.yearMake.toString(),
                            maxLines = 1,
                            style = TextStyle(
                                fontWeight = FontWeight.Medium,
                                fontSize = MaterialTheme.typography.bodyMedium.fontSize
                            )
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun CarInfoCardPreview() {
    TireWiseTheme {
        CarInfoCard(vehicle =
        Vehicle(plateNo = "ABC-1234",
            vehicleName = "Bro whats?",
            yearMake = "",
            vehicleMake = "Toyota",
            vehicleBrand = "Corolla"),)
        {}
    }
}
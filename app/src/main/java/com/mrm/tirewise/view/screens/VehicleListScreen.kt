package com.mrm.tirewise.view.screens

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.mrm.tirewise.R
import com.mrm.tirewise.model.UserData
import com.mrm.tirewise.model.Vehicle
import com.mrm.tirewise.navigation.DrawerContainer
import com.mrm.tirewise.navigation.NavDestination
import com.mrm.tirewise.networkConnectivity.ConnectivityObserver
import com.mrm.tirewise.view.reusablecomponents.LicensePlate
import com.mrm.tirewise.view.reusablecomponents.NetworkConnectivityScreen
import com.mrm.tirewise.view.theme.BORDER_WIDTH
import com.mrm.tirewise.view.theme.TireWiseTheme


@Preview
@Composable
fun VehiclesListScreenPreview() {
    TireWiseTheme {
//        VehicleListScreen( )
    }
}

@Composable
fun VehicleListScreen(
    vehicleList : List<Vehicle>,
    networkState: State<ConnectivityObserver.Status>,
    userData: UserData,
    navigateToVehicleDetails : () -> Unit,
    onLogout: () -> Unit,
    onAddVehicle: () -> Unit,
    navController: NavController
) {
    DrawerContainer(navController = navController, userData = userData, onLogout = onLogout, topBarTitle = NavDestination.VehicleList.title) {
        if (networkState.value == ConnectivityObserver.Status.Unavailable) {
            NetworkConnectivityScreen(if ( vehicleList.isEmpty()) " add new vehicles." else " view your vehicles.")
        } else {
            VehicleListScreenContent(vehicleList = vehicleList, navigateToVehicleDetails = navigateToVehicleDetails, onAddVehicle = onAddVehicle)
        }
    }
}
@Composable
fun VehicleListScreenContent(
    vehicleList : List<Vehicle>,
    navigateToVehicleDetails : () -> Unit,
    onAddVehicle : () -> Unit
    ) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            ExtendedFloatingActionButton(
                modifier = Modifier.border(
                    width = BORDER_WIDTH,
                    color = MaterialTheme.colorScheme.tertiary,
                    shape = MaterialTheme.shapes.extraLarge
                ),
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.tertiary,
                shape = MaterialTheme.shapes.extraLarge,
                text = { Text(text = "Add Vehicle") },
                icon = { Icon(imageVector =  Icons.Rounded.Add, contentDescription = "Create a new vehicle") },
                onClick = onAddVehicle)
        },
        floatingActionButtonPosition = FabPosition.Center
    ) {
        LazyColumn(modifier = Modifier
            .padding(it),
            horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(15.dp)) {
            item {
                Spacer(modifier = Modifier.height(10.dp))
            }
            items(vehicleList.size) {
                VehicleListItem(
                    vehicle = vehicleList[it], onClick = navigateToVehicleDetails)
            }
            item {
                Spacer(modifier = Modifier.height(70.dp))
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehicleListItem(vehicle: Vehicle, onClick: () -> Unit) {
    /*val vehicleName = vehicle.vehicleName
     Convert vehicle placeholder image from R.drawable to bitmap
    val convertedToBitmap = BitmapFactory.decodeResource( Application().resources,
        R.drawable.placeholder_vehicle);
    val veihcleImage= vehicle.vehicleImage?: painterResource(id = R.drawable.placeholder_vehicle)
    convertedToBitmap*/

    val vehicleDisplayName = if (!vehicle.vehicleName.isNullOrBlank()) {
        vehicle.vehicleName
    } else {
        "${vehicle.vehicleMake} • ${vehicle.vehicleBrand}"
    }

    Box(modifier = Modifier.fillMaxWidth()) {
        OutlinedCard(
            onClick = onClick,
            border = BorderStroke(BORDER_WIDTH, color = MaterialTheme.colorScheme.tertiary),
            shape = MaterialTheme.shapes.extraLarge,
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(0.8f)
                .aspectRatio(ratio = 1 / 1.1f)
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(BORDER_WIDTH, MaterialTheme.colorScheme.tertiary)
                        .padding(vertical = 10.dp)
                ) {
                    if (vehicleDisplayName != null) {
                        Text(
                            text = vehicleDisplayName, // The
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                }

                Box {
                    // Vehicle Image

                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(vehicle.imageUri)
                            .crossfade(true)
                            .build().also {
                                Log.d("__IMAGE", "Vehicle Image URI: ${vehicle.imageUri}")
                            },
                        contentDescription = "Vehicle Image",
                        modifier = Modifier.fillMaxHeight(),
                        placeholder = painterResource(R.drawable.default_car_image_gray),
                        error = painterResource(R.drawable.default_car_image_gray),
                        contentScale = ContentScale.Crop
                    )

                    // License Plate
                    LicensePlate(
                        plateNumber = vehicle.plateNo,
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(15.dp)
                            .fillMaxWidth(0.5f),
//                            .padding(vertical = 10.dp),
                        fontSize = MaterialTheme.typography.headlineMedium.fontSize,
                        borderColor = Color.Black
                    )

                }
//            Image(bitmap = ImageBitmap(),
//                contentScale = ContentScale.Crop,
//                modifier = Modifier
//                    .fillMaxSize(),
//                contentDescription = "")
            }
        }
    }
}
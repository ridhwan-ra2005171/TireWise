package com.mrm.tirewise.view.reusablecomponents

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.mrm.tirewise.R
import com.mrm.tirewise.view.theme.BORDER_WIDTH


@Composable
fun VehicleImage(imageUri: String?, plateNumber: String) {
    var vehicleImage : Any = R.drawable.default_car_image_gray
    if (imageUri != null) {
        vehicleImage = imageUri
    }
    val borderShape = MaterialTheme.shapes.large
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .fillMaxHeight()
            .clip(shape = borderShape)
    ) {
        // Car Image
//        AsyncImage(
//            modifier = Modifier
//                    shape = borderShape)
//                .aspectRatio(1f)
//                .fillMaxWidth(),
//            contentScale = ContentScale.Crop,
//            model = R.drawable.default_car_iamge,
//            contentDescription = "Tire Image"
//        )
        SubcomposeAsyncImage(loading = {LoadingBar(Modifier.fillMaxSize()) },
            model = ImageRequest.Builder(LocalContext.current)
                .data(vehicleImage)
                .crossfade(true)
                .build().also {
                    Log.d("__IMAGE", "Vehicle Image URI: ${imageUri}")
                },
            contentDescription = "Vehicle Image",
            modifier = Modifier.fillMaxHeight().border(width = BORDER_WIDTH, color = MaterialTheme.colorScheme.tertiary, shape = borderShape),
//            placeholder = painterResource(R.drawable.default_car_image_dark_gray),
//            error = painterResource(R.drawable.default_car_image_error),
            contentScale = ContentScale.Crop
        )

        // Plate Number
        LicensePlate(
            plateNumber = plateNumber,
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.BottomCenter)
                .wrapContentHeight()
//                        .fillMaxWidth()
//                        .height(26.dp)
        )
    }
}
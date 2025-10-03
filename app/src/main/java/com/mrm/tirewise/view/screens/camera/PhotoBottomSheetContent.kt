package com.mrm.tirewise.view.screens.camera

import android.graphics.Bitmap
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mrm.tirewise.view.theme.BORDER_WIDTH

//this is just shows the result after image is taken
// //for the user to choose to retake or confirm
@Composable
fun  PhotoBottomSheetContent(
    bitmap: Bitmap,
    modifier: Modifier = Modifier,
    onRetakePhoto: () -> Unit,
    onConfirmPhoto: () -> Unit
) {
    if (bitmap == null) {
        return Box(modifier = modifier) {
            Text(text = "No photo taken")
        }
    }
    Scaffold(
        modifier = modifier,
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedButton(
                    border =  BorderStroke(BORDER_WIDTH, MaterialTheme.colorScheme.onBackground),
                    colors =  ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onBackground
                    ),
                    modifier = Modifier.weight(1f),
                    onClick = { onRetakePhoto() }) {
                    Text("Retake")
                }
                OutlinedButton(
                    border =  BorderStroke(BORDER_WIDTH, MaterialTheme.colorScheme.onBackground),
                    colors =  ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onBackground
                    ),
                    modifier = Modifier.weight(1f),
                    onClick = onConfirmPhoto) {
                    Text("Continue")
                }

            }
        }
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = modifier
                .padding(it)
                .padding(16.dp)
        ) {
            Image(
                bitmap = bitmap.asImageBitmap(), contentDescription = "Photo taken",
                modifier.clip(MaterialTheme.shapes.small)
            )
        }
    }
}

@Preview
@Composable
fun PhotoBottomSheetContentPreview() {
    Box(modifier = Modifier.fillMaxSize()) {
        PhotoBottomSheetContent(
            bitmap = // Provide a sample bitmap
            Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888),
            onRetakePhoto = {},
            onConfirmPhoto = {}
        )
    }
}
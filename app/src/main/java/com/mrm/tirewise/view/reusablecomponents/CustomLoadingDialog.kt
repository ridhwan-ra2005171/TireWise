package com.mrm.tirewise.view.reusablecomponents

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Preview
@Composable
fun CustomLoadingDialogBar(onDismissRequest: () -> Unit = {}) {
    Dialog(onDismissRequest = onDismissRequest) {
        ElevatedCard() {
            Box(
                modifier = Modifier
                    .height(50.dp)
                    .width(200.dp),
//                verticalArrangement = Arrangement.SpaceBetween,
//                horizontalAlignment = Alignment.CenterHorizontally
            ) {
//                    Text(
//                        text = "Please wait...", fontWeight = FontWeight.Bold,
//                        modifier = Modifier.fillMaxHeight(0.45f).align(Alignment.Center)
//                    )
                LoadingBar(
                    Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth().fillMaxHeight()
//                            .fillMaxHeight(0.3f)
                )
            }
        }
    }
}

@Preview
@Composable
fun CustomLoadingDialogCircular(showDialog: Boolean = true, onDismissRequest: () -> Unit = {}) {
    if (showDialog) {
        Dialog(onDismissRequest = onDismissRequest) {
//            ElevatedCard(
////                colors = ButtonDefaults.buttonColors(
////                    containerColor = Color.Transparent
////                ),
//            ) {
                Box(
                    modifier = Modifier
                        .size(100.dp),
//                verticalArrangement = Arrangement.SpaceBetween,
//                horizontalAlignment = Alignment.CenterHorizontally
                ) {
//                    Text(
//                        text = "Please wait...", fontWeight = FontWeight.Bold,
//                        modifier = Modifier.fillMaxHeight(0.45f).align(Alignment.Center)
//                    )
                    LoadingCircle(
                        Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxSize()
//                            .fillMaxHeight(0.3f)
                    )
                }
//            }
        }
    }
}
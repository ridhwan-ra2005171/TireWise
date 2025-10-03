package com.mrm.tirewise.view.reusablecomponents

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mrm.tirewise.R
import com.mrm.tirewise.view.theme.BORDER_WIDTH
import com.mrm.tirewise.view.theme.LARGE_ICON_SIZE
import com.mrm.tirewise.view.theme.SCREEN_PADDING
import com.mrm.tirewise.view.theme.TireWiseTheme

@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NetworkConnectivityScreen(message: String = "", showCameraButton: Boolean = false, onCameraClick: () -> Unit = {}) {
//    val color = Color.LightGray
    TireWiseTheme {
        Scaffold(
            bottomBar = {

                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 25.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "You can check your tire's condition here", textAlign = TextAlign.Center)
                    Icon(imageVector = Icons.Rounded.KeyboardArrowDown, contentDescription = "")
                    FloatingActionButton(
                        contentColor = MaterialTheme.colorScheme.onBackground,
                        containerColor = MaterialTheme.colorScheme.primary,
                        shape = MaterialTheme.shapes.extraLarge,
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .size(74.dp)
                            .border(
                                BORDER_WIDTH,
                                MaterialTheme.colorScheme.onBackground,
                                MaterialTheme.shapes.extraLarge
                            ),
                        onClick = onCameraClick) {
                        Icon(modifier = Modifier.size(48.dp),
                            tint = Color.Black,
                            painter = painterResource(id = R.drawable.ic_camera),
                            contentDescription = "")
                    }
                }
            }
        ) {
            Column(
                modifier = Modifier
                    .padding(it)
                    .padding(SCREEN_PADDING)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(painter = painterResource(id = R.drawable.ic_wifi_off), contentDescription ="",
                    modifier = Modifier.size(LARGE_ICON_SIZE)
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = "No Internet Connection",
                    fontSize = MaterialTheme.typography.headlineSmall.fontSize)
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = "Please connect to the internet to $message", textAlign = TextAlign.Center)
            }
        }
    }
}

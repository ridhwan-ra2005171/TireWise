package com.test.tirewise.view.screens.signIn

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.MarqueeSpacing
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.mrm.tirewise.R
import com.mrm.tirewise.networkConnectivity.ConnectivityObserver
import com.mrm.tirewise.view.reusablecomponents.GoogleButton
import com.mrm.tirewise.view.reusablecomponents.NetworkConnectivityDialog
import com.mrm.tirewise.view.reusablecomponents.TypewriterText
import com.mrm.tirewise.view.theme.BORDER_WIDTH

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WelcomeScreen(
    onSignInClick: () -> Unit,
    onGuestClick: () -> Unit, /*onTempClick: () -> Unit = {}*/
    networkState: State<ConnectivityObserver.Status>, ) {

    var showNetworkDialog by remember { mutableStateOf(false) }
    if (showNetworkDialog) {
        NetworkConnectivityDialog { showNetworkDialog = false }
    }

    Scaffold {
        Box(modifier = Modifier.fillMaxSize()
        ) {
            Column(modifier = Modifier
                .padding(it)
                .fillMaxWidth()
                .align(Alignment.Center)
                .testTag("WelcomeScreen"),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.tire_wise_logo),
                    contentDescription = "Tire Wise Logo",
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.extraLarge)
                        .size(150.dp)
                        .border(BORDER_WIDTH, MaterialTheme.colorScheme.primary, MaterialTheme.shapes.extraLarge))

                // Add the title "Welcome back to TireWise!
                TypewriterText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp),
                    texts = listOf("Hello!","Welcome back to TireWise!"))
        //            Text(
        //                textAlign = TextAlign.Center,
        //                text = "Welcome back to TireWise!",
        //                style = TextStyle(fontSize = MaterialTheme.typography.titleLarge.fontSize, fontWeight = FontWeight.Bold),
        //                modifier = Modifier
        //                    .fillMaxWidth()
        //                    .padding(horizontal = 10.dp)
        //            )

                // Add the description "Log in to get started!
                Text(
                    textAlign = TextAlign.Center,
                    text = "Login to get started!",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp))

                // Google sign in button
                GoogleButton(text = "Continue with Google", onClick = {
                    if (networkState.value == ConnectivityObserver.Status.Available) {
                        onSignInClick()
                    } else {
                        showNetworkDialog = true
                    }

                })

                // add an underlined text button
                TextButton(onClick = onGuestClick ) {
                    Text(color = MaterialTheme.colorScheme.tertiary,text = "or continue as a guest", style = TextStyle(textDecoration = TextDecoration.Underline))
                }
            }

            Row(modifier = Modifier
                .align(Alignment.BottomCenter)
                .basicMarquee(
                    iterations = Int.MAX_VALUE,
                    delayMillis = 0,
                    velocity = 70.dp,
                    spacing = MarqueeSpacing(0.dp),
                    initialDelayMillis = 0)
            ) {
                repeat(5) {
                    Image(
                        painter = painterResource(id = R.drawable.tire_marks_single_yellow),
                        contentDescription = null,
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier
                            .scale(scaleX = 1f, scaleY = 1f)
                            .width(65.dp)
                            .height(80.dp)
                    )
                    Spacer(modifier = Modifier.width(20.dp))
                }
            }
        }
    }
}
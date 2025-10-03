package com.mrm.tirewise.view.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.mrm.tirewise.R
import com.mrm.tirewise.view.theme.TireWiseTheme

@Preview
@Composable
fun SplashScreen(checkAuthState: () -> Unit = {}) {

    LaunchedEffect(Unit) {
        checkAuthState()
    }

    TireWiseTheme {
        Column(
            verticalArrangement = Arrangement.Center, modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background)
        ) {
            LoaderAnimation(
                modifier = Modifier.size(500.dp),
                anim = R.raw.mono_traffic_light
            )
        }
    }
}

@Composable
fun LoaderAnimation(modifier: Modifier = Modifier, anim : Int) {
    val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(anim))
    LottieAnimation(
        composition = composition, iterations = LottieConstants.IterateForever,
        modifier = modifier
    )
}
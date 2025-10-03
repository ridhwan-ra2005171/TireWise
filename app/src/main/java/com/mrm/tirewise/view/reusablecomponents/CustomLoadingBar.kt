package com.mrm.tirewise.view.reusablecomponents

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mrm.tirewise.view.theme.TireWiseTheme

@Preview
@Composable
fun LoadingBar(modifier: Modifier = Modifier) {
    TireWiseTheme {
        LinearProgressIndicator(
            modifier = Modifier
                .then(modifier),
            color = MaterialTheme.colorScheme.primary,
            trackColor = Color.Black,
        )
    }
}

@Preview
@Composable
fun LoadingCircle(modifier: Modifier = Modifier) {
    TireWiseTheme {
        CircularProgressIndicator(
            strokeCap = androidx.compose.ui.graphics.StrokeCap.Square,
            strokeWidth = 10.dp,

            modifier = Modifier
                .then(modifier),
            color = MaterialTheme.colorScheme.primary,
            trackColor = Color.Black,
        )
    }
}

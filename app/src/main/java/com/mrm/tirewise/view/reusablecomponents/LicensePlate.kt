package com.mrm.tirewise.view.reusablecomponents

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.mrm.tirewise.view.theme.BORDER_WIDTH

@Composable
fun LicensePlate(plateNumber: String,
                 modifier: Modifier = Modifier,
                 borderColor: Color = Color.Black,
                 fontSize : TextUnit = MaterialTheme.typography.titleMedium.fontSize) {
    OutlinedCard(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke( BORDER_WIDTH - 0.5.dp, color = borderColor),
        modifier = modifier,
        shape = MaterialTheme.shapes.small
    ) {
        Row(horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(3.dp),
            ) {
            Text(text = plateNumber,
                color = Color.Black,
                style = TextStyle(fontSize = fontSize, fontWeight = FontWeight.Black),
                textAlign = TextAlign.Center,
                maxLines = 1
            )
        }
    }
}
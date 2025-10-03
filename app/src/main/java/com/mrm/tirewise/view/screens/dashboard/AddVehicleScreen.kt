package com.mrm.tirewise.view.screens.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mrm.tirewise.view.theme.BORDER_WIDTH
import com.mrm.tirewise.view.theme.TireWiseTheme

@Composable
fun AddVehicleScreen(isVehicleListEmpty: Boolean,onAddVehicle: () -> Unit) {
    Column(modifier = Modifier
        .fillMaxSize()
        .background(color = MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        IconButton(
            modifier = Modifier
                .testTag("addVehicle")
                .size(110.dp)
                .shadow(elevation = 8.dp, shape = CircleShape)
                .background(MaterialTheme.colorScheme.primary, CircleShape),
            onClick = onAddVehicle) {
            Icon(
                imageVector =  Icons.Rounded.AddCircle,
                tint =  MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.fillMaxSize().border(BORDER_WIDTH, MaterialTheme.colorScheme.tertiary, CircleShape),
                contentDescription = "Add a new vehicle")
        }
        Spacer(modifier = Modifier.height(15.dp))
        Text(text =
         if (isVehicleListEmpty) "Add your first vehicle to get started" else "Add a new vehicle",
            fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.titleMedium.fontSize)
    }
}

@Preview
@Composable
fun AddVehicleScreenPreview() {
    TireWiseTheme {
        AddVehicleScreen(false, onAddVehicle = {})
    }
}
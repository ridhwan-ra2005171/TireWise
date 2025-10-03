package com.mrm.tirewise.view.reusablecomponents

import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import com.mrm.tirewise.view.theme.BORDER_WIDTH

@Preview
@Composable
fun DeleteVehicleDialog(onDeleteClicked: (Boolean) -> Unit = {}, onDeleteVehicle: () -> Unit = {}) {
    val context = LocalContext.current
    AlertDialog(
        title = {
            Text(text = "Delete Vehicle") },
        text = { Text(text = "Are you sure you want to delete this vehicle?") },
        modifier = Modifier.border(
            BORDER_WIDTH, MaterialTheme.colorScheme.onBackground,
            MaterialTheme.shapes.extraLarge),
        dismissButton = {
            TextButton(
                onClick = { onDeleteClicked(false)  }
            ) {
                Text(text = "Cancel",
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onDeleteClicked(false)
                onDeleteVehicle()
                Toast.makeText(context, "Vehicle Deleted Successfully", Toast.LENGTH_LONG).show()
            }
            ) {
                Text(text = "Delete",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.testTag("confirmDelete")
                )
            }
        },
        onDismissRequest = { onDeleteClicked(false) },
        )
}
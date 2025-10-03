package com.mrm.tirewise.view.reusablecomponents

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.text.font.FontWeight
import androidx.core.content.ContextCompat.startActivity
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.PermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraPermissionDialog(showRationalDialog: MutableState<Boolean>, permission: PermissionState, context: android.content.Context) {
    AlertDialog(
        onDismissRequest = {
            showRationalDialog.value = false
        },
        title = {
            Text(
                text = "Permission",
                fontWeight = FontWeight.Bold,
                fontSize = MaterialTheme.typography.titleSmall.fontSize
            )
        },
        text = {
            Text(
               "The app needs to access your camera. Please grant the permission.",
                fontSize = MaterialTheme.typography.bodyMedium.fontSize
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    showRationalDialog.value = false
                    val intent = Intent(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.fromParts("package", context.packageName, null)
                    )
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(context, intent, null)

                }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    showRationalDialog.value = false
                }) {
                Text("Cancel")
            }
        },
    )
}


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MultiplePermissionDialog(showRationalDialog: MutableState<Boolean>, multiplePermission: MultiplePermissionsState, context: android.content.Context) {
    AlertDialog(
        onDismissRequest = {
            showRationalDialog.value = false
        },
        title = {
            Text(
                text = "Permission",
                fontWeight = FontWeight.Bold,
                fontSize = MaterialTheme.typography.titleSmall.fontSize
            )
        },
        text = {
            Text(
                text = if (multiplePermission.revokedPermissions.size == 2) {
                    "The app needs to access your camera and photo album. Please grant the permission."
                } else if (multiplePermission.revokedPermissions.first().permission == Manifest.permission.CAMERA) {
                    "The app needs to access your camera. Please grant the permission."
                } else {
                    "The app needs to access your photo album. Please grant the permission."
                },
                fontSize = MaterialTheme.typography.bodyMedium.fontSize
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    showRationalDialog.value = false
                    val intent = Intent(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.fromParts("package", context.packageName, null)
                    )
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(context, intent, null)

                }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    showRationalDialog.value = false
                }) {
                Text("Cancel")
            }
        },
    )
}

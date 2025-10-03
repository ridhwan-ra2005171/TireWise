package com.mrm.tirewise.view.reusablecomponents

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mrm.tirewise.R
import com.mrm.tirewise.view.theme.BORDER_WIDTH
import com.mrm.tirewise.view.theme.LARGE_ICON_SIZE
import com.mrm.tirewise.view.theme.TireWiseTheme


@Composable
fun NetworkConnectivityDialog(onDismissRequest: () -> Unit) {
    TireWiseTheme {
        AlertDialog(
            modifier = Modifier.border(BORDER_WIDTH, MaterialTheme.colorScheme.onBackground, MaterialTheme.shapes.extraLarge,),
            title = {
                Column( horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()) {
                    Icon(painter = painterResource(id = R.drawable.ic_wifi_off),
                        contentDescription = "",
                        modifier = Modifier.size(LARGE_ICON_SIZE))
                    Text(text = "No Internet Connection", textAlign = TextAlign.Center)
                }
            },
            text = { Text(text ="Please connect to the internet to use this feature."
//                        +"\nYou can use the tire classification feature without an internet connection."
            ) },
            onDismissRequest = onDismissRequest,
            confirmButton = {},
            dismissButton = {
                TextButton(
                    onClick = { onDismissRequest() }
                ) {
                    Text(text = "Cancel",
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }                },
        )
    }
}
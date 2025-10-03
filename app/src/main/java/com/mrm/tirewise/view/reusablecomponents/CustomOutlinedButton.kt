package com.mrm.tirewise.view.reusablecomponents

import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CustomOutlinedButton(onClick: () -> Unit, backgroundColor: Color = MaterialTheme.colorScheme.secondaryContainer, modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = if (backgroundColor == MaterialTheme.colorScheme.primary) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.tertiary,
            containerColor = backgroundColor
        ),
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.tertiary),
        shape = MaterialTheme.shapes.large,)
    {
        content()
    }
}
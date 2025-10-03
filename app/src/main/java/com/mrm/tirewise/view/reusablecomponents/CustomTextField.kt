package com.mrm.tirewise.view.reusablecomponents

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp


@Composable
fun CustomTextField(label: String, placeholder: String = "", value: String, onValueChange: (String) -> Unit, modifier: Modifier = Modifier, minLines: Int = 1, maxLines: Int = 1, singleLine : Boolean = true,
                    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
                    outlineColor: Color = MaterialTheme.colorScheme.onTertiary) {


    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(1.dp)
            .minimumInteractiveComponentSize()
            .then(modifier),
        shape = MaterialTheme.shapes.large,
        colors = TextFieldDefaults.colors(
            unfocusedTextColor = MaterialTheme.colorScheme.tertiary,
            unfocusedIndicatorColor = outlineColor, // Dont uncomment
            unfocusedContainerColor = MaterialTheme.colorScheme.background,
//            unfocusedLabelColor = MaterialTheme.colorScheme.tertiary, // Don't uncomment
            focusedLabelColor = MaterialTheme.colorScheme.tertiary,
            focusedIndicatorColor = MaterialTheme.colorScheme.primary,
            focusedContainerColor = MaterialTheme.colorScheme.background,
            cursorColor = MaterialTheme.colorScheme.tertiary

        ),
        placeholder =  { if (placeholder.isNotBlank()) Text(text = placeholder) },
        maxLines = if(minLines > maxLines) minLines else maxLines,
        minLines = minLines,
        singleLine = singleLine,
        label = { Text(text = label, fontWeight = FontWeight.Medium) },
        value = value,
        onValueChange = onValueChange,
        keyboardOptions = keyboardOptions,
    )
}
package com.mrm.tirewise.view.reusablecomponents

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun SortTires(selectedOption: String, onOptionSelected: (String) -> Unit) {

    var sortByOptions = listOf("Date: Ascending", "Date: Descending", "Condition: Good First", "Condition: Bad First")

    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
//            .background(MaterialTheme.colorScheme.primary)
    ) {
        Row(
            modifier = Modifier.height(40.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Menu, //change this icon to a sort icon
//                tint = MaterialTheme.colorScheme.onPrimary,
                contentDescription = "Sort By",
                modifier = Modifier
                    .clickable { expanded = !expanded }
                    .padding(10.dp)
            )
            Text(text = selectedOption)
        }

        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }, modifier = Modifier.fillMaxWidth()) {
            sortByOptions.forEach {
                DropdownMenuItem(
                    onClick = {
                        expanded = false
                        onOptionSelected(it)
                    },
                    text = { Text(text = it) }
                )
            }
        }
    }


}


@Preview
@Composable
fun SortTiresPreview() {
    var selectedOption by remember {
        mutableStateOf("All")
    }

    SortTires(selectedOption = selectedOption) { option ->
        selectedOption = option}

}
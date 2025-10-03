package com.mrm.tirewise.view.screens.tireList

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mrm.tirewise.view.screens.dashboard.DashboardDivider
import com.mrm.tirewise.view.theme.BORDER_WIDTH

enum class SortBy {
    DateAscending,
    DateDescending,
    ConditionGoodFirst,
    ConditionBadFirst
}

@Composable
fun SortTiresMenu(selectedOption: SortBy, onOptionSelected: (SortBy) -> Unit) {

    var sortByOptions = mapOf<String,SortBy>(
        "Date: Newest to Oldest (Default)" to SortBy.DateAscending,
        "Date: Oldest to Newest" to SortBy.DateDescending,
        "Condition: Good First" to SortBy.ConditionGoodFirst,
        "Condition: Bad First" to SortBy.ConditionBadFirst
    )

    val bottomRoundedCorner = RoundedCornerShape(bottomEnd = 20.dp, bottomStart = 20.dp)

    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
//            .background(MaterialTheme.colorScheme.primary)
    ) {
        Row(
            modifier = Modifier
                .shadow(1.dp)
                .background(MaterialTheme.colorScheme.background)
                .border(
                    BORDER_WIDTH,
                    MaterialTheme.colorScheme.onBackground
                )
                .clickable { expanded = !expanded }
                .height(48.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                imageVector =
                if (expanded) Icons.Rounded.KeyboardArrowDown else Icons.Rounded.KeyboardArrowUp, //change this icon to a sort icon
//                tint = MaterialTheme.colorScheme.onPrimary,
                contentDescription = "",
                modifier = Modifier
                    .size(32.dp)
//                    .padding(10.dp)
            )
            Row {
                Text(text = "Sort by",
                    fontSize = MaterialTheme.typography.titleMedium.fontSize,
                    fontWeight = FontWeight.Bold)
                Text(text =" ${sortByOptions.keys.find { sortByOptions[it] == selectedOption }}",
                    fontSize = MaterialTheme.typography.titleMedium.fontSize,
                )
            }
        }

        DropdownMenu(expanded = expanded,
            onDismissRequest = { expanded = false },

            modifier = Modifier
//                .animateContentSize()
                .fillMaxWidth(0.7f)
                .border(BORDER_WIDTH,MaterialTheme.colorScheme.onBackground,)
                .background(MaterialTheme.colorScheme.background,)
        ) {
            sortByOptions.map { it.key }.subList(0,2).forEach { option : String  ->
                DropdownMenuItem(
                    modifier = Modifier.background(MaterialTheme.colorScheme.background),
                    colors = MenuDefaults.itemColors(
                        textColor = MaterialTheme.colorScheme.onBackground,
                    ),
                    onClick = {
                        expanded = false
                        onOptionSelected(sortByOptions[option] ?: SortBy.DateAscending)
                    },
                    text = { Text(text = option) }
                )
            }
            DashboardDivider(
                modifier = Modifier.padding(horizontal = 5.dp)
            )
            sortByOptions.map { it.key }.subList(2, sortByOptions.size).forEach { option : String  ->
                DropdownMenuItem(
                    modifier = Modifier.background(MaterialTheme.colorScheme.background),
                    colors = MenuDefaults.itemColors(
                        textColor = MaterialTheme.colorScheme.onBackground,
                    ),
                    onClick = {
                        expanded = false
                        onOptionSelected(
                            sortByOptions[option] ?: SortBy.DateAscending
                        )
                    },
                    text = { Text(text = option) }
                )
            }
        }
    }
}

@Preview
@Composable
fun SortTiresMenuPreview() {
    SortTiresMenu(selectedOption = SortBy.DateDescending, onOptionSelected = {})
}
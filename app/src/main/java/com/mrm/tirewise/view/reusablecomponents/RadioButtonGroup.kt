package com.mrm.tirewise.view.reusablecomponents

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun RadioButtonGroup(
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    Column {
        options.forEach { option ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = option == selectedOption,
                    onClick = { onOptionSelected(option) },
                    modifier = Modifier
                        .size(24.dp)
                        .padding(4.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = option, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
fun RadioButtonGroup2(listTitle : String, options: List<String>, selectedOption: String,  onOptionSelected: (String) -> Unit) {

//    val context = LocalContext.current
//    var animateBackgroundColor by remember { mutableStateOf(false) }
//    val animatedColor by animateColorAsState(
//        if (animateBackgroundColor) Color.Transparent else MaterialTheme.colorScheme.primary,
//        label = "color"
//    )
//    val animatedButtonColor = animateColorAsState(
//        targetValue = if (animateBackgroundColor) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.background,
//        animationSpec = tween(350, 0)
//    )
    // Calculate height of the grid
    val gridHeight = (options.size / 2) * 48 + (options.size % 2 -0.7) * 48
    LazyVerticalGrid(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
//            .background(MaterialTheme.colorScheme.primaryContainer)
            .height(gridHeight.dp),
        columns = GridCells.Fixed(2),
        content = {
            items(options.size) { index ->
                Row(modifier = Modifier
                    .background(shape = MaterialTheme.shapes.large, color = Color.Transparent)
                    .clickable { onOptionSelected(options[index]) }
//                        .then(
//                            if ( options[index] == selectedOption) {
//                                animateBackgroundColor = true
//                                CoroutineScope.{}
//                                delay(200)
//                                 Modifier
////                                     .drawBehind {
////                                     drawRoundRect( color = animatedColor, cornerRadius = CornerRadius(25f, 25f))
////                                 }
//                                     .background(animatedButtonColor.value, MaterialTheme.shapes.large)
//                                         also {
//
////                                             animateBackgroundColor = false
//                                         }
//                            } else {
//                                animateBackgroundColor = false
//                                Modifier
//                            }
//                        )
                    .padding(vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = options[index] == selectedOption,
                        onClick = { onOptionSelected(options[index]) },
                        modifier = Modifier
                            .size(24.dp)
                            .padding(end = 4.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = options[index], fontWeight = FontWeight.Medium,
                        fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                        maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
            }
        }
    )
}

@Preview
@Composable
fun RadioButtonGroupPreview() {
    var selectedOption by remember { mutableStateOf("Custom") }

    val listy = listOf(
        "Tire Checkup",
        "Tire Replacement",
        "Tire Alignment",
        "Tire Rotation",
        "Oil Change",
        "Insurance Renewal",
        "Custom"
    )
    RadioButtonGroup2("Group of Radios", options = listy, selectedOption = selectedOption, onOptionSelected = {selectedOption = it})

}
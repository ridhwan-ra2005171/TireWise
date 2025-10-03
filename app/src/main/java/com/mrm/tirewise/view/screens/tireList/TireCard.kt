package com.mrm.tirewise.view.screens.tireList

import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.mrm.tirewise.model.ClassificationResult
import com.mrm.tirewise.model.TireScan
import com.mrm.tirewise.utils.toReadableDate
import com.mrm.tirewise.view.reusablecomponents.LoadingBar
import com.mrm.tirewise.view.reusablecomponents.TypewriterText
import com.mrm.tirewise.view.theme.BORDER_WIDTH

@Composable
fun TireList() {
    Column(
        modifier = Modifier.padding(4.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
//        CarInfoCard()
//        TireCard()
//        TireCard()
        Card{
            TypewriterText(
                texts = listOf(
                    "Hello!",
                "Welcome back to TireWise!"
                )
            )
        }
    }
}


@Composable
fun TireCard(
    tireScan: TireScan,
    isSelected: Boolean = false,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {

    // Set the result colors
    var condColor = ClassificationResult().getResult(tireScan.tireCondition).color

    // Convert the date from milliseconds (Long) to a readable format
    val tireScanDate = tireScan.date.toReadableDate()

    OutlinedCard(
//    OutlinedButton(
        border = BorderStroke( if (isSelected) BORDER_WIDTH+2.dp else BORDER_WIDTH,
            if (isSelected)
                MaterialTheme.colorScheme.primary
            else
                MaterialTheme.colorScheme.onBackground),
        shape =  MaterialTheme.shapes.large,
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(150.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize(),
            horizontalArrangement = Arrangement.Center
        ) {
            // Tire Image
            SubcomposeAsyncImage(
                modifier = Modifier
                    .border(BorderStroke(BORDER_WIDTH, condColor))
                    .aspectRatio(1f)
                    .fillMaxHeight(),
                contentScale = ContentScale.Crop,
                model =  tireScan.tireImageUri,  // Use the provided tire image resource ID
                contentDescription = "Tire Image",
                loading = { LoadingBar(Modifier.fillMaxSize())}
            )

            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.Start
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = tireScan.tireCondition + " Condition",  // Use the provided tire condition text
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = MaterialTheme.typography.titleLarge.fontSize,
                            color = condColor
                        )
                    )
                    Text(
                        text = tireScan.tireConditionDesc,  // Use the provided tire condition description text
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = TextStyle(
//                        fontSize = MaterialTheme.typography.titleSmall.fontSize
                            color = condColor
                        )
                    )
                    Text(
                        text = tireScan.tirePosition,  // Use the provided tire position text
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
//                        fontSize = MaterialTheme.typography.labelMedium.fontSize
                        )
                    )
                    if (!tireScan.additionalNotes.isNullOrEmpty()) {
                        Text(
                            text = buildAnnotatedString {
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Medium)) {
                                    append("Comments: ")
                                }
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Normal)) {
                                    append(tireScan.additionalNotes)
                                }
                            },
                            maxLines = 2,  // Set maxLines to 1
                            overflow = TextOverflow.Ellipsis,  // Truncate text with ellipsis if it overflows
                            style = TextStyle(
                                fontSize = MaterialTheme.typography.bodyMedium.fontSize
                            ),
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                    }
                }


                Text(
                    text = tireScanDate,  // Use the provided date text
                    style = TextStyle(
                        fontWeight = FontWeight.Medium,
                        fontSize = MaterialTheme.typography.bodyMedium.fontSize
                    )
                )
            }
        }
    }
}


@Preview
@Composable
fun TireCardPreview() {
//    TireCard()
    //call tirecard()
    // Replace these sample values with your actual data
    val tireImageRes = Uri.EMPTY.toString()
    val tireCondition = "Good"
    val tireConditionDesc = "No tire replacement required"
    val tirePosition = "Front Left"
    val comments = "No visible issues. Tread depth is good. Perfect Perfect Perfect Preferred Perfect Perfect"
    val date = System.currentTimeMillis()

    TireCard(
        tireScan = TireScan(
            vehicleId = "",
            tireImageUri = tireImageRes,
            tireCondition = tireCondition,
            tireConditionDesc = tireConditionDesc,
            tirePosition = tirePosition,
            additionalNotes = comments,
            date = date
        )
    ) {}
}

//@Preview
//@Composable
//fun TireListPreview() {
//    TireList()
//}
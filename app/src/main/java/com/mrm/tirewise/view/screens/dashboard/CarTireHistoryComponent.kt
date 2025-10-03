package com.mrm.tirewise.view.screens.dashboard

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.maxkeppeker.sheets.core.utils.testTags
import com.mrm.tirewise.R
import com.mrm.tirewise.view.theme.TireWiseTheme

@Preview
@Composable
fun FourWheelVehicleTireHistoryPreview() {
    TireWiseTheme {
        FourWheelVehicleTireHistory({},{},{},{}) {}
    }
}

@Composable
fun FourWheelVehicleTireHistory(
    onViewFrontRightTireHistory: () -> Unit,
    onViewBackRightTireHistory: () -> Unit,
    onViewFrontLeftTireHistory: () -> Unit,
    onViewBackLeftTireHistory: () -> Unit,
    onViewAllTireHistory: () -> Unit
) {
    val spacedByVal = 5.dp
    Column(modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.spacedBy(spacedByVal),
        ) {
        //// Top Part of the Tire History
        Box(modifier = Modifier
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(spacedByVal),
                modifier = Modifier.align(Alignment.Center)
            ) {

                TireHistoryButtonDuo(
                    modifier = Modifier,
                    leftTextId = R.string.front_right_tire_history,
                    rightTextId = R.string.back_right_tire_history,
                    leftOnClick = onViewFrontRightTireHistory,
                    rightOnClick = onViewBackRightTireHistory)

                TireHistoryButtonDuo(
                    modifier = Modifier,
                    leftTextId = R.string.front_left_tire_history,
                    rightTextId = R.string.back_left_tire_history,
                    leftOnClick = onViewFrontLeftTireHistory,
                    rightOnClick = onViewBackLeftTireHistory)

            }
            // Car Picture
            Image(
                modifier = Modifier.align(Alignment.Center),
                painter = painterResource(id = R.drawable.car_white_2),
                contentDescription = null)
        }
        //// View All Previous Tire Scans
        TirePositionButton(
            modifier = Modifier.testTag("view_all_tire_history_button"),
            textId = R.string.view_all_tire_history,
            onClick = onViewAllTireHistory,
        )

    }
}


@Composable
fun TireHistoryButtonDuo( modifier: Modifier = Modifier,
                           leftTextId: Int, rightTextId: Int,
                           leftOnClick: () -> Unit, rightOnClick: () -> Unit,
                       ) {
    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        TirePositionButton(
            textId = leftTextId,
            textAlign = TextAlign.Start,
            modifier = modifier.weight(1f).testTags("left_tire_button"),
            onClick = leftOnClick)
        TirePositionButton(
            textId = rightTextId,
            textAlign = TextAlign.End,
            modifier = modifier.weight(1f).testTags("right_tire_button"),
            onClick = rightOnClick)
    }
}


@Composable
fun TirePositionButton(
    textId: Int,
    backgroundColor: Color = MaterialTheme.colorScheme.secondaryContainer,
    onClick: () -> Unit, modifier: Modifier = Modifier, textAlign: TextAlign = TextAlign.Center
) {
    val shape = MaterialTheme.shapes.large
    OutlinedButton(
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.tertiary),
        onClick = onClick,
        shape = shape,
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = MaterialTheme.colorScheme.tertiary,
            containerColor = backgroundColor,
        ),
        contentPadding = PaddingValues(vertical = 10.dp, horizontal = 15.dp),
//        horizontalArrangement = horizontalArrangement,
//        verticalAlignment = verticalAlignment,
        modifier = modifier
            .fillMaxWidth()
//            .background(color = MaterialTheme.colorScheme.onPrimary, shape = shape)
//            .clickable {
//                /*TODO: view Tire History */ +textId
//                onClick
//            }
//            .padding(10.dp)
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            textAlign =  textAlign,
            color = MaterialTheme.colorScheme.tertiary,
            fontWeight = FontWeight.Bold,
            text = stringResource(id = textId))
    }
}
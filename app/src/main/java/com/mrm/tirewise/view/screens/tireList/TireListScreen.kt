package com.mrm.tirewise.view.screens.tireList

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mrm.tirewise.model.TirePosition
import com.mrm.tirewise.model.TireScan
import com.mrm.tirewise.networkConnectivity.ConnectivityObserver
import com.mrm.tirewise.view.reusablecomponents.NetworkConnectivityScreen
import com.mrm.tirewise.view.theme.SCREEN_PADDING

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TireListScreen(
    tires: List<TireScan>,
    networkState: State<ConnectivityObserver.Status>,
    currentTirePosition: TirePosition,
    onNavigateUp: () -> Unit,
    onTireCardClicked: (TireScan) -> Unit,
    onSortByClicked: (SortBy) -> Unit,
) {
    var selectedOption by remember {
        mutableStateOf(SortBy.DateAscending)
    }

    LaunchedEffect(key1 = Unit) {
        // initialize the tire scans
        onSortByClicked(selectedOption)
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text =
                if (currentTirePosition == TirePosition.ALL)
                    "All Tire Scans"
                else
                    "${currentTirePosition.getPositionAsString()} Tires",
                    fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    FilledIconButton(
                        onClick = onNavigateUp,
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = MaterialTheme.colorScheme.background,
                            contentColor = MaterialTheme.colorScheme.onBackground)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = null
                        )
                    }
                }

            )
        }
    ) { padVal ->

        // Show network connectivity screen if network is unavailable
        if (networkState.value == ConnectivityObserver.Status.Unavailable) {
            NetworkConnectivityScreen()
            return@Scaffold
        } else if (tires.isEmpty()) {
            // Show a message if no scans are found
            Text(
                text = "No Scans Found",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 64.dp)
                    .padding(padVal))
            return@Scaffold
        }

        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .padding(padVal)
                .background(MaterialTheme.colorScheme.background),
            topBar = {
                // Your search bar and sort dropdown can be added here if needed
                SortTiresMenu(selectedOption = selectedOption, onOptionSelected = { selectedOption = it;
                    val sortBy = selectedOption
                    Log.d( "__SORT", sortBy.toString())
                    onSortByClicked(it) })
//                SortTires(
//                    selectedOption = selectedOption,
//                    onOptionSelected = { selectedOption = it })
            }
        ) {

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(it)
                    .background(MaterialTheme.colorScheme.background),
                verticalArrangement = Arrangement.spacedBy(SCREEN_PADDING)
            ) {
                item { Spacer(modifier = Modifier.height(8.dp)) }
                items(tires.size) { index ->
                    val currentTire = tires[index]
                    TireCard(tireScan = currentTire, modifier = Modifier.padding(horizontal = SCREEN_PADDING), onClick = {onTireCardClicked(currentTire)})
                }
            }
        }
    }
}


@Preview
@Composable
fun TireListScreenPreview() {
    val date = System.currentTimeMillis()
    val emptyUriString = ""
    val tireList = listOf(
        TireScan(
            vehicleId = "",
            tireId = "1",
            tireImageUri = "",
            tireCondition = "Good",
            tireConditionDesc = "Well-maintained tire",
            tirePosition = "Front Left",
            additionalNotes = "No visible issues. Tread depth is good.",
            date = date + 100
        ),
        TireScan(
            vehicleId = "",
            tireId = "2",
            tireImageUri = "",
            tireCondition = "Fair",
            tireConditionDesc = "Slightly worn tire",
            tirePosition = "Rear Right",
            additionalNotes = "Minor wear and tear. Tread depth is fair.",
            date = date + 1000
        ),
        TireScan(
            vehicleId = "",
            tireId = "3",
            tireImageUri = "",
            tireCondition = "Excellent",
            tireConditionDesc = "Brand new tire",
            tirePosition = "Front Right",
            additionalNotes = "No issues. Brand new tire.",
            date = date + 101
        ),
        TireScan(
            vehicleId = "",
            tireId = "4",
            tireImageUri = "",
            tireCondition = "Poor",
            tireConditionDesc = "Signs of damage",
            tirePosition = "Rear Left",
            additionalNotes = "Visible damage. Tread depth is low.",
            date = date + 200
        ),
        TireScan(
            vehicleId = "",
            tireId = "5",
            tireImageUri = "",
            tireCondition = "Good",
            tireConditionDesc = "Well-maintained tire",
            tirePosition = "Spare",
            additionalNotes = "No visible issues. Tread depth is good.No visible issues. Tread depth is good",
            date = date + 4000
        )
    )

//    TireListScreen(tires = tireList)
}

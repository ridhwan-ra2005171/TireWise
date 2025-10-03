package com.mrm.tirewise.view.reusablecomponents

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.mrm.tirewise.R
import com.mrm.tirewise.model.TirePosition
import com.mrm.tirewise.view.screens.dashboard.TirePositionButton
import com.mrm.tirewise.view.theme.BORDER_WIDTH
import com.mrm.tirewise.view.theme.TireWiseTheme

@Composable
fun TirePositionDialog(onDismissRequest: () -> Unit, onSave: (TirePosition) -> Unit) {
    val context = LocalContext.current
    var chosenPosition: TirePosition? by remember {
        mutableStateOf(null)
    }
    TireWiseTheme {
        Dialog(onDismissRequest = onDismissRequest) {
            val border = BorderStroke(BORDER_WIDTH, MaterialTheme.colorScheme.onBackground)
            ElevatedCard(
                shape = MaterialTheme.shapes.large,
                modifier = Modifier
                    .border(border, shape=MaterialTheme.shapes.large)
            ) {
                Column(
                    modifier = Modifier
                        .height(300.dp)
                        .width(280.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.tertiaryContainer)
                            .border(border)
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(text = "Please select the tire position to continue", fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                    }


                    Box(modifier = Modifier
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(20.dp),
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(10.dp)
                        ) {
                            Row(horizontalArrangement = Arrangement.spacedBy(20.dp), modifier = Modifier.height(60.dp)) {
                                TirePositionButton(
                                    backgroundColor = if (chosenPosition == TirePosition.FRONT_LEFT) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondaryContainer,
                                    textId = R.string.front_left_tire_history,
                                    textAlign = TextAlign.Start,
                                    modifier = Modifier.weight(1f).height(60.dp),
                                    onClick = {chosenPosition = TirePosition.FRONT_LEFT})
                                TirePositionButton(
                                    backgroundColor = if (chosenPosition == TirePosition.FRONT_RIGHT) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondaryContainer,
                                    textId = R.string.front_right_tire_history,
                                    textAlign = TextAlign.End,
                                    modifier = Modifier.weight(1f).height(60.dp),
                                    onClick = { chosenPosition = TirePosition.FRONT_RIGHT})
                            }
                            Row(horizontalArrangement = Arrangement.spacedBy(20.dp), modifier = Modifier) {
                                TirePositionButton(
                                    backgroundColor = if (chosenPosition == TirePosition.BACK_LEFT) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondaryContainer,
                                    textId = R.string.back_left_tire_history,
                                    textAlign = TextAlign.Start,
                                    modifier = Modifier.weight(1f).height(60.dp),
                                    onClick = { chosenPosition = TirePosition.BACK_LEFT})
                                TirePositionButton(
                                    backgroundColor = if (chosenPosition == TirePosition.BACK_RIGHT) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondaryContainer,
                                    textId = R.string.back_right_tire_history,
                                    textAlign = TextAlign.End,
                                    modifier = Modifier.weight(1f).height(60.dp),
                                    onClick = { chosenPosition = TirePosition.BACK_RIGHT})
                            }
                        }
                        // Car Picture
                        Image(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .rotate(90f),
                            painter = painterResource(id = R.drawable.car_white_2),
                            contentDescription = null)
                    }

                    Button(
                        onClick = {
                                  if (chosenPosition == null) {
                                      Toast.makeText(context, "Please select a position", Toast.LENGTH_SHORT).show()
                                  } else {
                                      onSave(chosenPosition!!)
                                      onDismissRequest()
                                  }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.primary)
                            .border(border),
                    ) {
                        Text(text = "Save", color = Color.Black, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                    }


                }
            }
        }
    }
}

@Preview
@Composable
fun TirePositionDialogPreview() {
    Column(modifier = Modifier.fillMaxSize()) {
        TirePositionDialog({},{})
    }
}
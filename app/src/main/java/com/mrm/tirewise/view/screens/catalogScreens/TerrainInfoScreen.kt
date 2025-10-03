package com.mrm.tirewise.view.screens.catalogScreens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mrm.tirewise.view.theme.BORDER_WIDTH
import com.mrm.tirewise.view.theme.SCREEN_PADDING
import com.mrm.tirewise.view.theme.TireWiseTheme

@Composable
fun TerrainInfoScreen(terrainType: TerrainType, onClose : () -> Unit ) {

    val tireTypes = terrainType.tireTypes

    var expand by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = Unit) {
        expand = true
    }


    Scaffold(
        bottomBar = {
            Row(modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                .height(70.dp)
                .padding(SCREEN_PADDING)
                .background(Color.Transparent), horizontalArrangement = Arrangement.Center) {
                OutlinedButton(
                    modifier = Modifier,
                    colors = ButtonDefaults.buttonColors(
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        containerColor = MaterialTheme.colorScheme.primary),
                    border = BorderStroke( BORDER_WIDTH, MaterialTheme.colorScheme.onBackground),
                    shape = MaterialTheme.shapes.large,
                    onClick = onClose
                ) {
                    Row(horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "Close  ", fontWeight = FontWeight.Bold)
                        Icon(
                            imageVector = Icons.Rounded.Close,
                            contentDescription = null
                        )
                    }
                }
            }
        },
        topBar = {
            Box(modifier = Modifier.height(100.dp)) {
                AnimatedVisibility(visible = expand, enter =
                slideInVertically(
                    initialOffsetY = { -it },
                    animationSpec = tween(200)
                )
                ) {
                    Box(modifier = Modifier.height(100.dp)) {
                        
                        OutlinedCard(
                            shape = RoundedCornerShape(topEnd = 0.dp, topStart = 1.dp, bottomEnd = 25.dp, bottomStart = 25.dp),
                            border = BorderStroke(BORDER_WIDTH, MaterialTheme.colorScheme.onBackground),
                        ) {
                            Image(
                                painter = painterResource(id = terrainType.image), //change to terrains
                                contentDescription = "",
                                modifier = Modifier.fillMaxWidth(),
                                colorFilter = ColorFilter.tint(Color(0xFF928572), blendMode = BlendMode.Darken),
                                contentScale = ContentScale.Crop
                            )

                        }

                        Column(
                            modifier = Modifier
                                .fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "Terrain Type", fontWeight = FontWeight.Bold, color = Color.White)
                            Text(
                                modifier = Modifier
//                            .align(Alignment.)
//                            .fillMaxSize()
//                                    .shadow(20.dp, MaterialTheme.shapes.medium)
//                                    .background(Color.Black.copy(0.15f))
                                    .padding(vertical = 2.dp, horizontal = 10.dp),
                                text = "${terrainType.terrainType}",
                                style = TextStyle(
                                    fontSize = MaterialTheme.typography.headlineLarge.fontSize,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center,
                                    color = Color.White
                                ),
                            ) //the terrain name
                        }
                    }
                }
            }
        }
    ) {
        Box(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.SpaceEvenly
        ) {

            LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp), horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(
                        top = 0.dp,
                        bottom = 0.dp,
                        end = SCREEN_PADDING,
                        start = SCREEN_PADDING
                    )
                    .fillMaxWidth()) {
                // Title of the page
                item {
                    Text(text = "Suitable Tire Types",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(top= SCREEN_PADDING))
                }

                items(tireTypes.size) { index ->
                    // tire images
                    val tireImages = tireTypes[index].images

                    OutlinedCard(
                        modifier = Modifier
//                            .height(300.dp)
                            .fillMaxWidth(),
                        border = BorderStroke( BORDER_WIDTH, color = MaterialTheme.colorScheme.onBackground),
                        colors = CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.primary),
                        shape = MaterialTheme.shapes.extraLarge,
                    ) {
                        Column(modifier = Modifier.fillMaxSize()) {
                            Text(text = tireTypes[index].tireType,
                                color = MaterialTheme.colorScheme.tertiary,
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleLarge,
                                modifier = Modifier
                                    .padding(8.dp)
                                    .height(25.dp)
                                    .fillMaxWidth()
                            )

                            Box(modifier = Modifier
                                .fillMaxSize()
                                .clip(MaterialTheme.shapes.extraLarge)
                                .border( BORDER_WIDTH, MaterialTheme.colorScheme.tertiary,MaterialTheme.shapes.extraLarge)
                            ) {

                                LazyRow(modifier = Modifier.fillMaxSize().aspectRatio(1f)) {
                                    items( tireImages.size) { imageIndex->
//                                        Row(Modifier.fillMaxWidth().height(100.dp).background(Color.Red)) {
//                                            Spacer(modifier = Modifier
//                                                .background(Color.Black)
//                                                .fillMaxWidth()
//                                                .weight(1f))
//                                        }
                                        Box(modifier = Modifier) {
                                            Image(
                                                painter = painterResource(id = tireImages[imageIndex]),
                                                contentScale = ContentScale.FillHeight,
                                                contentDescription = "",
                                                modifier = Modifier.fillMaxHeight().aspectRatio(1f)
                                            )


                                        }
                                    }
                                }

                                Row(modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.BottomCenter)
                                    .padding(bottom = 5.dp),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically) {
                                    Icon(modifier = Modifier.size(16.dp),
                                        tint =  Color.White,
                                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                                        contentDescription = ""
                                    )
                                    Text(text = "  Swipe to see more  ",
                                        color = Color.White,
                                        fontWeight = FontWeight.Black)

                                    Icon(modifier = Modifier.size(16.dp),
                                        tint =  Color.White,
                                        imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                                        contentDescription = ""
                                    )

                                }

                            }


                        }

                    }

                    val annotatedString = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("Tire Tread Patterns: ")
                        }
                        append(tireTypes[index].tireTread)
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    // Tire Tread Patterns
                    Text(modifier = Modifier.fillMaxWidth(),
                        text = annotatedString,
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(modifier = Modifier.fillMaxWidth(),
                        text = tireTypes[index].tireDesc,
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                }
            }
        }
    }
}

@Preview
@Composable
fun HighwayCatalogPreview() {
    TireWiseTheme {
        TerrainInfoScreen(TerrainType.Highway, {})
    }
}
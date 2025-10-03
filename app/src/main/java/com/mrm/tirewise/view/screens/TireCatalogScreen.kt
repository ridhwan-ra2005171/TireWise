package com.mrm.tirewise.view.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mrm.tirewise.view.screens.catalogScreens.TerrainInfoScreen
import com.mrm.tirewise.view.screens.catalogScreens.TerrainType
import com.mrm.tirewise.view.theme.BORDER_WIDTH
import com.mrm.tirewise.view.theme.SCREEN_PADDING
import com.mrm.tirewise.view.theme.TireWiseTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TireCatalogScreen(onNavigateUp: () -> Unit) {

    val terrainTypeList = listOf(
        TerrainType.Highway,
        TerrainType.OffRoad,
        TerrainType.Sandy,
        TerrainType.Muddy,
        TerrainType.Snowy,
//        TerrainType.Mountainous,
    )

    var openInfo by remember {
        mutableStateOf(false)
    }

    var selectedTerrain by remember {
        mutableStateOf(terrainTypeList[0])
    }

    // show terrain tire(S) info
    if (openInfo) {
        TerrainInfoScreen( terrainType = selectedTerrain, onClose = { openInfo = false })
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.shadow(15.dp),
                title = { Text(text = "Tire Catalog", fontWeight = FontWeight.Bold) },
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
    ) {
        TerrainListScreen(paddingValues = it, terrainTypeList, onTerrainSelected = { selectedTerrain = it; openInfo = true })
    }

}

@Composable
fun TerrainListScreen(
    paddingValues: PaddingValues,
    terrainTypeList: List<TerrainType>,
    onTerrainSelected: (TerrainType) -> Unit,
) {

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .padding(horizontal = SCREEN_PADDING)
            .padding(paddingValues)
            .fillMaxSize()
    ) {
        item { Spacer(modifier = Modifier.height(SCREEN_PADDING)) }
        items(terrainTypeList) { item ->
            TerrainCard(item,
                onClick = {
                    // Show the info screen
                    onTerrainSelected(item)
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
        item { Spacer(modifier = Modifier.height(SCREEN_PADDING)) }
    }

}


@Composable
fun TerrainCard(terrain: TerrainType,onClick: () -> Unit) {
    OutlinedCard(
        shape = MaterialTheme.shapes.large,
        border = BorderStroke(BORDER_WIDTH, MaterialTheme.colorScheme.onBackground),
        modifier = Modifier
            .clip(MaterialTheme.shapes.large)
            .clickable { onClick() },
    ) {
        Box(
            modifier = Modifier
                .height(130.dp)
                .fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = terrain.image), //change to terrains
                contentDescription = "",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier
                        .shadow(15.dp, MaterialTheme.shapes.large)
                        .background(Color.Black.copy(0.01f))
                        .padding(vertical = 2.dp, horizontal = 10.dp),
                    text = "${terrain.terrainType}",
                    style = TextStyle(
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    ),
                ) //the terrain name
            }
        }
    }



}

@Composable
@Preview
fun TerrainCardPreview() {
    TerrainCard(TerrainType.Muddy) {}
}

@Composable
@Preview
fun TerrainListScreenPreview() {
    TireWiseTheme {
        TireCatalogScreen(  onNavigateUp = {  })
    }
}
//
//@Composable
//@Preview
//fun TireCatalogScreenPreview() {
//    TireCatalogScreen()
//}
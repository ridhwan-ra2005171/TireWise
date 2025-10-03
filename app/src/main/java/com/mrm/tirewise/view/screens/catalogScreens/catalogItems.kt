package com.mrm.tirewise.view.screens.catalogScreens

import com.mrm.tirewise.R

sealed class TerrainType(
    val terrainType: String,
    val image: Int,
    val tireTypes: List<TireType>,
) {
    object Highway : TerrainType(
        "Highway Roads",
        R.drawable.paved_road,
        listOf(
            TireType.AllSeasonTires,
        )
    )

    object Snowy : TerrainType(
        "Snowy Roads",
        R.drawable.snow_road,
        listOf(
            TireType.WinterSnowTires,
        )
    )

    object Sandy : TerrainType(
        "Sandy Roads",
        R.drawable.sandy_road,
        listOf(
            TireType.AllTerrainTires,
            TireType.MudTerrainTires
        )
    )

    object OffRoad : TerrainType(
        "Off-Roads",
        R.drawable.offroad,
        listOf(
            TireType.AllTerrainTires,
            TireType.MudTerrainTires
        )
    )

//    object Mountainous : TerrainType(
//        "Mountainous Roads",
//        R.drawable.mountain_road,
//        listOf(
//            TireType.AllTerrainTires,
//        )
//    )

    object Muddy : TerrainType(
        "Muddy Roads",
        R.drawable.muddy_road,
        listOf(
            TireType.MudTerrainTires,
            TireType.AllTerrainTires
        )
    )
}

sealed class TireType(
    val tireType: String,
    val tireDesc: String,
    val tireTread: String,
    val images: List<Int> = emptyList()
) {
    object AllSeasonTires : TireType(
        "All-Season Tires",
        "Versatile tires designed to perform well in various weather conditions, including dry and wet surfaces, suitable for everyday driving.",
        "Symmetric, Asymmetric, or Uni-directional Tread",
        listOf(R.drawable.type_allseason_symmetric, R.drawable.type_allseason_assymetric, R.drawable.type_allseason_3)
    )

    object WinterSnowTires : TireType(
        "Winter/Snow Tires",
        "Tires specifically engineered for cold weather conditions and snow-covered roads.",
        "Deep grooves with small cuts for better grip on snow",
        listOf(R.drawable.type_winter_1, R.drawable.type_winter_2)
    )

//    object OffRoadTires : TireType(
//        "Off-Road Tires",
//        "Tires designed for driving on rough, unpaved surfaces such as dirt, gravel, and mud.",
//        "Wide treads for sand traction",
//        listOf(R.drawable.type_allterrain_1)
//    )

    object AllTerrainTires : TireType(
        "All-Terrain Tires",
        "Tires designed to handle a variety of terrains and weather conditions, including mild off-road trails. They offer good grip and durability for venturing off paved roads.",
        "Good grip and puncture resistance",
        listOf(R.drawable.type_allterrain_1, R.drawable.type_allterrain_2, R.drawable.type_allterrain_3)
    )

    object MudTerrainTires : TireType(
        "Mud-Terrain Tires",
        "Specifically engineered tires for extreme off-road conditions, such as deep mud, loose gravel, and rocky surfaces.",
        "Wide, deep tread voids to quickly expel mud and debris",
        listOf(R.drawable.type_mudterrain_1, R.drawable.type_mudterrain_2)
    )

    // Additional tire types can be defined here as needed
}

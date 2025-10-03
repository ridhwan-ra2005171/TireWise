package com.mrm.tirewise.model

enum class TirePosition(val position: String) {
    FRONT_RIGHT("Front Right"),
    BACK_RIGHT("Back Right"),
    FRONT_LEFT("Front Left"),
    BACK_LEFT("Back Left"),
    ALL("All");

    fun getPositionAsString(): String {
        return name.split("_").map { it.lowercase().replaceFirstChar { it.uppercase() } }.joinToString(" ")
    }
}
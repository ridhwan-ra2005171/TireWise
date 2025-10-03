package com.mrm.tirewise.model

import androidx.compose.ui.graphics.Color


class RESULT_COLORS {
    val GREEN: Color = Color(0xFF16B642)
    val ORANGE: Color = Color(0xFFF9A930)
    val RED: Color = Color(0xFFFF212F)
}

data class Result(
    val color: Color,
    val title : String,
    val description : String
)
class ClassificationResult {
    companion object {
        val resultColors = RESULT_COLORS()
    }
    fun getResult(label: String): Result {
       return when (label.lowercase()) {
            "Bad".lowercase() -> {
                Result(resultColors.RED,
                    "Poor Tire Condition",
                    "The tire needs to be replaced immediately!")
            }
            "Good".lowercase() -> {
                Result(resultColors.GREEN,
                    "Good Tire Condition",
                    "Your tire is in good condition!")
            }
           "Moderate".lowercase() -> {
               Result(resultColors.ORANGE,
                   "Moderate Tire Condition",
                   "Your tire is in decent condition and needs to be replaced soon.")
           }
            else -> {
                Result(Color.Blue,
                    "ERROR",
                    "Error changing color!")
            }
        }
    }
}
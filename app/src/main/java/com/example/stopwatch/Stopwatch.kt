package com.example.stopwatch

data class Stopwatch(
    val id: Int,
    var totalSec: Int = 0,
    var remainingSec: Int = totalSec,
    var isActive: Boolean
) {
    fun calculateProgress() = (totalSec - remainingSec).toFloat() / totalSec
}
package com.example.diceroller

/**
 * This is the dice class
 */
class Dice(private val numSides: Int) {
    fun roll(): Int {
        return (1..numSides).random()
    }
}
package com.example.diceroller

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

/**
 * This activity allows the user to roll a dice and view the result
 * on the screen.
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val rollButton: Button = findViewById(R.id.button)
        rollButton.setOnClickListener { rollDice() }
    }

    /**
     * Roll the dice and update the screen with the result.
     */
    private fun rollDice() {
        // Create new Dice objects with 6 sides and roll them
        val dice1 = Dice(6)
        val dice2 = Dice(6)
        val diceRoll1 = dice1.roll()
        val diceRoll2 = dice2.roll()

        // Update the screen with the dice rolls
        val resultDice1: TextView = findViewById(R.id.dice1)
        val resultDice2: TextView = findViewById(R.id.dice2)
        resultDice1.text = diceRoll1.toString()
        resultDice2.text = diceRoll2.toString()

        //Display a toast with the results
        Toast.makeText(this, "Dice Rolled! You got $diceRoll1 and $diceRoll2", Toast.LENGTH_SHORT).show()
    }
}
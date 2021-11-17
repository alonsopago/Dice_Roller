/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.diceroller

import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import java.util.*

import android.media.ToneGenerator

import android.media.AudioManager
import android.view.View

/**
 * This activity allows the user to roll a dice and view the result
 * on the screen.
 */
class MainActivity : AppCompatActivity() {
    lateinit var winImage : ImageView
    lateinit var rollButton: Button
    var diceImageArray : ArrayList<ImageView> = ArrayList()

    /**
     * This method is called when the Activity is created.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Find Image in the layout
        diceImageArray.add(findViewById(R.id.imageView))
        diceImageArray.add(findViewById(R.id.imageView2))
        diceImageArray[0].setImageResource(R.drawable.dice_4__orig)
        diceImageArray[1].setImageResource(R.drawable.dice_5_orig)

        // Win banner: https://lovepik.com/images/png-contrast.html%22%3EContrast%20Png%20vectors%20by%20Lovepik.com
        winImage = findViewById(R.id.imageView3)

        // Find the Button in the layout
        rollButton = findViewById(R.id.button)
        // Set a click listener on the button to roll the dice when the user taps the button
        rollButton.setOnClickListener {
            rollEfect(Dice(6))
        }
    }

    // Roll efect
    private fun rollEfect(dice: Dice){
        val diceImageArrayResult : ArrayList<Int> = ArrayList()
        winImage.visibility = View.INVISIBLE
        rollButton.visibility = View.INVISIBLE

        //Sound generator
        val toneGen1 = ToneGenerator(AudioManager.STREAM_MUSIC, 100)
        // Timer generator
        val timer = object: CountDownTimer(3000, 500) {
            //On tick generator
            override fun onTick(millisUntilFinished: Long) {
                diceImageArrayResult.clear()
                for(i in 0..1) {
                    diceImageArrayResult.add(dice.roll())
                    // Determine which drawable resource ID to use based on the dice roll
                    val drawableResource = when (diceImageArrayResult[i]) {
                        1 -> R.drawable.dice_1_orig
                        2 -> R.drawable.dice_2_orig
                        3 -> R.drawable.dice_3_orig
                        4 -> R.drawable.dice_4__orig
                        5 -> R.drawable.dice_5_orig
                        else -> R.drawable.dice_6_orig
                    }
                    // Update the ImageView with the correct drawable resource ID
                    diceImageArray[i].rotation = (0..359).random().toFloat()
                    diceImageArray[i].setImageResource(drawableResource)
                    toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP, 200)
                }
            }
            override fun onFinish() {
                for(i in 0..1) {
                    diceImageArray[i].rotation = 0f
                }
                toneGen1.startTone(ToneGenerator.TONE_SUP_CONFIRM, 200)
                toneGen1.startTone(ToneGenerator.TONE_SUP_CONFIRM, 200)
                if(diceImageArrayResult[0] == diceImageArrayResult[1]){
                    winImage.setImageResource(R.drawable.win)
                    rollButton.setText(R.string.win)
                }
                else{
                    winImage.setImageResource(R.drawable.youlose)
                    rollButton.setText(R.string.lose)

                }
                winImage.visibility = View.VISIBLE
                rollButton.visibility = View.VISIBLE
            }
        }
        timer.start()
    }
}

/**
 * Dice with a fixed number of sides.
 */
class Dice(private val numSides: Int) {

    /**
     * Do a random dice roll and return the result.
     */
    fun roll(): Int {
        return (1..numSides).random()
    }
}
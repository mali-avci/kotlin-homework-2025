package com.example.toplamamakinesi

import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import com.example.toplamamakinesi.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var currentNumber = ""
    private var totalSum = 0
    private var operationsString = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Rakam tuşları
        val numberButtons = listOf(
            binding.button0, binding.button1, binding.button2,
            binding.button3, binding.button4, binding.button5,
            binding.button6, binding.button7, binding.button8, binding.button9
        )

        numberButtons.forEachIndexed { index, button ->
            button.setOnClickListener {
                currentNumber += index.toString()
                updateDisplay()
            }
        }

        // + tuşu
        binding.buttonTopla.setOnClickListener {
            if (currentNumber.isNotEmpty()) {
                totalSum += currentNumber.toInt()
                operationsString += if (operationsString.isEmpty()) {
                    currentNumber
                } else {
                    " + $currentNumber"
                }
                currentNumber = ""
                updateDisplay()
            }
        }

        // = tuşu
        binding.buttonEsittir.setOnClickListener {
            if (currentNumber.isNotEmpty()) {
                totalSum += currentNumber.toInt()
                operationsString += " + $currentNumber"
                currentNumber = ""
            }
            updateDisplay(showResult = true)
        }

        // C (sıfırlama) tuşu
        binding.buttonSifirla.setOnClickListener {
            totalSum = 0
            currentNumber = ""
            operationsString = ""
            binding.textViewResult.text = "0"
        }

    }
    private fun updateDisplay(showResult: Boolean = false) {
        val displayText = if (showResult) {
            "$operationsString = $totalSum"
        } else {
            if (operationsString.isEmpty()) {
                currentNumber
            } else {
                "$operationsString + $currentNumber"
            }
        }

        binding.textViewResult.text = displayText
    }
}

package com.rickrip.mycalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.core.widget.TextViewCompat
import kotlin.math.*

class MainActivity : AppCompatActivity() {

    private companion object {
        const val MAX_SINGLE_NUMBER_DIGITS = 12
        const val KEY_OUTPUT_STRING = "KEY_OUTPUT_STRING"
        const val DEFAULT_OUTPUT_VALUE = "0"
    }

    // 0 -> entering first value
    // 1 -> operator entered, digits haven't yet
    // 2 -> secVal's digit entered
    // 3 -> equal pressed, solution on the screen
    private var state = 0
    private var firstValueStr: String = DEFAULT_OUTPUT_VALUE
    private var operatorStr: String = ""
    private var secondValueStr: String = DEFAULT_OUTPUT_VALUE

    private lateinit var tvInput: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_my2)

        println("---${savedInstanceState?.getString(KEY_OUTPUT_STRING)}")

        tvInput = findViewById(R.id.tvInput)
        tvInput.text = savedInstanceState?.getString(KEY_OUTPUT_STRING) ?: "0"

        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.root_view)
        ) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemGestures())
            view.updatePadding(insets.left, insets.top, insets.right, insets.bottom)
            WindowInsetsCompat.CONSUMED
        }
    }

    fun onDigit(view: View) {

        val str = (view as Button).text

        when (state) {
            0 -> {
                if (firstValueStr.length < MAX_SINGLE_NUMBER_DIGITS) {
                    if (firstValueStr == "0") {
                        firstValueStr = ""
                    }
                    if (firstValueStr == "-0") {
                        firstValueStr = "-"
                    }
                    firstValueStr += str
                    if (tvInput.text.toString() == "0") {
                        tvInput.text = ""
                    }
                    if (tvInput.text.toString() == "-0") {
                        tvInput.text = "-"
                    }
                    tvInput.append(str)
                } else {
                    Toast.makeText(this, "Value digits limit reached!", Toast.LENGTH_SHORT).show()
                }

            }

            1 -> {
                secondValueStr = ""
                secondValueStr += str
                state = 2
                tvInput.append(str)
            }

            2 -> {
                if (secondValueStr.length < MAX_SINGLE_NUMBER_DIGITS) {
                    if (secondValueStr == "-0") {
                        secondValueStr = "-"
                    }
                    secondValueStr += str
                    tvInput.text = firstValueStr
                    tvInput.append(operatorStr)
                    tvInput.append(secondValueStr)
                } else {
                    Toast.makeText(this, "Value digits limit reached!", Toast.LENGTH_SHORT).show()
                }

            }

            3 -> {
                firstValueStr = ""
                firstValueStr += str
                tvInput.text = ""
                tvInput.append(str)
                state = 0
            }

            4 -> {}
        }


    }

    fun onClear(view: View) {
        tvInput.text = DEFAULT_OUTPUT_VALUE
        state = 0
        firstValueStr = DEFAULT_OUTPUT_VALUE
        operatorStr = ""
        secondValueStr = DEFAULT_OUTPUT_VALUE
    }

    fun onDelete(view: View) {

        when (state) {
            0 -> {
                if (firstValueStr.isNotEmpty()) {
                    firstValueStr = firstValueStr.dropLast(1)
                }
                tvInput.text = firstValueStr
                if (tvInput.text.toString().isEmpty()) {
                    firstValueStr = DEFAULT_OUTPUT_VALUE
                    tvInput.text = DEFAULT_OUTPUT_VALUE
                }
            }

            1 -> {
                operatorStr = ""
                state = 0
                tvInput.text = firstValueStr
            }

            2 -> {
                secondValueStr = secondValueStr.dropLast(1)
                if (secondValueStr == "-") {
                    secondValueStr = ""
                }
                tvInput.text = firstValueStr
                tvInput.append(operatorStr)
                tvInput.append(secondValueStr)
                if (secondValueStr.isEmpty()) {
                    secondValueStr = DEFAULT_OUTPUT_VALUE
                    state = 1
                }
            }

            3 -> {
                if (firstValueStr.isNotEmpty()) {
                    firstValueStr = firstValueStr.dropLast(1)
                }
                tvInput.text = firstValueStr
                state = 1
            }

            4 -> {}
        }

    }

    fun onPlusMinus(view: View) {

        when (state) {
            0 -> {
                if (!firstValueStr.startsWith("-")) {
                    firstValueStr = "-$firstValueStr"
                } else {
                    firstValueStr = firstValueStr.drop(1)
                }
                tvInput.text = firstValueStr
            }

            1 -> {
                if (!secondValueStr.startsWith("-")) {
                    secondValueStr = "-$secondValueStr"
                } else {
                    secondValueStr = secondValueStr.drop(1)
                }
                tvInput.text = firstValueStr
                tvInput.append(operatorStr)
                tvInput.append(secondValueStr)
                state = 2
            }

            2 -> {
                if (!secondValueStr.startsWith("-")) {
                    secondValueStr = "-$secondValueStr"
                } else {
                    secondValueStr = secondValueStr.drop(1)
                }
                tvInput.text = firstValueStr
                tvInput.append(operatorStr)
                tvInput.append(secondValueStr)
            }

            3 -> {
                if (!firstValueStr.startsWith("-")) {
                    firstValueStr = "-$firstValueStr"
                } else {
                    firstValueStr = firstValueStr.drop(1)
                }
                tvInput.text = firstValueStr
                state = 0
            }

            4 -> {}
        }

    }

    fun onDecimalPoint(view: View) {

        when (state) {
            0 -> {
                if (firstValueStr.length < MAX_SINGLE_NUMBER_DIGITS) {
                    if (firstValueStr.contains(".")) {
                        for (i in firstValueStr.indices) {
                            if (firstValueStr[i] == '.') {
                                val r1 = firstValueStr.dropLast(firstValueStr.length - i)
                                val r2 = firstValueStr.drop(i + 1)
                                firstValueStr = ""
                                firstValueStr += r1
                                firstValueStr += r2
                                if (firstValueStr.startsWith("0") && firstValueStr.length > 1) {
                                    firstValueStr = firstValueStr.drop(1)
                                }
                                break
                            }
                        }
                    }
                    firstValueStr += "."
                    tvInput.text = firstValueStr
                } else {
                    Toast.makeText(this, "Value digits limit reached!", Toast.LENGTH_SHORT).show()
                }
            }

            1 -> {
                secondValueStr += "."
                tvInput.text = firstValueStr
                tvInput.append(operatorStr)
                tvInput.append(secondValueStr)
                state = 2
            }

            2 -> {
                if (secondValueStr.length < MAX_SINGLE_NUMBER_DIGITS) {
                    if (secondValueStr.contains(".")) {
                        for (i in secondValueStr.indices) {
                            if (secondValueStr[i] == '.') {
                                val r1 = secondValueStr.dropLast(secondValueStr.length - i)
                                val r2 = secondValueStr.drop(i + 1)
                                secondValueStr = ""
                                secondValueStr += r1
                                secondValueStr += r2
                                if (secondValueStr.startsWith("0") && secondValueStr.length > 1) {
                                    secondValueStr = secondValueStr.drop(1)
                                }
                                break
                            }
                        }
                    }
                    secondValueStr += "."
                    tvInput.text = firstValueStr
                    tvInput.append(operatorStr)
                    tvInput.append(secondValueStr)
                } else {
                    Toast.makeText(this, "Value digits limit reached!", Toast.LENGTH_SHORT).show()
                }
            }

            3 -> {
                if (firstValueStr.length < MAX_SINGLE_NUMBER_DIGITS) {
                    if (firstValueStr.contains(".")) {
                        for (i in firstValueStr.indices) {
                            if (firstValueStr[i] == '.') {
                                val r1 = firstValueStr.dropLast(firstValueStr.length - i)
                                val r2 = firstValueStr.drop(i + 1)
                                firstValueStr = ""
                                firstValueStr += r1
                                firstValueStr += r2
                                if (firstValueStr.startsWith("0") && firstValueStr.length > 1) {
                                    firstValueStr = firstValueStr.drop(1)
                                }
                                break
                            }
                        }
                    }
                    firstValueStr += "."
                    tvInput.text = firstValueStr
                } else {
                    Toast.makeText(this, "Value digits limit reached!", Toast.LENGTH_SHORT).show()
                }
                state = 0
            }

            4 -> {}
        }

    }

    fun onOperator(view: View) {

        operatorStr = (view as Button).text.toString()

        when (state) {
            0 -> {
                firstValueStr = tvInput.text.toString()
                if (firstValueStr.endsWith(".")) {
                    firstValueStr += "0"
                }
                tvInput.text = firstValueStr
                tvInput.append(operatorStr)
                state = 1
            }

            1 -> {
                tvInput.text = firstValueStr
                tvInput.append(operatorStr)
            }

            2 -> {
                tvInput.text = firstValueStr
                tvInput.append(operatorStr)
                tvInput.append(secondValueStr)
            }

            3 -> {
                secondValueStr = "0"
                tvInput.text = firstValueStr
                tvInput.append(operatorStr)
                state = 1
            }

            4 -> {}
        }


    }

    fun onFunc(view: View) {

        val func = (view as Button).text

        when (state) {
            0 -> {
                when (func) {
                    "SIN" -> {
                        tvInput.text = showResultWithoutPointZero(
                            (round(
                                (sin(firstValueStr.toDouble()))
                                        * 1_000_000_000_000
                            ) * 0.000_000_000_001).toString()
                        )
                        firstValueStr = tvInput.text.toString()
                        state = 3
                    }

                    "COS" -> {
                        tvInput.text = showResultWithoutPointZero(
                            (round(
                                (cos(firstValueStr.toDouble()))
                                        * 1_000_000_000_000
                            ) * 0.000_000_000_001).toString()
                        )
                        firstValueStr = tvInput.text.toString()
                        state = 3
                    }

                    "SQR" -> {
                        if (firstValueStr.toDouble() >= 0) {
                            tvInput.text = showResultWithoutPointZero(
                                (round(
                                    (sqrt(firstValueStr.toDouble()))
                                            * 1_000_000_000_000
                                ) * 0.000_000_000_001).toString()
                            )
                            firstValueStr = tvInput.text.toString()
                            state = 3
                        } else {
                            Toast.makeText(
                                this,
                                "Cannot use SQR with negative number!",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }

            1 -> {
                Toast.makeText(this, "Cannot use function in equation!", Toast.LENGTH_LONG).show()
            }

            2 -> {
                Toast.makeText(this, "Cannot use function in equation!", Toast.LENGTH_LONG).show()
            }

            3 -> {
                when (func) {
                    "SIN" -> {
                        tvInput.text = showResultWithoutPointZero(
                            (round(
                                (sin(firstValueStr.toDouble()))
                                        * 1_000_000_000_000
                            ) * 0.000_000_000_001).toString()
                        )
                        firstValueStr = tvInput.text.toString()
                        secondValueStr = ""
                    }

                    "COS" -> {
                        tvInput.text = showResultWithoutPointZero(
                            (round(
                                (cos(firstValueStr.toDouble()))
                                        * 1_000_000_000_000
                            ) * 0.000_000_000_001).toString()
                        )
                        firstValueStr = tvInput.text.toString()
                        secondValueStr = ""
                    }

                    "SQR" -> {
                        if (firstValueStr.toDouble() >= 0) {
                            tvInput.text = showResultWithoutPointZero(
                                (round(
                                    (sqrt(firstValueStr.toDouble()))
                                            * 1_000_000_000_000
                                ) * 0.000_000_000_001).toString()
                            )
                            firstValueStr = tvInput.text.toString()
                            secondValueStr = ""
                        } else {
                            Toast.makeText(
                                this,
                                "Cannot use SQR with negative number!",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }

            4 -> {}
        }
    }

    private fun showResultWithoutPointZero(value: String): String {
        return if (value.contains(".")) {
            val afterPoint: String = value.substringAfter(".")
            if (afterPoint == "0") {
                value.substringBefore(".")
            } else {
                value
            }
        } else value
    }

    fun onEqual(view: View) {

        if (state == 2 || state == 3) {
            when (operatorStr) {
                "-" -> {
                    tvInput.text = showResultWithoutPointZero(
                        (
                                floor(
                                    round(
                                        (firstValueStr.toDouble()
                                                - secondValueStr.toDouble()) * 1_000_000_000
                                    ) / 10.0
                                ) / 1_000_000_00).toString()
                    )
                    firstValueStr = tvInput.text.toString()
                    firstValueStr = tvInput.text.toString()
                    state = 3
                }

                "+" -> {
                    tvInput.text = showResultWithoutPointZero(
                        (
                                floor(
                                    round(
                                        (firstValueStr.toDouble()
                                                + secondValueStr.toDouble()) * 1_000_000_000
                                    ) / 10.0
                                ) / 1_000_000_00).toString()
                    )
                    firstValueStr = tvInput.text.toString()
                    firstValueStr = tvInput.text.toString()
                    state = 3
                }

                "*" -> {
                    tvInput.text = showResultWithoutPointZero(
                        (
                                floor(
                                    round(
                                        (firstValueStr.toDouble()
                                                * secondValueStr.toDouble()) * 1_000_000_000
                                    ) / 10.0
                                ) / 1_000_000_00).toString()
                    )
                    firstValueStr = tvInput.text.toString()
                    firstValueStr = tvInput.text.toString()
                    state = 3
                }

                "/" -> {
                    if (secondValueStr.toDouble() != 0.0) {
                        tvInput.text = showResultWithoutPointZero(
                            (
                                    floor(
                                        round(
                                            (firstValueStr.toDouble()
                                                    / secondValueStr.toDouble()) * 1_000_000_000
                                        ) / 10.0
                                    ) / 1_000_000_00).toString()
                        )

                        //see https://gordonlesti.com/inexact-rounding-up-or-down-with-decimal-digits/

                        firstValueStr = tvInput.text.toString()
                        state = 3
                    } else {
                        Toast.makeText(this, "Dividing by zero isn't allowed!", Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_OUTPUT_STRING, tvInput.text.toString())
    }
}



























package com.example.tipcalculatorapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tipcalculatorapplication.ui.theme.TipCalculatorApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TipCalculatorApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TipScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun TipScreen(modifier: Modifier = Modifier) {
    var amountText by remember { mutableStateOf("") }
    var countText by remember { mutableStateOf("") }
    var tipPercent by remember { mutableFloatStateOf(0f) }

    val amount = checkAmount(amountText)
    val count = checkCount(countText)
    val discountPercent = if (count == null) 0 else discount(count)

    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("Сумма заказа:")
        OutlinedTextField(
            value = amountText,
            onValueChange = { amountText = it },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Text("Количество блюд:")
        OutlinedTextField(
            value = countText,
            onValueChange = { countText = it },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Text("Чаевые:")
        Slider(
            value = tipPercent,
            onValueChange = { tipPercent = it },
            valueRange = 0f..25f,
            steps = 24,
            modifier = Modifier.fillMaxWidth()
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("0")
            Text("25")
        }
        Text("Скидка:")
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            listOf(3, 5, 7, 10).forEach { percent ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = discountPercent == percent,
                        onClick = {}
                    )
                    Text("$percent%")
                }
            }
        }
        if (amount != null && count != null) {
            val tipSum = tip(amount, tipPercent.toInt())
            val discountSum = amount * discountPercent / 100.0
            val totalSum = amount - discountSum + tipSum
            Text("Чаевые: ${"%.2f".format(tipSum)}")
            Text("Скидка: ${"%.2f".format(discountSum)}")
            Text("Итог: ${"%.2f".format(totalSum)}")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TipScreenPreview() {
    TipCalculatorApplicationTheme {
        TipScreen()
    }
}

fun checkAmount(text: String): Double? {
    val amount = text.trim().replace(',', '.').toDoubleOrNull()
    if (amount == null || amount <= 0.0) {
        return null
    }
    return amount
}

fun checkCount(text: String): Int? {
    val count = text.trim().toIntOrNull()
    if (count == null || count < 0) {
        return null
    }
    return count
}

fun discount(count: Int): Int {
    if (count <= 0) {
        return 0
    }
    if (count <= 2) {
        return 3
    }
    if (count <= 5) {
        return 5
    }
    if (count <= 10) {
        return 7
    }
    return 10
}

fun tip(amount: Double, percent: Int): Double {
    return amount * percent / 100.0
}

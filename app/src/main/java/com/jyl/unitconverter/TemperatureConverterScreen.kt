package com.jyl.unitconverter
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TemperatureConverterScreen() {
    val tempUnits = listOf("Celsius", "Fahrenheit", "Kelvin")
    var input by remember { mutableStateOf("") }
    var fromUnit by remember { mutableStateOf("Fahrenheit") }
    var toUnit by remember { mutableStateOf("Celsius") }
    val inputDouble = input.toDoubleOrNull() ?: 0.0

    val result = convertTemperature(inputDouble, fromUnit, toUnit)
    val resultFormatted = String.format("%.2f %s", result, toUnit)

    Column(modifier = Modifier.padding(16.dp).fillMaxSize().verticalScroll(rememberScrollState())) {
        Text("Temperature Converter", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))
        ClearableOutlinedTextField(value = input, onValueChange = { input = it }, label = "Enter value", modifier = Modifier.fillMaxWidth())

        Spacer(modifier = Modifier.height(16.dp))
        UnitDropdownRow("From", fromUnit, { fromUnit = it }, "To", toUnit, { toUnit = it }, tempUnits)

        Spacer(modifier = Modifier.height(16.dp))
        ResultText("Result: $resultFormatted")
        Spacer(modifier = Modifier.height(24.dp))
        TemperatureConversionExamples()
    }
}

fun convertTemperature(value: Double, from: String, to: String): Double {
    val celsius = when (from) {
        "Fahrenheit" -> (value - 32) * 5 / 9
        "Kelvin" -> value - 273.15
        else -> value
    }
    return when (to) {
        "Fahrenheit" -> celsius * 9 / 5 + 32
        "Kelvin" -> celsius + 273.15
        else -> celsius
    }
}

@Composable
fun TemperatureConversionExamples() {
    val examples = listOf(
        "°C → °F: (°C × 9/5) + 32",
        "°F → °C: (°F − 32) × 5/9",
        "°C → K: °C + 273.15",
        "K → °C: K − 273.15",
        "°F → K: (°F − 32) × 5/9 + 273.15",
        "K → °F: (K − 273.15) × 9/5 + 32",
        "0 °C = 32 °F",
        "100 °C = 212 °F",
        "0 K = −273.15 °C",
        "300 K = 80.33 °F"
    )
    Text("📘 Temperature Conversion Examples & Formulas", style = MaterialTheme.typography.titleMedium)
    Spacer(modifier = Modifier.height(8.dp))
    examples.forEach { Text("• $it", style = MaterialTheme.typography.bodyMedium) }
}

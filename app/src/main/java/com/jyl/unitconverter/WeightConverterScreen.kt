package com.jyl.unitconverter
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
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
fun WeightConverterScreen() {
    val weightImperialUnits = listOf("Ounce", "Pound", "Stone")
    val weightMetricUnits = listOf("Gram", "Kilogram")

    var input by remember { mutableStateOf("") }
    var fromUnit by remember { mutableStateOf("Kilogram") }
    var toUnit by remember { mutableStateOf("Pound") }
    var showSubunit by remember { mutableStateOf(true) }

    val inputDouble = input.toDoubleOrNull() ?: 0.0
    val result = convertWeight(inputDouble, fromUnit, toUnit)
    val resultFormatted = if (showSubunit) convertToSubunitString(result, toUnit) else String.format("%.4f %s", result, toUnit)

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Text("Imperial ⇄ Metric Weight Converter", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))
        ClearableOutlinedTextField(value = input, onValueChange = { input = it }, label = "Enter value", modifier = Modifier.fillMaxWidth())

        Spacer(modifier = Modifier.height(16.dp))

        UnitDropdownRow("From", fromUnit, { fromUnit = it }, "To", toUnit, { toUnit = it }, weightImperialUnits + weightMetricUnits)
        Spacer(modifier = Modifier.height(16.dp))
        Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
            Checkbox(checked = showSubunit, onCheckedChange = { showSubunit = it })
            Spacer(modifier = Modifier.width(8.dp))
            Text("Show result in subunits")
        }

        Spacer(modifier = Modifier.height(16.dp))
        ResultText("Result: $resultFormatted")
        //Text("Result: $resultFormatted", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(24.dp))
        WeightConversionExamples()
    }
}

fun convertWeight(value: Double, from: String, to: String): Double {
    val kilograms = when (from) {
        "Ounce" -> value * 0.0283495
        "Pound" -> value * 0.453592
        "Stone" -> value * 6.35029
        "Gram" -> value / 1000
        "Kilogram" -> value
        else -> value
    }

    return when (to) {
        "Ounce" -> kilograms / 0.0283495
        "Pound" -> kilograms / 0.453592
        "Stone" -> kilograms / 6.35029
        "Gram" -> kilograms * 1000
        "Kilogram" -> kilograms
        else -> kilograms
    }
}

@Composable
fun WeightConversionExamples() {
    val examples = listOf(
        Triple("1 ounce =", "28.3495 g", "1 oz × 28.3495 = 28.3495 g"),
        Triple("1 pound =", "453.592 g", "1 lb × 453.592 = 453.592 g"),
        Triple("1 stone =", "6.35029 kg", "1 st × 6.35029 = 6.35029 kg"),
        Triple("100 g =", "3.5274 oz", "100 g × 0.035274 = 3.5274 oz"),
        Triple("1 kg =", "2.2046 lb", "1 kg × 2.2046 = 2.2046 lb"),
        Triple("500 g =", "1.1023 lb", "500 g × 0.0022046 = 1.1023 lb"),
        Triple("2 kg =", "4.4092 lb", "2 kg × 2.2046 = 4.4092 lb"),
        Triple("5 lb =", "2.26796 kg", "5 lb × 0.453592 = 2.26796 kg"),
        Triple("8 oz =", "226.796 g", "8 oz × 28.3495 = 226.796 g"),
        Triple("3 st =", "19.0509 kg", "3 st × 6.35029 = 19.0509 kg")
    )

    Text("📘 Weight Conversion Examples & Formulas", style = MaterialTheme.typography.titleMedium)
    Spacer(modifier = Modifier.height(8.dp))

    examples.forEach {
        Text("• ${it.first} ${it.second}", style = MaterialTheme.typography.bodyMedium)
        Text("   ${it.third}", style = MaterialTheme.typography.bodySmall)
        Spacer(modifier = Modifier.height(4.dp))
    }
}

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
import kotlin.math.floor

val imperialUnits = listOf("Inch", "Foot", "Yard", "Mile")
val metricUnits = listOf("Millimeter", "Centimeter", "Meter", "Kilometer")

fun convertToSubunitString(value: Double, unit: String): String {
    return when (unit) {
        "Mile" -> {
            val miles = floor(value).toInt()
            val feet = floor((value - miles) * 5280).toInt()
            "$miles mi $feet ft"
        }
        "Yard" -> {
            val yards = floor(value).toInt()
            val feet = floor((value - yards) * 3).toInt()
            "$yards yd $feet ft"
        }
        "Foot" -> {
            val feet = floor(value).toInt()
            val inches = floor((value - feet) * 12).toInt()
            "$feet ft $inches in"
        }
        "Meter" -> {
            val meters = floor(value).toInt()
            val centimeters = floor((value - meters) * 100).toInt()
            "$meters m $centimeters cm"
        }
        "Kilometer" -> {
            val km = floor(value).toInt()
            val m = floor((value - km) * 1000).toInt()
            "$km km $m m"
        }
        "Pound" -> {
            val pounds = floor(value).toInt()
            val ounces = floor((value - pounds) * 16).toInt()
            "$pounds lb $ounces oz"
        }
        else -> "${value.toInt()} $unit"
    }
}

fun convertImperialMetric(value: Double, from: String, to: String): Double {
    val meters = when (from) {
        "Inch" -> value * 0.0254
        "Foot" -> value * 0.3048
        "Yard" -> value * 0.9144
        "Mile" -> value * 1609.34
        "Millimeter" -> value / 1000
        "Centimeter" -> value / 100
        "Meter" -> value
        "Kilometer" -> value * 1000
        else -> value
    }

    return when (to) {
        "Inch" -> meters / 0.0254
        "Foot" -> meters / 0.3048
        "Yard" -> meters / 0.9144
        "Mile" -> meters / 1609.34
        "Millimeter" -> meters * 1000
        "Centimeter" -> meters * 100
        "Meter" -> meters
        "Kilometer" -> meters / 1000
        else -> meters
    }
}


@Composable
fun ImperialMetricConverterScreen() {
    var input by remember { mutableStateOf("") }
    var fromUnit by remember { mutableStateOf("Centimeter") }
    var toUnit by remember { mutableStateOf("Foot") }
    var showSubunit by remember { mutableStateOf(true) }

    val inputDouble = input.toDoubleOrNull() ?: 0.0
    val result = convertImperialMetric(inputDouble, fromUnit, toUnit)
    val resultFormatted = if (showSubunit) convertToSubunitString(result, toUnit) else String.format("%.4f %s", result, toUnit)

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Text("Imperial ⇄ Metric Converter", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))

        /*OutlinedTextField(
            value = input,
            onValueChange = { input = it },
            label = { Text("Enter value") },
            modifier = Modifier.fillMaxWidth()
        )*/

        ClearableOutlinedTextField(value = input, onValueChange = { input = it }, label = "Enter value", modifier = Modifier.fillMaxWidth())


        Spacer(modifier = Modifier.height(16.dp))
        UnitDropdownRow("From", fromUnit, { fromUnit = it }, "To", toUnit, { toUnit = it }, imperialUnits + metricUnits)
        //UnitDropdown("From", fromUnit, imperialUnits + metricUnits) { fromUnit = it }
        //Spacer(modifier = Modifier.height(8.dp))
        //UnitDropdown("To", toUnit, imperialUnits + metricUnits) { toUnit = it }

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
        ConversionExamples()
    }
}

@Composable
fun ConversionExamples() {
    val examples = listOf(
        Triple("1 inch =", "2.54 cm", "1 in × 2.54 = 2.54 cm"),
        Triple("1 foot =", "0.3048 m", "1 ft × 0.3048 = 0.3048 m"),
        Triple("1 yard =", "0.9144 m", "1 yd × 0.9144 = 0.9144 m"),
        Triple("1 mile =", "1.60934 km", "1 mi × 1.60934 = 1.60934 km"),
        Triple("1 cm =", "0.3937 in", "1 cm × 0.3937 = 0.3937 in"),
        Triple("1 m =", "3.2808 ft", "1 m × 3.2808 = 3.2808 ft"),
        Triple("1 km =", "0.6214 mi", "1 km × 0.6214 = 0.6214 mi"),
        Triple("10 in =", "25.4 cm", "10 in × 2.54 = 25.4 cm"),
        Triple("5 ft =", "1.524 m", "5 ft × 0.3048 = 1.524 m"),
        Triple("3 yd =", "2.7432 m", "3 yd × 0.9144 = 2.7432 m"),
        Triple("2 mi =", "3.21868 km", "2 mi × 1.60934 = 3.21868 km"),
        Triple("100 mm =", "3.937 in", "100 mm × 0.03937 = 3.937 in"),
        Triple("250 cm =", "8.2021 ft", "250 cm × 0.032808 = 8.2021 ft"),
        Triple("3 m =", "3.2808 yd", "3 m × 1.0936 = 3.2808 yd"),
        Triple("5 km =", "3.1069 mi", "5 km × 0.6214 = 3.1069 mi")
    )

    Text("📘 Conversion Examples & Formulas", style = MaterialTheme.typography.titleMedium)
    Spacer(modifier = Modifier.height(8.dp))

    examples.forEach {
        Text("• ${it.first} ${it.second}", style = MaterialTheme.typography.bodyMedium)
        Text("   ${it.third}", style = MaterialTheme.typography.bodySmall)
        Spacer(modifier = Modifier.height(4.dp))
    }
}

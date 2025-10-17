package com.jyl.unitconverter

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
fun AreaConverterScreen() {
    var input by remember { mutableStateOf("") }
    var fromUnit by remember { mutableStateOf("Square Meter") }
    var toUnit by remember { mutableStateOf("Square Foot") }

    val inputDouble = input.toDoubleOrNull() ?: 0.0
    var showSubunit by remember { mutableStateOf(true) }
    val result = convertArea(inputDouble, fromUnit, toUnit)
    val resultFormatted = if (showSubunit) areaSubunitBreakdown(result, toUnit) else String.format("%.4f %s", result, toUnit)

    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text("Area Converter", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(8.dp))

        ClearableOutlinedTextField(input, { input = it }, "Enter area", Modifier.fillMaxWidth())
        Spacer(Modifier.height(8.dp))

        UnitDropdownRow("From", fromUnit, { fromUnit = it }, "To", toUnit, { toUnit = it }, areaUnits)
        Spacer(Modifier.height(8.dp))
        Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
            Checkbox(checked = showSubunit, onCheckedChange = { showSubunit = it })
            Spacer(modifier = Modifier.width(8.dp))
            Text("Show result in subunits")
        }

        Spacer(modifier = Modifier.height(16.dp))
        ResultText("Result: $resultFormatted")
        //Text("Result: $resultFormatted", style = MaterialTheme.typography.headlineMedium)

        //ResultText(String.format("%.4f %s", result, toUnit))
        Spacer(Modifier.height(16.dp))

        AreaConversionExamples()
    }
}

val areaUnits = listOf(
    "Square Meter",
    "Square Kilometer",
    "Square Centimeter",
    "Square Millimeter",
    "Hectare",
    "Square Inch",
    "Square Foot",
    "Square Yard",
    "Acre"
)

fun convertArea(value: Double, from: String, to: String): Double {
    val sqMeters = when (from) {
        "Square Kilometer" -> value * 1_000_000.0
        "Square Meter" -> value
        "Square Centimeter" -> value / 10_000.0
        "Square Millimeter" -> value / 1_000_000.0
        "Hectare" -> value * 10_000.0
        "Square Inch" -> value * 0.00064516
        "Square Foot" -> value * 0.092903
        "Square Yard" -> value * 0.836127
        "Acre" -> value * 4046.86
        else -> value
    }

    return when (to) {
        "Square Kilometer" -> sqMeters / 1_000_000.0
        "Square Meter" -> sqMeters
        "Square Centimeter" -> sqMeters * 10_000.0
        "Square Millimeter" -> sqMeters * 1_000_000.0
        "Hectare" -> sqMeters / 10_000.0
        "Square Inch" -> sqMeters / 0.00064516
        "Square Foot" -> sqMeters / 0.092903
        "Square Yard" -> sqMeters / 0.836127
        "Acre" -> sqMeters / 4046.86
        else -> sqMeters
    }
}

@Composable
fun AreaConversionExamples() {
    val examples = listOf(
        "1 m² = 10,000 cm²",
        "1 m² = 1,000,000 mm²",
        "1 ha = 10,000 m²",
        "1 km² = 1,000,000 m²",
        "1 acre = 4046.86 m²",
        "1 ft² = 144 in²",
        "1 yd² = 9 ft²",
        "1 m² = 10.7639 ft²",
        "1 in² = 6.4516 cm²",
        "1 acre ≈ 43,560 ft²"
    )
    Text("📐 Area Conversion Examples", style = MaterialTheme.typography.titleMedium)
    Spacer(Modifier.height(4.dp))
    examples.forEach { Text("• $it", style = MaterialTheme.typography.bodyMedium) }
}

fun areaSubunitBreakdown1(value: Double, unit: String): String {
    return when (unit) {
        "Square Meter" -> {
            val cm2 = value * 10_000
            val mm2 = value * 1_000_000
            "%.2f m² = %.0f cm² = %.0f mm²".format(value, cm2, mm2)
        }
        "Square Kilometer" -> {
            val m2 = value * 1_000_000
            val ha = value * 100
            "%.2f km² = %.0f m² = %.0f ha".format(value, m2, ha)
        }
        "Square Centimeter" -> {
            val mm2 = value * 100
            val m2 = value / 10_000
            "%.2f cm² = %.2f m² = %.0f mm²".format(value, m2, mm2)
        }
        "Square Foot" -> {
            val inch2 = value * 144
            "%.2f ft² = %.0f in²".format(value, inch2)
        }
        else -> ""
    }
}

fun areaSubunitBreakdown(value: Double, unit: String): String {
    return when (unit) {
        "Square Meter" -> {
            val cm2 = value * 10_000
            val mm2 = value * 1_000_000
            "%.2f m² = %.0f cm² = %.0f mm²".format(value, cm2, mm2)
        }
        "Square Kilometer" -> {
            val m2 = value * 1_000_000
            val ha = value * 100
            "%.2f km² = %.0f m² = %.0f ha".format(value, m2, ha)
        }
        "Square Centimeter" -> {
            val mm2 = value * 100
            val m2 = value / 10_000
            "%.2f cm² = %.2f m² = %.0f mm²".format(value, m2, mm2)
        }
        "Square Foot" -> {
            val inch2 = value * 144
            val yd2 = value / 9.0
            val acre = value / 43_560.0
            "%.2f ft² = %.0f in² = %.2f yd² = %.4f acres".format(value, inch2, yd2, acre)
        }
        "Square Yard" -> {
            val ft2 = value * 9.0
            val in2 = ft2 * 144
            val acre = ft2 / 43_560.0
            "%.2f yd² = %.0f ft² = %.0f in² = %.4f acres".format(value, ft2, in2, acre)
        }
        "Acre" -> {
            val ft2 = value * 43_560.0
            val yd2 = ft2 / 9.0
            "%.2f acres = %.0f ft² = %.0f yd²".format(value, ft2, yd2)
        }
        else -> ""
    }
}



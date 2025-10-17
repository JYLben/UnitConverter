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

@Composable
fun VolumeConverterScreen() {
    val volumeUnits = listOf("Teaspoon", "Tablespoon", "Fluid Ounce", "Cup",
        "Pint", "Quart", "Gallon", "Milliliter", "Liter",
        "Cubic Meter","Cubic Centimeter","Cubic Millimeter")
    var input by remember { mutableStateOf("") }
    var fromUnit by remember { mutableStateOf("Gallon") }
    var toUnit by remember { mutableStateOf("Liter") }
    var showSubunit by remember { mutableStateOf(true) }

    val inputDouble = input.toDoubleOrNull() ?: 0.0
    val result = convertVolume(inputDouble, fromUnit, toUnit)
    val resultFormatted = if (showSubunit) convertVolumeToSubunit(result, toUnit) else String.format("%.4f %s", result, toUnit)

    Column(modifier = Modifier.padding(16.dp).fillMaxSize().verticalScroll(rememberScrollState())) {
        Text("Volume Converter", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))
        ClearableOutlinedTextField(value = input, onValueChange = { input = it }, label = "Enter value", modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(16.dp))
        UnitDropdownRow("From", fromUnit, { fromUnit = it }, "To", toUnit, { toUnit = it }, volumeUnits)

        Spacer(modifier = Modifier.height(16.dp))
        Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
            Checkbox(checked = showSubunit, onCheckedChange = { showSubunit = it })
            Spacer(modifier = Modifier.width(8.dp))
            Text("Show result in subunits")
        }
        Spacer(modifier = Modifier.height(16.dp))
        ResultText("Result: $resultFormatted")
        Spacer(modifier = Modifier.height(24.dp))
        VolumeConversionExamples()
    }
}

fun convertVolume(value: Double, from: String, to: String): Double {
    val liters = when (from) {
        "Teaspoon" -> value * 0.00492892
        "Tablespoon" -> value * 0.0147868
        "Fluid Ounce" -> value * 0.0295735
        "Cup" -> value * 0.24
        "Pint" -> value * 0.473176
        "Quart" -> value * 0.946353
        "Gallon" -> value * 3.78541
        "Milliliter" -> value / 1000
        "Cubic Meter" -> value * 1000.0
        "Cubic Centimeter" -> value / 1000.0
        "Cubic Millimeter" -> value / 1_000_000.0
        "Liter" -> value
        else -> value
    }
    return when (to) {
        "Teaspoon" -> liters / 0.00492892
        "Tablespoon" -> liters / 0.0147868
        "Fluid Ounce" -> liters / 0.0295735
        "Cup" -> liters / 0.24
        "Pint" -> liters / 0.473176
        "Quart" -> liters / 0.946353
        "Gallon" -> liters / 3.78541
        "Milliliter" -> liters * 1000
        "Cubic Meter" -> liters / 1000.0
        "Cubic Centimeter" -> liters * 1000.0
        "Cubic Millimeter" -> liters * 1_000_000.0
        "Liter" -> liters
        else -> liters
    }
}

fun convertVolumeToSubunit(value: Double, unit: String): String {
    return when (unit) {
        "Gallon" -> {
            val gal = floor(value).toInt()
            val qt = ((value - gal) * 4).toInt()
            "$gal gal $qt qt"
        }
        "Quart" -> {
            val qt = floor(value).toInt()
            val pt = ((value - qt) * 2).toInt()
            "$qt qt $pt pt"
        }
        "Pint" -> {
            val pt = floor(value).toInt()
            val cup = ((value - pt) * 2).toInt()
            "$pt pt $cup cup"
        }
        else -> String.format("%.2f %s", value, unit)
    }
}

@Composable
fun VolumeConversionExamples() {
    val examples = listOf(
        "1 tsp = 4.9289 mL (tsp × 4.9289)",
        "1 tbsp = 14.7868 mL (tbsp × 14.7868)",
        "1 fl oz = 29.5735 mL (fl oz × 29.5735)",
        "1 cup = 240 mL (cup × 240)",
        "1 pint = 473.176 mL (pt × 473.176)",
        "1 quart = 946.353 mL (qt × 946.353)",
        "1 gallon = 3.785 L (gal × 3.785)",
        "500 mL = 2.113 cups (mL × 0.00422675)",
        "1 L = 4.226 cups (L × 4.226)",
        "2 L = 0.528 gal (L × 0.264172)",
        "1 m³ = 1000 L",
        "1 cm³ = 1 mL",
        "1 mm³ = 0.001 mL"
    )
    Text("📘 Volume Conversion Examples & Formulas", style = MaterialTheme.typography.titleMedium)
    Spacer(modifier = Modifier.height(8.dp))
    examples.forEach { Text("• $it", style = MaterialTheme.typography.bodyMedium) }
}

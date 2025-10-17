package com.jyl.unitconverter

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

// 🔹 Default offline rates for fallback
val defaultRates = mapOf(
    "USD" to 1.0,
    "EUR" to 0.88,
    "GBP" to 0.76,
    "JPY" to 149.5,
    "CNY" to 7.2,
    "AUD" to 1.55,
    "CAD" to 1.38,
    "CHF" to 0.81,
    "INR" to 87.6
)

var cachedRates: Map<String, Double> = defaultRates

// 🔹 Fetch from Frankfurter API (no API key required)
suspend fun fetchCurrencyRatesSafe(): Map<String, Double> = withContext(Dispatchers.IO) {
    try {
        val url = URL("https://api.frankfurter.app/latest?from=USD")
        val connection = url.openConnection() as HttpURLConnection
        connection.connectTimeout = 5000
        connection.readTimeout = 5000
        connection.requestMethod = "GET"

        val responseCode = connection.responseCode
        if (responseCode != 200) throw Exception("HTTP $responseCode")

        val response = connection.inputStream.bufferedReader().readText()
        val json = JSONObject(response)
        val rates = json.getJSONObject("rates")

        val result = mutableMapOf<String, Double>()
        result["USD"] = 1.0   // Frankfurter doesn't include USD, add manually
        rates.keys().forEach { key ->
            result[key] = rates.getDouble(key)
        }

        cachedRates = result
        result
    } catch (e: Exception) {
        cachedRates
    }
}



@Composable
fun CurrencyConverterScreen() {
    var input by remember { mutableStateOf("") }
    var fromUnit by remember { mutableStateOf("USD") }
    var toUnit by remember { mutableStateOf("EUR") }
    var rates by remember { mutableStateOf<Map<String, Double>>(defaultRates) }
    var loading by remember { mutableStateOf(true) }
    var showOfflineWarning by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }

    // 🔹 Fetch rates on first load
    LaunchedEffect(Unit) {
        val fetched = fetchCurrencyRatesSafe()
        if (fetched == defaultRates) {
            showOfflineWarning = true
            snackbarHostState.showSnackbar("⚠️ Using offline currency rates")
        }
        rates = fetched
        loading = false
    }

    val inputDouble = input.toDoubleOrNull() ?: 0.0
    val result = if (rates.isNotEmpty()) {
        val valueInUSD = inputDouble / (rates[fromUnit] ?: 1.0)
        valueInUSD * (rates[toUnit] ?: 1.0)
    } else 0.0

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        val adjustedPadding = PaddingValues(
            top = 0.dp, // 🔹 Force top to 0
            bottom = paddingValues.calculateBottomPadding(),
            start = paddingValues.calculateStartPadding(LayoutDirection.Ltr),
            end = paddingValues.calculateEndPadding(LayoutDirection.Ltr)
        )
        Column(
            modifier = Modifier
                .padding(adjustedPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text("Currency Converter", style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.height(8.dp))

            if (loading) {
                CircularProgressIndicator()
            } else {
                if (showOfflineWarning) {
                    Text(
                        "⚠️ Showing cached or default rates (offline mode)",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(Modifier.height(8.dp))
                }

                // 🔹 Converter UI
                ClearableOutlinedTextField(
                    value = input,
                    onValueChange = { input = it },
                    label = "Enter amount",
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))

                UnitDropdownRow(
                    "From", fromUnit, { fromUnit = it },
                    "To", toUnit, { toUnit = it },
                    rates.keys.sorted()
                )
                Spacer(Modifier.height(8.dp))

                ResultText(String.format("%.4f %s", result, toUnit))
                Spacer(Modifier.height(16.dp))

                // 🔹 Live rates for 1 USD
                Text("Live Exchange Rates (1 USD):", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))
                rates.keys.sorted().forEach { currency ->
                    val value = rates[currency] ?: 0.0
                    Text(
                        text = "1 USD = %.4f %s".format(value, currency),
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(vertical = 2.dp)
                    )
                }
            }
        }
    }
}

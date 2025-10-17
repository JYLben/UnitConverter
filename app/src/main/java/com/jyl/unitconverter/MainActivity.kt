package com.jyl.unitconverter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.jyl.unitconverter.ui.theme.UnitConverterTheme

import android.content.Context
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
//import com.example.unitconverter.ui.theme.UnitConverterTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val context = this

        // SharedPreferences for install date
        val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        var installDate = prefs.getLong("install_date", 0L)

        // Save first launch time if not set
        if (installDate == 0L) {
            installDate = System.currentTimeMillis()
            prefs.edit().putLong("install_date", installDate).apply()
        }

        // 10 months ≈ 304 days
        val expiryTime = installDate + 304L * 24 * 60 * 60 * 1000
        val isExpired = System.currentTimeMillis() > expiryTime
        //isExpired =true
        setContent {
            ConverterApp(isExpired)
            /*UnitConverterTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Surface(modifier = Modifier.fillMaxSize().padding(innerPadding),
                        color = MaterialTheme.colorScheme.background) {
                        var selectedScreen by remember { mutableStateOf("Length") }
                        Column {
                            ScrollableButtonRow(
                                listOf("Length", "Weight", "Volume", "Temp", "Area", "Currency", "About"),
                                selectedScreen
                            ) { selectedScreen = it }

                            when (selectedScreen) {
                                "Length" -> ImperialMetricConverterScreen()
                                "Weight" -> WeightConverterScreen()
                                "Volume" -> VolumeConverterScreen()
                                "Temp" -> TemperatureConverterScreen()
                                "Area" -> AreaConverterScreen()
                                "Currency" -> CurrencyConverterScreen()
                                "About" -> AboutScreen()
                            }
                        }

                    }
                }
            }*/
        }
    }
}


@Composable
fun
        ConverterApp(
    isExpired: Boolean
) {
    UnitConverterTheme {
        if (isExpired) {
            ExpiredScreen()
        } else {
            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                Surface(modifier = Modifier.fillMaxSize().padding(innerPadding),
                    color = MaterialTheme.colorScheme.background) {
                    var selectedScreen by remember { mutableStateOf("Length") }
                    Column {
                        ScrollableButtonRow(
                            listOf("Length", "Weight", "Volume", "Temp", "Area", "Currency", "About"),
                            selectedScreen
                        ) { selectedScreen = it }

                        when (selectedScreen) {
                            "Length" -> ImperialMetricConverterScreen()
                            "Weight" -> WeightConverterScreen()
                            "Volume" -> VolumeConverterScreen()
                            "Temp" -> TemperatureConverterScreen()
                            "Area" -> AreaConverterScreen()
                            "Currency" -> CurrencyConverterScreen()
                            "About" -> AboutScreen()
                        }
                    }

                }
            }
        }
    }
}

@Composable
fun
        ExpiredScreen1(expiryTime: Long) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(80.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "This version of the app has expired.",
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = expiryTime.toString()+"Please download the latest version from Google Play.",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun ExpiredScreen() {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(80.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "This version of the app has expired.",
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Please download the latest version from Google Play.",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun ScrollableButtonRow(buttons: List<String>, selected: String, onSelect: (String) -> Unit) {
    Row(
        Modifier
            .horizontalScroll(rememberScrollState())
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        buttons.forEach { label ->
            FilledTonalButton(
                onClick = { onSelect(label) },
                modifier = Modifier.padding(horizontal = 1.dp),
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = if (label == selected)
                        MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondaryContainer
                ),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(label, style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}

// ---------- Clearable TextField ----------
@Composable
fun ClearableOutlinedTextField(value: String, onValueChange: (String) -> Unit, label: String, modifier: Modifier = Modifier) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = modifier.fillMaxWidth(),
        trailingIcon = {
            if (value.isNotEmpty()) {
                IconButton(onClick = { onValueChange("") }) {
                    Icon(Icons.Filled.Close, contentDescription = "Clear")
                }
            }
        }
    )
}

// ---------- Result Display Card ----------
@Composable
fun ResultText(text: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = MaterialTheme.shapes.medium
    ) {
        Text(
            text,
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
fun UnitDropdown(label: String, selectedUnit: String, unitList: List<String>, onUnitSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        Text(label)
        Box {
            OutlinedButton(onClick = { expanded = true }) {
                Text(selectedUnit)
            }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                unitList.sorted().forEach {
                    DropdownMenuItem(
                        text = { Text(it) },
                        onClick = {
                            onUnitSelected(it)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun UnitDropdownRow(labelFrom: String, fromUnit: String, onFromChange: (String) -> Unit, labelTo: String, toUnit: String, onToChange: (String) -> Unit, units: List<String>) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            //Text(labelFrom, style = MaterialTheme.typography.labelSmall)
            UnitDropdown(labelFrom, fromUnit, units) { onFromChange(it) }
        }
        Column(modifier = Modifier.weight(1f)) {
            //Text(labelTo, style = MaterialTheme.typography.labelSmall)
            UnitDropdown(labelTo, toUnit, units) { onToChange(it) }
        }
    }
}

/*
class MainActivity :
    ComponentActivity() {
    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(
            savedInstanceState
        )
        enableEdgeToEdge()
        setContent {
            UnitConverterTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(
                            innerPadding
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(
    name: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(
    showBackground = true
)
@Composable
fun GreetingPreview() {
    UnitConverterTheme {
        Greeting(
            "Android"
        )
    }
}*/

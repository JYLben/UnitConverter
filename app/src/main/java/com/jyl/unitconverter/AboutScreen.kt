package com.jyl.unitconverter

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AboutScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text("About This App", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(8.dp))
        Text(
            "This app provides unit and currency conversions.\n\n" +
                    "Currency exchange rates are provided by the Frankfurter API, " +
                    "which sources its data from the European Central Bank.",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(Modifier.height(16.dp))
        Text("Terms of Use", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(4.dp))
        Text(
            "• Free for personal and commercial projects\n" +
                    "• Rates are updated on business days\n" +
                    "• No guarantee of accuracy or availability\n" +
                    "• Please cache results to avoid excessive requests",
            style = MaterialTheme.typography.bodySmall
        )

        Spacer(Modifier.height(16.dp))
        Text("Privacy Policy", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(4.dp))
        Text(
            "• This app does not collect, store, or share any personal information.\n" +
                    "• Currency data is fetched directly from the Frankfurter API; no user-identifiable data is transmitted.\n" +
                    "• All conversions are processed locally on your device.\n" +
                    "• If network access is unavailable, cached or default rates are used.",
            style = MaterialTheme.typography.bodySmall
        )

        Spacer(Modifier.height(16.dp))
        Text(
            "Attribution:\nhttps://www.frankfurter.app",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.primary
        )
    }
}
package dev.syamsu.billsplitter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import dev.syamsu.billsplitter.calculator.BillSplitter
import dev.syamsu.billsplitter.calculator.DefaultBillSplitter
import dev.syamsu.billsplitter.model.OrderItem
import dev.syamsu.billsplitter.model.PersonShare
import dev.syamsu.billsplitter.ui.theme.BillSplitterTheme

class MainActivity : ComponentActivity() {

    private val splitter: BillSplitter = DefaultBillSplitter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BillSplitterTheme {
                BillSplitterScreen(splitter)
            }
        }
    }
}

private val demoItems = listOf(
    OrderItem("Salad", 10.0, "Alice"),
    OrderItem("Steak", 90.0, "Bob"),
    OrderItem("Pasta", 25.0, "Carol"),
    OrderItem("Wine", 15.0, "Bob")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BillSplitterScreen(splitter: BillSplitter) {
    var taxPercent by remember { mutableStateOf("10") }
    var tipPercent by remember { mutableStateOf("20") }
    var shares by remember { mutableStateOf<List<PersonShare>>(emptyList()) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Bill Splitter") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Order", style = MaterialTheme.typography.titleMedium)

            demoItems.forEach { item ->
                Text("${item.person}: ${item.name} — ${"$%.2f".format(item.price)}")
            }

            HorizontalDivider()

            OutlinedTextField(
                value = taxPercent,
                onValueChange = { taxPercent = it },
                label = { Text("Tax %") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = tipPercent,
                onValueChange = { tipPercent = it },
                label = { Text("Tip %") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    shares = splitter.splitBill(
                        items = demoItems,
                        taxPercent = taxPercent.toDoubleOrNull() ?: 0.0,
                        tipPercent = tipPercent.toDoubleOrNull() ?: 0.0
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Split Bill")
            }

            if (shares.isNotEmpty()) {
                HorizontalDivider()
                Text("Result", style = MaterialTheme.typography.titleMedium)

                shares.forEach { share ->
                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(share.person, style = MaterialTheme.typography.titleSmall)
                            Text("Items: ${"$%.2f".format(share.itemsTotal)}")
                            Text("Tax: ${"$%.2f".format(share.taxShare)}")
                            Text("Tip: ${"$%.2f".format(share.tipShare)}")
                            Text(
                                "Owes: ${"$%.2f".format(share.total)}",
                                style = MaterialTheme.typography.titleSmall
                            )
                        }
                    }
                }

                val grandTotal = shares.sumOf { it.total }
                Text(
                    "Grand Total: ${"$%.2f".format(grandTotal)}",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

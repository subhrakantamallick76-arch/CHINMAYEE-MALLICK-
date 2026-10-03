package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.TransactionRecord
import com.example.model.Wallet
import com.example.ui.theme.*

@Composable
fun WalletScreen(
    wallet: Wallet,
    transactions: List<TransactionRecord>,
    onAddCash: (Double) -> Unit,
    onWithdrawCash: (Double) -> Unit,
    onBack: () -> Unit
) {
    var showAddCashDialog by remember { mutableStateOf(false) }
    var showWithdrawDialog by remember { mutableStateOf(false) }
    var customAmount by remember { mutableStateOf("200") }

    Scaffold(
        topBar = {
            Surface(
                color = DarkSurface,
                tonalElevation = 4.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = TextPrimary
                        )
                    }

                    Column {
                        Text(
                            text = "My SK Cotrage Wallet",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = TextPrimary
                        )
                        Text(
                            text = "Instant 100% Safe Payments",
                            fontSize = 11.sp,
                            color = PitchGreen
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .testTag("wallet_screen"),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Main Balance Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, DarkSurfaceBorder)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.verticalGradient(
                                    listOf(
                                        Color(0xFF1E293B),
                                        DarkSurface
                                    )
                                )
                            )
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Column {
                            Text(
                                text = "TOTAL BALANCE",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextSecondary,
                                letterSpacing = 1.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "₹${wallet.totalBalance.toInt()}",
                                fontSize = 36.sp,
                                fontWeight = FontWeight.Black,
                                color = TrophyGold
                            )
                        }

                        // Action Buttons: Add Cash & Withdraw
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Button(
                                onClick = { showAddCashDialog = true },
                                modifier = Modifier.weight(1f).testTag("add_cash_action_btn"),
                                colors = ButtonDefaults.buttonColors(containerColor = PitchGreen),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(imageVector = Icons.Default.AddCircle, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Add Cash", fontWeight = FontWeight.Bold)
                            }

                            Button(
                                onClick = { showWithdrawDialog = true },
                                modifier = Modifier.weight(1f).testTag("withdraw_action_btn"),
                                colors = ButtonDefaults.buttonColors(containerColor = PrimaryRed),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(imageVector = Icons.Default.Payments, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Withdraw", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // Wallet Breakdown
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, DarkSurfaceBorder)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Text(
                            text = "Balance Breakdown",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = TextPrimary
                        )

                        // Deposit Cash
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Unutilized / Deposit Cash", fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = TextPrimary)
                                Text("Used to join contests", fontSize = 11.sp, color = TextSecondary)
                            }
                            Text("₹${wallet.depositBalance.toInt()}", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = TextPrimary)
                        }

                        HorizontalDivider(color = DarkSurfaceBorder, thickness = 0.6.dp)

                        // Winnings Cash
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Winnings Cash", fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = PitchGreen)
                                Text("Withdrawable anytime to UPI / Bank", fontSize = 11.sp, color = TextSecondary)
                            }
                            Text("₹${wallet.winningBalance.toInt()}", fontWeight = FontWeight.Black, fontSize = 15.sp, color = PitchGreen)
                        }

                        HorizontalDivider(color = DarkSurfaceBorder, thickness = 0.6.dp)

                        // Cash Bonus
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Cash Bonus", fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = TrophyGold)
                                Text("Usable up to 10% in mega leagues", fontSize = 11.sp, color = TextSecondary)
                            }
                            Text("₹${wallet.bonusBalance.toInt()}", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = TrophyGold)
                        }
                    }
                }
            }

            // Quick Add Cash Shortcuts
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, DarkSurfaceBorder)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Quick Recharge Packs",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = TextPrimary
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf(100.0, 200.0, 500.0, 1000.0).forEach { amt ->
                                Surface(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(8.dp))
                                        .clickable { onAddCash(amt) },
                                    color = DarkSurfaceElevated,
                                    border = androidx.compose.foundation.BorderStroke(1.dp, DarkSurfaceBorder)
                                ) {
                                    Column(
                                        modifier = Modifier.padding(vertical = 10.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = "+₹${amt.toInt()}",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp,
                                            color = TextPrimary
                                        )
                                        Text(
                                            text = "Instant",
                                            fontSize = 9.sp,
                                            color = PitchGreen
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Transaction History
            item {
                Text(
                    text = "Recent Transactions",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = TextPrimary
                )
            }

            if (transactions.isEmpty()) {
                item {
                    Text(
                        text = "No recent transactions found.",
                        color = TextSecondary,
                        fontSize = 13.sp
                    )
                }
            } else {
                items(transactions) { tx ->
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = DarkSurface,
                        shape = RoundedCornerShape(10.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, DarkSurfaceBorder)
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(if (tx.isCredit) PitchGreen.copy(alpha = 0.2f) else PrimaryRed.copy(alpha = 0.2f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = if (tx.isCredit) Icons.Default.ArrowDownward else Icons.Default.ArrowUpward,
                                        contentDescription = null,
                                        tint = if (tx.isCredit) PitchGreen else PrimaryRed,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Column {
                                    Text(
                                        text = tx.title,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = TextPrimary
                                    )
                                    Text(
                                        text = tx.dateText,
                                        fontSize = 11.sp,
                                        color = TextSecondary
                                    )
                                }
                            }

                            Text(
                                text = "${if (tx.isCredit) "+" else "-"}₹${tx.amount.toInt()}",
                                fontWeight = FontWeight.Black,
                                fontSize = 15.sp,
                                color = if (tx.isCredit) PitchGreen else PrimaryRed
                            )
                        }
                    }
                }
            }
        }

        // Add Cash Dialog
        if (showAddCashDialog) {
            AlertDialog(
                onDismissRequest = { showAddCashDialog = false },
                containerColor = DarkSurface,
                title = { Text("Add Cash to Wallet", fontWeight = FontWeight.Bold, color = TextPrimary) },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text("Enter Amount to Add (₹)", fontSize = 13.sp, color = TextSecondary)
                        OutlinedTextField(
                            value = customAmount,
                            onValueChange = { customAmount = it },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = PrimaryRed,
                                unfocusedBorderColor = DarkSurfaceBorder,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf("100", "200", "500").forEach { preset ->
                                Surface(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .clickable { customAmount = preset },
                                    color = DarkSurfaceElevated,
                                    border = androidx.compose.foundation.BorderStroke(1.dp, DarkSurfaceBorder)
                                ) {
                                    Text(
                                        text = "+₹$preset",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TrophyGold,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            val amt = customAmount.toDoubleOrNull() ?: 100.0
                            onAddCash(amt)
                            showAddCashDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = PitchGreen)
                    ) {
                        Text("Pay & Add ₹$customAmount")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showAddCashDialog = false }) {
                        Text("Cancel", color = TextSecondary)
                    }
                }
            )
        }

        // Withdraw Dialog
        if (showWithdrawDialog) {
            var withdrawAmount by remember { mutableStateOf("100") }
            var upiId by remember { mutableStateOf("user@upi") }

            AlertDialog(
                onDismissRequest = { showWithdrawDialog = false },
                containerColor = DarkSurface,
                title = { Text("Instant Withdrawal", fontWeight = FontWeight.Bold, color = TextPrimary) },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text("Available Winnings: ₹${wallet.winningBalance.toInt()}", fontSize = 13.sp, color = PitchGreen, fontWeight = FontWeight.Bold)

                        OutlinedTextField(
                            value = withdrawAmount,
                            onValueChange = { withdrawAmount = it },
                            label = { Text("Withdrawal Amount (₹)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = upiId,
                            onValueChange = { upiId = it },
                            label = { Text("UPI ID (e.g. mobile@upi)") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            val amt = withdrawAmount.toDoubleOrNull() ?: 50.0
                            onWithdrawCash(amt)
                            showWithdrawDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryRed)
                    ) {
                        Text("Withdraw Instant")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showWithdrawDialog = false }) {
                        Text("Cancel", color = TextSecondary)
                    }
                }
            )
        }
    }
}

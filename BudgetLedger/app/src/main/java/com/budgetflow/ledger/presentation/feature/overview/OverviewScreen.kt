package com.budgetflow.ledger.presentation.feature.overview

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.budgetflow.ledger.domain.model.FinancialRecord
import com.budgetflow.ledger.presentation.components.RecordListItem
import com.budgetflow.ledger.presentation.components.WalletCardPager
import com.budgetflow.ledger.presentation.components.WalletDisplayData

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OverviewScreen(
    onNavigateToRecordEntry: (String) -> Unit,
    viewModel: OverviewViewModel = hiltViewModel()
) {
    val screenState by viewModel.screenState.collectAsState()
    val walletsWithBalances by viewModel.walletsWithBalances.collectAsState()
    val walletRecords by viewModel.walletRecords.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
    ) {
        when (val state = screenState) {
            is OverviewScreenState.Loading -> {
                LoadingState()
            }

            is OverviewScreenState.Success -> {
                OverviewContent(
                    walletsWithBalances = walletsWithBalances,
                    records = walletRecords,
                    onPageChanged = { viewModel.selectWalletByIndex(it) },
                    onAddIncome = { onNavigateToRecordEntry("CREDIT") },
                    onAddExpense = { onNavigateToRecordEntry("DEBIT") }
                )
            }

            is OverviewScreenState.Error -> {
                ErrorState(message = state.message)
            }
        }
    }
}

@Composable
private fun LoadingState(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun ErrorState(
    message: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Error: $message",
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun OverviewContent(
    modifier: Modifier = Modifier,
    walletsWithBalances: List<WalletDisplayData>,
    records: List<FinancialRecord>,
    onPageChanged: (Int) -> Unit,
    onAddIncome: () -> Unit,
    onAddExpense: () -> Unit
) {
    if (walletsWithBalances.isEmpty()) return

    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { walletsWithBalances.size }
    )

    // Sync page changes to ViewModel
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            onPageChanged(page)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 8.dp)
    ) {
        // Custom header with profile icon and app name
        CustomHeader()

        Spacer(modifier = Modifier.height(8.dp))

        // Large wallet card pager with balance and action buttons
        WalletCardPager(
            wallets = walletsWithBalances,
            pagerState = pagerState,
            onAddIncome = { onAddIncome() },
            onAddExpense = { onAddExpense() }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Recent transactions header
        Text(
            text = "Recent Records",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
        )

        // Records list
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentPadding = PaddingValues(top = 4.dp, bottom = 80.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (records.isEmpty()) {
                item {
                    EmptyRecordsCard(modifier = Modifier.padding(horizontal = 16.dp))
                }
            } else {
                items(records) { record ->
                    RecordListItem(
                        record = record,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun CustomHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Profile icon on left
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Profile",
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(24.dp)
            )
        }

        // App name centered
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Budget Ledger",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        // Empty space for symmetry
        Box(modifier = Modifier.size(44.dp))
    }
}

@Composable
private fun EmptyRecordsCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No records yet. Add your first entry!",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

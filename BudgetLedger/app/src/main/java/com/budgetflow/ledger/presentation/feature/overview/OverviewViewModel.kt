package com.budgetflow.ledger.presentation.feature.overview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.budgetflow.ledger.data.repository.FinancialRecordRepository
import com.budgetflow.ledger.data.repository.UserDataRepository
import com.budgetflow.ledger.domain.model.FinancialRecord
import com.budgetflow.ledger.domain.model.RecordConverter
import com.budgetflow.ledger.domain.model.RecordType
import com.budgetflow.ledger.domain.model.UserDashboard
import com.budgetflow.ledger.domain.model.Wallet
import com.budgetflow.ledger.presentation.components.WalletDisplayData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OverviewViewModel @Inject constructor(
    private val userDataRepository: UserDataRepository,
    private val recordRepository: FinancialRecordRepository
) : ViewModel() {

    private val _dashboardData = MutableStateFlow<UserDashboard?>(null)
    val dashboardData: StateFlow<UserDashboard?> = _dashboardData.asStateFlow()

    private val _activeWalletId = MutableStateFlow<String?>(null)
    val activeWalletId: StateFlow<String?> = _activeWalletId.asStateFlow()

    private val _screenState = MutableStateFlow<OverviewScreenState>(OverviewScreenState.Loading)
    val screenState: StateFlow<OverviewScreenState> = _screenState.asStateFlow()

    private val _allRecordsMap = MutableStateFlow<Map<String, List<FinancialRecord>>>(emptyMap())
    val allRecordsMap: StateFlow<Map<String, List<FinancialRecord>>> = _allRecordsMap.asStateFlow()

    private val _currentWalletIndex = MutableStateFlow(0)
    val currentWalletIndex: StateFlow<Int> = _currentWalletIndex.asStateFlow()

    val walletRecords: StateFlow<List<FinancialRecord>> = _activeWalletId
        .flatMapLatest { walletId ->
            if (walletId != null) {
                recordRepository.observeWalletRecords(walletId)
            } else {
                flowOf(emptyList())
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val activeWallet: StateFlow<Wallet?> = combine(
        _dashboardData,
        _activeWalletId
    ) { dashboard, walletId ->
        dashboard?.accounts?.find { it.id == walletId }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    val walletsWithBalances: StateFlow<List<WalletDisplayData>> = combine(
        _dashboardData,
        _allRecordsMap
    ) { dashboard, recordsMap ->
        dashboard?.accounts?.map { wallet ->
            val records = recordsMap[wallet.id] ?: emptyList()
            val initialBalance = wallet.balance.currentAmount
            val adjustment = records.fold(0.0) { total, record ->
                when (record.recordType) {
                    RecordType.CREDIT -> total + record.amount
                    RecordType.DEBIT -> total - record.amount
                }
            }
            WalletDisplayData(
                wallet = wallet,
                calculatedBalance = initialBalance + adjustment
            )
        } ?: emptyList()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    init {
        loadDashboard()
        observeAllWalletRecords()
    }

    private fun observeAllWalletRecords() {
        viewModelScope.launch {
            _dashboardData.collect { dashboard ->
                dashboard?.accounts?.forEach { wallet ->
                    viewModelScope.launch {
                        recordRepository.observeWalletRecords(wallet.id).collect { records ->
                            _allRecordsMap.value = _allRecordsMap.value.toMutableMap().apply {
                                put(wallet.id, records)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun loadDashboard() {
        viewModelScope.launch {
            _screenState.value = OverviewScreenState.Loading

            userDataRepository.fetchDashboard()
                .onSuccess { dashboard ->
                    _dashboardData.value = dashboard
                    _screenState.value = OverviewScreenState.Success(dashboard)

                    syncWalletRecords(dashboard.accounts)

                    if (dashboard.accounts.isNotEmpty()) {
                        selectWallet(dashboard.accounts.first().id)
                    }
                }
                .onFailure { error ->
                    _screenState.value = OverviewScreenState.Error(
                        error.message ?: "Failed to load dashboard"
                    )
                }
        }
    }

    private suspend fun syncWalletRecords(wallets: List<Wallet>) {
        wallets.forEach { wallet ->
            wallet.transactions?.let { remoteRecords ->
                if (remoteRecords.isNotEmpty()) {
                    val localRecords = remoteRecords.map { remote ->
                        RecordConverter.toFinancialRecord(remote, wallet.id)
                    }
                    recordRepository.clearWalletRecords(wallet.id)
                    recordRepository.saveRecords(localRecords)
                }
            }
        }
    }

    fun selectWallet(walletId: String) {
        _activeWalletId.value = walletId
        // Update index based on walletId
        _dashboardData.value?.accounts?.indexOfFirst { it.id == walletId }?.let { index ->
            if (index >= 0) _currentWalletIndex.value = index
        }
    }

    fun selectWalletByIndex(index: Int) {
        _currentWalletIndex.value = index
        _dashboardData.value?.accounts?.getOrNull(index)?.let { wallet ->
            _activeWalletId.value = wallet.id
        }
    }
}

sealed class OverviewScreenState {
    data object Loading : OverviewScreenState()
    data class Success(val dashboard: UserDashboard) : OverviewScreenState()
    data class Error(val message: String) : OverviewScreenState()
}

package com.budgetflow.ledger.presentation.feature.recordentry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.budgetflow.ledger.data.repository.FinancialRecordRepository
import com.budgetflow.ledger.domain.model.FinancialRecord
import com.budgetflow.ledger.domain.model.RecordType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class RecordEntryViewModel @Inject constructor(
    private val recordRepository: FinancialRecordRepository
) : ViewModel() {

    private val _walletId = MutableStateFlow<String?>(null)

    private val _descriptionInput = MutableStateFlow("")
    val descriptionInput: StateFlow<String> = _descriptionInput.asStateFlow()

    private val _amountInput = MutableStateFlow("")
    val amountInput: StateFlow<String> = _amountInput.asStateFlow()

    private val _selectedRecordType = MutableStateFlow(RecordType.CREDIT)
    val selectedRecordType: StateFlow<RecordType> = _selectedRecordType.asStateFlow()

    private val _entryState = MutableStateFlow<RecordEntryState>(RecordEntryState.Idle)
    val entryState: StateFlow<RecordEntryState> = _entryState.asStateFlow()

    fun initialize(walletId: String, recordType: String) {
        _walletId.value = walletId
        _selectedRecordType.value = when (recordType.uppercase()) {
            "CREDIT" -> RecordType.CREDIT
            "DEBIT" -> RecordType.DEBIT
            else -> RecordType.CREDIT
        }
    }

    fun updateDescription(value: String) {
        _descriptionInput.value = value
    }

    fun updateAmount(value: String) {
        _amountInput.value = value
    }

    fun updateRecordType(type: RecordType) {
        _selectedRecordType.value = type
    }

    fun saveRecord() {
        viewModelScope.launch {
            val currentWalletId = _walletId.value
            if (currentWalletId == null) {
                _entryState.value = RecordEntryState.Error("Wallet not selected")
                return@launch
            }

            val description = _descriptionInput.value.trim()
            if (description.isEmpty()) {
                _entryState.value = RecordEntryState.Error("Description is required")
                return@launch
            }

            val amount = _amountInput.value.toDoubleOrNull()
            if (amount == null || amount <= 0) {
                _entryState.value = RecordEntryState.Error("Enter a valid amount greater than zero")
                return@launch
            }

            _entryState.value = RecordEntryState.Saving

            try {
                val newRecord = FinancialRecord(
                    walletId = currentWalletId,
                    description = description,
                    amount = amount,
                    timestamp = System.currentTimeMillis(),
                    recordType = _selectedRecordType.value
                )

                recordRepository.saveRecord(newRecord)
                _entryState.value = RecordEntryState.Saved

                clearForm()
            } catch (exception: Exception) {
                _entryState.value = RecordEntryState.Error(
                    exception.message ?: "Failed to save record"
                )
            }
        }
    }

    private fun clearForm() {
        _descriptionInput.value = ""
        _amountInput.value = ""
    }

    fun resetState() {
        _entryState.value = RecordEntryState.Idle
    }
}

sealed class RecordEntryState {
    data object Idle : RecordEntryState()
    data object Saving : RecordEntryState()
    data object Saved : RecordEntryState()
    data class Error(val message: String) : RecordEntryState()
}

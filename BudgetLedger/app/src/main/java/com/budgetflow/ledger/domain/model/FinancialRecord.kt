package com.budgetflow.ledger.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "financial_records")
data class FinancialRecord(
    @PrimaryKey(autoGenerate = true)
    val recordId: Long = 0L,
    val walletId: String,
    val description: String,
    val amount: Double,
    val timestamp: Long,
    val recordType: RecordType
)

enum class RecordType {
    CREDIT,
    DEBIT
}

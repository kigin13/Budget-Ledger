package com.budgetflow.ledger.presentation.feature.overview

import com.budgetflow.ledger.domain.model.FinancialRecord
import com.budgetflow.ledger.domain.model.RecordType
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Unit tests for balance calculation - core financial logic.
 */
class BalanceCalculationTest {

    private fun calculateBalance(initialBalance: Double, records: List<FinancialRecord>): Double {
        val adjustment = records.fold(0.0) { total, record ->
            when (record.recordType) {
                RecordType.CREDIT -> total + record.amount
                RecordType.DEBIT -> total - record.amount
            }
        }
        return initialBalance + adjustment
    }

    private fun createRecord(amount: Double, type: RecordType) = FinancialRecord(
        recordId = 0L,
        walletId = "test-wallet",
        description = "Test",
        amount = amount,
        timestamp = System.currentTimeMillis(),
        recordType = type
    )

    @Test
    fun `balance with no records equals initial balance`() {
        val result = calculateBalance(1000.0, emptyList())
        assertEquals(1000.0, result, 0.001)
    }

    @Test
    fun `credit increases balance`() {
        val records = listOf(createRecord(500.0, RecordType.CREDIT))
        val result = calculateBalance(1000.0, records)
        assertEquals(1500.0, result, 0.001)
    }

    @Test
    fun `debit decreases balance`() {
        val records = listOf(createRecord(300.0, RecordType.DEBIT))
        val result = calculateBalance(1000.0, records)
        assertEquals(700.0, result, 0.001)
    }

    @Test
    fun `mixed credits and debits calculate correctly`() {
        val records = listOf(
            createRecord(500.0, RecordType.CREDIT),
            createRecord(200.0, RecordType.DEBIT),
            createRecord(300.0, RecordType.CREDIT),
            createRecord(100.0, RecordType.DEBIT)
        )
        val result = calculateBalance(1000.0, records)
        assertEquals(1500.0, result, 0.001) // 1000 + 500 - 200 + 300 - 100
    }

    @Test
    fun `balance can go negative`() {
        val records = listOf(createRecord(500.0, RecordType.DEBIT))
        val result = calculateBalance(100.0, records)
        assertEquals(-400.0, result, 0.001)
    }
}

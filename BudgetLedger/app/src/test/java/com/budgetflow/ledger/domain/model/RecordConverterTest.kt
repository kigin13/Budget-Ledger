package com.budgetflow.ledger.domain.model

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Unit tests for RecordConverter - critical utility for API data transformation.
 */
class RecordConverterTest {

    private val testWalletId = "wallet-123"

    @Test
    fun `maps 'in' type code to CREDIT record type`() {
        val remoteRecord = RemoteRecord(
            description = "Salary",
            amount = 5000.0,
            dateString = "2024-01-15",
            typeCode = "in"
        )

        val result = RecordConverter.toFinancialRecord(remoteRecord, testWalletId)

        assertEquals(RecordType.CREDIT, result.recordType)
    }

    @Test
    fun `maps 'out' type code to DEBIT record type`() {
        val remoteRecord = RemoteRecord(
            description = "Groceries",
            amount = 150.0,
            dateString = "2024-01-10",
            typeCode = "out"
        )

        val result = RecordConverter.toFinancialRecord(remoteRecord, testWalletId)

        assertEquals(RecordType.DEBIT, result.recordType)
    }

    @Test
    fun `defaults unknown type code to DEBIT`() {
        val remoteRecord = RemoteRecord(
            description = "Unknown",
            amount = 50.0,
            dateString = "2024-03-01",
            typeCode = "invalid"
        )

        val result = RecordConverter.toFinancialRecord(remoteRecord, testWalletId)

        assertEquals(RecordType.DEBIT, result.recordType)
    }

    @Test
    fun `preserves all record fields correctly`() {
        val remoteRecord = RemoteRecord(
            description = "Freelance work",
            amount = 2500.50,
            dateString = "2024-01-20",
            typeCode = "in"
        )

        val result = RecordConverter.toFinancialRecord(remoteRecord, "my-wallet")

        assertEquals("my-wallet", result.walletId)
        assertEquals("Freelance work", result.description)
        assertEquals(2500.50, result.amount, 0.001)
    }

    @Test
    fun `handles invalid date format gracefully`() {
        val remoteRecord = RemoteRecord(
            description = "Bad date test",
            amount = 50.0,
            dateString = "invalid-date",
            typeCode = "out"
        )

        val result = RecordConverter.toFinancialRecord(remoteRecord, testWalletId)

        assert(result.timestamp > 0) { "Should fallback to current time" }
    }
}

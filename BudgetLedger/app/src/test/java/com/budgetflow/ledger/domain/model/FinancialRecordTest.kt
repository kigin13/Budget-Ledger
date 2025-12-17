package com.budgetflow.ledger.domain.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

/**
 * Unit tests for FinancialRecord and RecordType models.
 */
class FinancialRecordTest {

    @Test
    fun `create record with all fields`() {
        val record = FinancialRecord(
            recordId = 1L,
            walletId = "wallet-1",
            description = "Salary",
            amount = 5000.0,
            timestamp = 1704067200000L,
            recordType = RecordType.CREDIT
        )

        assertEquals(1L, record.recordId)
        assertEquals("wallet-1", record.walletId)
        assertEquals("Salary", record.description)
        assertEquals(5000.0, record.amount, 0.001)
        assertEquals(RecordType.CREDIT, record.recordType)
    }

    @Test
    fun `default recordId is zero for auto-generation`() {
        val record = FinancialRecord(
            walletId = "wallet-1",
            description = "Test",
            amount = 100.0,
            timestamp = System.currentTimeMillis(),
            recordType = RecordType.CREDIT
        )

        assertEquals(0L, record.recordId)
    }

    @Test
    fun `RecordType enum has CREDIT and DEBIT values`() {
        assertEquals(2, RecordType.entries.size)
        assertNotEquals(RecordType.CREDIT, RecordType.DEBIT)
    }
}

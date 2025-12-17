package com.budgetflow.ledger.presentation.feature.recordentry

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

/**
 * Unit tests for form validation rules.
 */
class RecordEntryValidationTest {

    private fun validateDescription(description: String): String? {
        return if (description.trim().isEmpty()) "Description is required" else null
    }

    private fun validateAmount(amountString: String): String? {
        val amount = amountString.toDoubleOrNull()
        return if (amount == null || amount <= 0) "Enter a valid amount greater than zero" else null
    }

    @Test
    fun `empty description returns error`() {
        assertEquals("Description is required", validateDescription(""))
    }

    @Test
    fun `valid description returns no error`() {
        assertNull(validateDescription("Groceries"))
    }

    @Test
    fun `empty amount returns error`() {
        assertEquals("Enter a valid amount greater than zero", validateAmount(""))
    }

    @Test
    fun `non-numeric amount returns error`() {
        assertEquals("Enter a valid amount greater than zero", validateAmount("abc"))
    }

    @Test
    fun `zero amount returns error`() {
        assertEquals("Enter a valid amount greater than zero", validateAmount("0"))
    }

    @Test
    fun `negative amount returns error`() {
        assertEquals("Enter a valid amount greater than zero", validateAmount("-50"))
    }

    @Test
    fun `valid amount returns no error`() {
        assertNull(validateAmount("100.50"))
    }
}

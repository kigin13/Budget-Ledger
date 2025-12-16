package com.budgetflow.ledger.domain.model

import java.text.SimpleDateFormat
import java.util.Locale

object RecordConverter {

    private const val API_DATE_PATTERN = "yyyy-MM-dd"

    fun toFinancialRecord(
        remoteRecord: RemoteRecord,
        walletId: String
    ): FinancialRecord {
        return FinancialRecord(
            walletId = walletId,
            description = remoteRecord.description,
            amount = remoteRecord.amount,
            timestamp = parseDateToMillis(remoteRecord.dateString),
            recordType = mapTypeCode(remoteRecord.typeCode)
        )
    }

    private fun parseDateToMillis(dateString: String): Long {
        return try {
            val formatter = SimpleDateFormat(API_DATE_PATTERN, Locale.US)
            formatter.parse(dateString)?.time ?: System.currentTimeMillis()
        } catch (e: Exception) {
            System.currentTimeMillis()
        }
    }

    private fun mapTypeCode(code: String): RecordType {
        return when (code.lowercase(Locale.US)) {
            "in" -> RecordType.CREDIT
            "out" -> RecordType.DEBIT
            else -> RecordType.DEBIT
        }
    }
}

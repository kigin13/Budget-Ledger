package com.budgetflow.ledger.data.repository

import com.budgetflow.ledger.data.local.FinancialRecordDao
import com.budgetflow.ledger.domain.model.FinancialRecord
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FinancialRecordRepository @Inject constructor(
    private val recordDao: FinancialRecordDao
) {

    fun observeWalletRecords(walletId: String): Flow<List<FinancialRecord>> {
        return recordDao.observeRecordsByWallet(walletId)
    }

    suspend fun saveRecord(record: FinancialRecord): Long {
        return recordDao.insertRecord(record)
    }

    suspend fun saveRecords(records: List<FinancialRecord>) {
        records.forEach { record ->
            recordDao.insertRecord(record)
        }
    }

    suspend fun clearWalletRecords(walletId: String) {
        recordDao.clearWalletRecords(walletId)
    }
}

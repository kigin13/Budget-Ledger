package com.budgetflow.ledger.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.budgetflow.ledger.domain.model.FinancialRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface FinancialRecordDao {

    @Query("SELECT * FROM financial_records WHERE walletId = :walletId ORDER BY timestamp DESC")
    fun observeRecordsByWallet(walletId: String): Flow<List<FinancialRecord>>

    @Query("SELECT * FROM financial_records ORDER BY timestamp DESC")
    fun observeAllRecords(): Flow<List<FinancialRecord>>

    @Query("SELECT * FROM financial_records WHERE recordId = :recordId")
    suspend fun fetchRecordById(recordId: Long): FinancialRecord?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecord(record: FinancialRecord): Long

    @Query("DELETE FROM financial_records WHERE recordId = :recordId")
    suspend fun removeRecord(recordId: Long)

    @Query("DELETE FROM financial_records WHERE walletId = :walletId")
    suspend fun clearWalletRecords(walletId: String)

    @Query("DELETE FROM financial_records")
    suspend fun clearAllRecords()
}

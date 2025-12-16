package com.budgetflow.ledger.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.budgetflow.ledger.domain.model.FinancialRecord

@Database(
    entities = [FinancialRecord::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(RecordTypeConverter::class)
abstract class LedgerDatabase : RoomDatabase() {

    abstract fun financialRecordDao(): FinancialRecordDao

    companion object {
        const val DB_NAME = "budget_ledger_db"
    }
}

package com.budgetflow.ledger.di

import android.content.Context
import androidx.room.Room
import com.budgetflow.ledger.data.local.FinancialRecordDao
import com.budgetflow.ledger.data.local.LedgerDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PersistenceModule {

    @Provides
    @Singleton
    fun provideLedgerDatabase(@ApplicationContext context: Context): LedgerDatabase {
        return Room.databaseBuilder(
            context,
            LedgerDatabase::class.java,
            LedgerDatabase.DB_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideFinancialRecordDao(database: LedgerDatabase): FinancialRecordDao {
        return database.financialRecordDao()
    }
}

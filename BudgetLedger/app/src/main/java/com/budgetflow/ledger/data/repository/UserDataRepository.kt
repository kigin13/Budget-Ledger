package com.budgetflow.ledger.data.repository

import com.budgetflow.ledger.data.remote.RemoteUserDataProvider
import com.budgetflow.ledger.domain.model.UserDashboard
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserDataRepository @Inject constructor(
    private val remoteProvider: RemoteUserDataProvider
) {

    suspend fun fetchDashboard(): Result<UserDashboard> {
        return remoteProvider.loadDashboardData()
    }
}

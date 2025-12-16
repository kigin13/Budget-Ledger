package com.budgetflow.ledger.data.remote

import com.budgetflow.ledger.domain.model.UserDashboard
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteUserDataProvider @Inject constructor(
    private val apiGateway: FinanceApiGateway
) {

    suspend fun loadDashboardData(): Result<UserDashboard> = withContext(Dispatchers.IO) {
        try {
            val dashboard = apiGateway.fetchUserDashboard()
            Result.success(dashboard)
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }
}

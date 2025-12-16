package com.budgetflow.ledger.data.remote

import com.budgetflow.ledger.domain.model.UserDashboard
import retrofit2.http.GET

interface FinanceApiGateway {

    @GET("/api/userData")
    suspend fun fetchUserDashboard(): UserDashboard
}

package com.budgetflow.ledger.domain.model

data class UserDashboard(
    val user: UserProfile,
    val accounts: List<Wallet>
)

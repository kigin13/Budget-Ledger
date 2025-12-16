package com.budgetflow.ledger.domain.model

import com.google.gson.annotations.SerializedName

data class WalletBalance(
    @SerializedName("totalBalance")
    val currentAmount: Double,
    @SerializedName("currency")
    val currencyCode: String = "USD"
)

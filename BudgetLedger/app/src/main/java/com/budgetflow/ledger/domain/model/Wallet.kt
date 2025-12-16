package com.budgetflow.ledger.domain.model

import com.google.gson.annotations.SerializedName

data class Wallet(
    val id: String,
    val name: String,
    val type: WalletCategory,
    val balance: WalletBalance,
    val transactions: List<RemoteRecord>? = null
)

enum class WalletCategory {
    @SerializedName("PRIMARY")
    PRIMARY,

    @SerializedName("SECONDARY")
    SECONDARY,

    @SerializedName("EXPENSE")
    EXPENSE
}

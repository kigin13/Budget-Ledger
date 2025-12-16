package com.budgetflow.ledger.domain.model

import com.google.gson.annotations.SerializedName

data class RemoteRecord(
    @SerializedName("Description")
    val description: String,
    @SerializedName("Amount")
    val amount: Double,
    @SerializedName("Date")
    val dateString: String,
    @SerializedName("Type")
    val typeCode: String
)

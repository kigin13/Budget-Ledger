package com.budgetflow.ledger.domain.model

import com.google.gson.annotations.SerializedName

data class UserProfile(
    @SerializedName("id")
    val userId: String,
    @SerializedName("name")
    val displayName: String,
    @SerializedName("email")
    val emailAddress: String,
    @SerializedName("profileImageUrl")
    val avatarUrl: String?
)

package com.budgetflow.ledger.presentation.navigation

sealed class Destination(val route: String) {

    data object Overview : Destination("overview_screen")

    data object RecordEntry : Destination("record_entry/{walletId}/{recordType}") {
        fun buildRoute(walletId: String, recordType: String): String {
            return "record_entry/$walletId/$recordType"
        }
    }
}

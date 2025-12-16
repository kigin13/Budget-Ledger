package com.budgetflow.ledger.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.budgetflow.ledger.presentation.feature.overview.OverviewScreen
import com.budgetflow.ledger.presentation.feature.overview.OverviewViewModel
import com.budgetflow.ledger.presentation.feature.recordentry.RecordEntryScreen

@Composable
fun AppNavigationHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Destination.Overview.route
    ) {
        composable(Destination.Overview.route) {
            val viewModel: OverviewViewModel = hiltViewModel()
            val activeWalletId by viewModel.activeWalletId.collectAsState()

            OverviewScreen(
                onNavigateToRecordEntry = { recordType ->
                    activeWalletId?.let { walletId ->
                        navController.navigate(
                            Destination.RecordEntry.buildRoute(walletId, recordType)
                        )
                    }
                }
            )
        }

        composable(
            route = Destination.RecordEntry.route,
            arguments = listOf(
                navArgument("walletId") { type = NavType.StringType },
                navArgument("recordType") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val walletId = backStackEntry.arguments?.getString("walletId") ?: ""
            val recordType = backStackEntry.arguments?.getString("recordType") ?: "CREDIT"

            RecordEntryScreen(
                walletId = walletId,
                initialRecordType = recordType,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}

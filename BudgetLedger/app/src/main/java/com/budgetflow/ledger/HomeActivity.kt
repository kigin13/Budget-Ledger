package com.budgetflow.ledger

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.budgetflow.ledger.presentation.navigation.AppNavigationHost
import com.budgetflow.ledger.presentation.theme.LedgerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            LedgerTheme {
                val navigationController = rememberNavController()
                AppNavigationHost(navController = navigationController)
            }
        }
    }
}

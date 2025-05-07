package org.atu

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import org.atu.ui.ClientApp

/**
 * Compose App entry point
 *
 * This class is the entry point for the client app
 *
 */
@Composable
fun App() {
    MaterialTheme {
        ClientApp()
    }
}

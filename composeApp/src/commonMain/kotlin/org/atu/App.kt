package org.atu

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import org.atu.ui.ClientApp

/**
 * Compose App entry point
 *
 * This class is the entry point for the client app
 *
 * @param serverUrl server url (differs for android client)
 *
 */
@Composable
fun App(serverUrl: String = SERVER_URL) {
    MaterialTheme {
        ClientApp(serverUrl)
    }
}

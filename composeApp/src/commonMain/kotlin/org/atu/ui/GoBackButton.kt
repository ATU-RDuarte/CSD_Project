package org.atu.ui

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable

/**
 * GoBackButton class for UI module
 *
 * This class is a UI module to create a custom go back button
 *
 * @param goBackAction callback to run on back button click action
 *
 */
@Composable
fun GoBackButton(goBackAction : () -> Unit) {
    IconButton(
        onClick = goBackAction){
        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Return to car list") }
}
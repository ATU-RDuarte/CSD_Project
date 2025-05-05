package org.atu.ui

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable

@Composable
fun GoBackButton(goBackAction : () -> Unit) {
    IconButton(
        onClick = goBackAction){
        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Return to car list") }
}
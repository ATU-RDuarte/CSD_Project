package org.atu.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.atu.viewModel.ClientApplicationViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Car List class for UI module
 *
 * This class is a UI module that lists all card lists
 *
 * @param clientApplicationViewModel application state view model
 * @param carCardOnClickAction callback that run on card click action
 *
 */
@Preview
@Composable
fun CarListScreen(clientApplicationViewModel: ClientApplicationViewModel, carCardOnClickAction: (String) -> Unit){
    val carList by clientApplicationViewModel.carListState.collectAsState()
    Scaffold {
        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Existing Cars:")
            for(car in carList)
            {
                CarCard(car, carCardOnClickAction)
            }
        }
    }
}
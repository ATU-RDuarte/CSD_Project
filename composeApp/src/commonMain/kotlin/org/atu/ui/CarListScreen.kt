package org.atu.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.atu.Car
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Car List class for UI module
 *
 * This class is a UI module that lists all card lists
 *
 * @param carList current list of cars
 * @param carCardOnClickAction callback that run on card click action
 *
 */
@Preview
@Composable
fun CarListScreen(carList : List<Car>, carCardOnClickAction: (String) -> Unit){
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